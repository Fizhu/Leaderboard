package com.fizhu.leaderboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fizhu.leaderboard.databinding.ItemListAvatarBinding

/**
 * Created by fizhu on 05,January,2021
 * https://github.com/Fizhu
 */

class AvatarAdapter(
    private val callback: (avatar: String) -> Unit
) : RecyclerView.Adapter<AvatarAdapter.ViewHolder>() {

    private val list: MutableList<String> = mutableListOf()

    fun setData(listData: List<String>) {
        list.clear()
        list.addAll(listData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemListAvatarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemListAvatarBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        with(holder.binding) {
            setImage(data, iv)
            root.setOnClickListener { callback.invoke(data) }
        }
    }

    private fun setImage(url: String, iv: ImageView) {
        Glide.with(iv.context)
            .asBitmap()
            .load(iv.context.resources.getIdentifier(url, "drawable", iv.context.packageName))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(iv)
    }
}