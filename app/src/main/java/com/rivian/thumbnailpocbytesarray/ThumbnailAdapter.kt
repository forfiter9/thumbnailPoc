package com.rivian.thumbnailpocbytesarray

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class ThumbnailAdapter (private val byteArrayList: List<ByteArray>, private val context: Context) : RecyclerView.Adapter<ThumbnailAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImage: ImageView = itemView.findViewById(R.id.thumbnailImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val thumbnailView = inflater.inflate(R.layout.thumnail_list_item, parent, false)
        return ViewHolder(thumbnailView)
    }

    override fun onBindViewHolder(viewHolder: ThumbnailAdapter.ViewHolder, position: Int) {
        Glide.with(context)
            .load(byteArrayList[position])
            .error(R.drawable.ic_launcher_background)
            .into(viewHolder.thumbnailImage)
    }

    override fun getItemCount(): Int {
        return byteArrayList.size
    }
}