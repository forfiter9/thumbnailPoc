package com.rivian.thumbnailpocbytesarray

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileInputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val externalStoragePath = Environment.getExternalStorageDirectory().absolutePath
        val downloadsPath = "$externalStoragePath/Download/"
        val initMp4 = "$externalStoragePath/Download/segment_fileinit.mp4"
        getAvailableMemory()
        val extractM4sFilesOnByteLevel = extractM4sFilesOnByteLevel(pathDownload = downloadsPath)
        val initMp4InByteArray = fileToBytesArray(File(initMp4))

        val mergedInitWithSegmentsAsByteArrayList = appendSegmentToInitFileOnByteLevel(extractM4sFilesOnByteLevel = extractM4sFilesOnByteLevel, initMp4Bytes = initMp4InByteArray)
        configureRecycler(mergedInitWithSegmentsAsByteArrayList)
        getAvailableMemory()
    }

    private fun getAvailableMemory(): ActivityManager.MemoryInfo {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return ActivityManager.MemoryInfo().also { memoryInfo ->
            activityManager.getMemoryInfo(memoryInfo)
        }
    }

    private fun extractM4sFilesOnByteLevel(pathDownload: String): List<ByteArray> {
        val segmentsOnByteLevelList = mutableListOf<ByteArray>()
        val allFilesInDirectory = File(pathDownload).listFiles()
        allFilesInDirectory?.let {
            allFilesInDirectory.sortBy { it.name }
            for (file in allFilesInDirectory) {
                if (file.isFile && file.path.endsWith(m4sExtension)) {
                    segmentsOnByteLevelList.add(fileToBytesArray(file))
                }
            }
        }
        return segmentsOnByteLevelList
    }


    private fun fileToBytesArray(file: File?): ByteArray {
        var bytes = ByteArray(0)
        try {
            FileInputStream(file).use { inputStream ->
                bytes = ByteArray(inputStream.available())
                inputStream.read(bytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bytes
    }

    private fun appendSegmentToInitFileOnByteLevel(extractM4sFilesOnByteLevel: List<ByteArray>, initMp4Bytes: ByteArray): List<ByteArray> {
        val mergedByteArrayList = mutableListOf<ByteArray>()
        for (m4sFileOnByteLevel in extractM4sFilesOnByteLevel) {
            val mergedBytes: ByteArray = initMp4Bytes + m4sFileOnByteLevel
            mergedByteArrayList.add(mergedBytes)
        }
        return mergedByteArrayList
    }

    private fun configureRecycler(byteArrayLists: List<ByteArray>) {
        val rvThumbnails = findViewById<View>(R.id.recyclerView) as RecyclerView
        val adapter = ThumbnailAdapter(byteArrayLists, context = baseContext)
        rvThumbnails.adapter = adapter
        rvThumbnails.layoutManager = GridLayoutManager(this,3)
    }

    companion object {
        const val m4sExtension = ".m4s"
    }
}