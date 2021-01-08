package com.fizhu.leaderboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.databinding.ItemListGameBinding

/**
 * Created by fizhu on 15,July,2020
 * https://github.com/Fizhu
 */

class GameAdapter(
    private val callBack: (game: Game) -> Unit
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private val list: MutableList<Game> = mutableListOf()

    fun setData(listBike: List<Game>) {
        list.clear()
        list.addAll(listBike)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemListGameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemListGameBinding.bind(view)
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
            tvDate.text = data.date
            tvPlayerCount.text = data.playerCount.toString()
            tvTitle.text = data.name
            root.setOnClickListener { callBack.invoke(data) }
        }
    }

}