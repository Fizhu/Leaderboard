package com.fizhu.leaderboard.ui.leaderboard

import android.content.Intent
import android.os.Bundle
import android.widget.EdgeEffect
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fizhu.leaderboard.adapters.GameAdapter
import com.fizhu.leaderboard.databinding.ActivityMainBinding
import com.fizhu.leaderboard.ui.createnew.CreateNewActivity
import com.fizhu.leaderboard.utils.AppConstants
import com.fizhu.leaderboard.utils.ext.gone
import com.fizhu.leaderboard.utils.ext.observe
import com.fizhu.leaderboard.utils.ext.visible
import com.fizhu.leaderboard.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LeaderboardActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onInit()
    }

    private fun onInit() {
        gameAdapter = GameAdapter {

        }
        binding.fab.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CreateNewActivity::class.java
                )
            )
        }
        binding.rv.let {
            with(it) {
                layoutManager =
                    LinearLayoutManager(this@LeaderboardActivity)
                setHasFixedSize(true)
                adapter = gameAdapter
                it.edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
                    override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
                        return object : EdgeEffect(view.context) {

                            override fun onPull(deltaDistance: Float) {
                                super.onPull(deltaDistance)
                                handlePull(deltaDistance)
                            }

                            override fun onPull(deltaDistance: Float, displacement: Float) {
                                super.onPull(deltaDistance, displacement)
                                handlePull(deltaDistance)
                            }

                            private fun handlePull(deltaDistance: Float) {
                                // This is called on every touch event while the list is scrolled with a finger.
                                // We simply update the view properties without animation.
                                val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                                val rotationDelta =
                                    sign * deltaDistance * AppConstants.OVERSCROLL_ROTATION_MAGNITUDE
                                val translationYDelta =
                                    sign * view.width * deltaDistance * AppConstants.OVERSCROLL_TRANSLATION_MAGNITUDE
                                view.forEachVisibleHolder { holder: GameAdapter.ViewHolder ->
                                    holder.rotation.cancel()
                                    holder.translationY.cancel()
                                    holder.itemView.rotation += rotationDelta
                                    holder.itemView.translationY += translationYDelta
                                }
                            }

                            override fun onRelease() {
                                super.onRelease()
                                // The finger is lifted. This is when we should start the animations to bring
                                // the view property values back to their resting states.
                                view.forEachVisibleHolder { holder: GameAdapter.ViewHolder ->
                                    holder.rotation.start()
                                    holder.translationY.start()
                                }
                            }

                            override fun onAbsorb(velocity: Int) {
                                super.onAbsorb(velocity)
                                val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                                // The list has reached the edge on fling.
                                val translationVelocity =
                                    sign * velocity * AppConstants.FLING_TRANSLATION_MAGNITUDE
                                view.forEachVisibleHolder { holder: GameAdapter.ViewHolder ->
                                    holder.translationY
                                        .setStartVelocity(translationVelocity)
                                        .start()
                                }
                            }
                        }
                    }
                }

                it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        recyclerView.forEachVisibleHolder { holder: GameAdapter.ViewHolder ->
                            holder.rotation
                                // Update the velocity.
                                // The velocity is calculated by the vertical scroll offset.
                                .setStartVelocity(holder.currentVelocity - dx * AppConstants.SCROLL_ROTATION_MAGNITUDE)
                                // Start the animation. This does nothing if the animation is already running.
                                .start()
                        }
                    }
                })

            }
        }
        viewModel.getListGame()
        observer()
    }

    private fun observer() {
        observe(viewModel.listGame) {
            gameAdapter.setData(it)
        }
        observe(viewModel.isExist) {
            if (it) {
                binding.layoutNocon.lyNocon.gone()
                binding.rv.visible()
            } else {
                binding.layoutNocon.lyNocon.visible()
                binding.rv.gone()
            }
        }
    }

    inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.forEachVisibleHolder(
        action: (T) -> Unit
    ) {
        for (i in 0 until childCount) {
            action(getChildViewHolder(getChildAt(i)) as T)
        }
    }
}