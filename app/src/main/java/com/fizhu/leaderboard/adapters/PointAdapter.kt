package com.fizhu.leaderboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fizhu.leaderboard.data.models.Point
import com.fizhu.leaderboard.databinding.ItemListIconBinding

/**
 * Created by fizhu on 15,July,2020
 * https://github.com/Fizhu
 */

class PointAdapter(
    private val callBackDelete: (point: Point) -> Unit
) : RecyclerView.Adapter<PointAdapter.ViewHolder>() {

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
        var currentVelocity = 0f

        val rotation: SpringAnimation = SpringAnimation(view, SpringAnimation.ROTATION)
            .setSpring(
                SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )
            .addUpdateListener { _, _, velocity ->
                currentVelocity = velocity
            }

        val translationY: SpringAnimation = SpringAnimation(view, SpringAnimation.TRANSLATION_Y)
            .setSpring(
                SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )
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
            btnRemove.setOnClickListener {
                callBackDelete.invoke(data)
                list.remove(data)
                notifyItemRemoved(position)
            }
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