package com.fizhu.leaderboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fizhu.leaderboard.R
import com.fizhu.leaderboard.data.models.Point
import com.fizhu.leaderboard.data.models.Score
import com.fizhu.leaderboard.databinding.ItemListScoreBinding
import com.fizhu.leaderboard.utils.ext.gone
import com.fizhu.leaderboard.utils.ext.loge
import com.fizhu.leaderboard.utils.ext.visible

/**
 * Created by fizhu on 15,July,2020
 * https://github.com/Fizhu
 */

class ScoreAdapter(
    private val callBack: (position: Int) -> Unit
) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    private val list: MutableList<Score> = mutableListOf()
    val listPoint = mutableListOf<Point>()

    fun setData(listData: List<Score>) {
        list.clear()
        list.addAll(listData)
        clearPoints()
        notifyDataSetChanged()
    }

    fun clearPoints() {
        listPoint.clear()
        repeat(list.size) {
            listPoint.add(Point())
        }
    }

    fun setPoint(point: Point, position: Int) {
        listPoint[position] = point
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        clearPoints()
        return ViewHolder(
            ItemListScoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemListScoreBinding.bind(view)
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
            if (listPoint[position].icon != null) {
                loge("ada")
                ivPoint.visible()
                Glide.with(iv.context)
                    .asBitmap()
                    .load(
                        iv.context.resources.getIdentifier(
                            listPoint[position].icon,
                            "drawable",
                            iv.context.packageName
                        )
                    )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(ivPoint)
            } else {
                ivPoint.gone()
            }
            cv.setOnClickListener { callBack.invoke(position) }
            tvName.text = data.player_name
            setImage(data.player_avatar ?: "", iv)
            tvNo.text = "${position + 1}"
            tvPoint.text = data.point.toString()
            if (position == 0) {
                ivCrown.visible()
            } else {
                ivCrown.gone()
            }
            when (position) {
                0 -> {
                    cvNo.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.first))
                }
                1 -> {
                    cvNo.setCardBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.second
                        )
                    )
                }
                2 -> {
                    cvNo.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.third))
                }
                else -> {
                    cvNo.setCardBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.primary
                        )
                    )
                }
            }
        }
    }

    private fun setImage(url: String, iv: ImageView) {
        try {
            Glide.with(iv.context)
                .asBitmap()
                .load(iv.context.resources.getIdentifier(url, "drawable", iv.context.packageName))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(iv)
        } catch (e: Exception) {
            loge(e.localizedMessage)
        }
    }
}