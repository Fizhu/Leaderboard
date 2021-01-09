package com.fizhu.leaderboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fizhu.leaderboard.data.models.Point
import com.fizhu.leaderboard.databinding.ItemListIconBinding
import com.fizhu.leaderboard.utils.ext.gone

/**
 * Created by fizhu on 15,July,2020
 * https://github.com/Fizhu
 */

class PointDialogAdapter(
    private val callBack: (point: Point) -> Unit
) : RecyclerView.Adapter<PointDialogAdapter.ViewHolder>() {

    private val list: MutableList<Point> = mutableListOf()

    fun setData(listData: List<Point>) {
        list.clear()
        list.addAll(listData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemListIconBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemListIconBinding.bind(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        with(holder.binding) {
            val p = data.points ?: 0
            var s = ""
            s = if (p > -1) {
                "+$p"
            } else {
                p.toString()
            }
            tvPoint.text = s
            setImage(data.icon ?: "", iv)
            btnRemove.gone()
            root.setOnClickListener { callBack.invoke(data) }
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