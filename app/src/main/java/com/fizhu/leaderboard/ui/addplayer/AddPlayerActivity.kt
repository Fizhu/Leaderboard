package com.fizhu.leaderboard.ui.addplayer

import android.content.Intent
import android.os.Bundle
import android.widget.EdgeEffect
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fizhu.leaderboard.R
import com.fizhu.leaderboard.adapters.PlayerAdapter
import com.fizhu.leaderboard.data.raw.RawData
import com.fizhu.leaderboard.databinding.ActivityAddPlayerBinding
import com.fizhu.leaderboard.ui.addscoring.AddScoringActivity
import com.fizhu.leaderboard.ui.dialog.AvatarDialog
import com.fizhu.leaderboard.utils.AppConstants
import com.fizhu.leaderboard.utils.ext.observe
import com.fizhu.leaderboard.utils.ext.toast
import com.fizhu.leaderboard.viewmodels.AddPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AddPlayerActivity : AppCompatActivity() {

    private val viewModel by viewModel<AddPlayerViewModel>()
    private lateinit var binding: ActivityAddPlayerBinding
    private lateinit var playerAdapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_player)
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this
        onInit()
    }

    private fun onInit() {
        intent.getStringExtra("data")?.let {
            viewModel.setGameName(it)
        }
        playerAdapter = PlayerAdapter {
            viewModel.removePlayer(it)
        }
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            iv.setOnClickListener { showDialog() }
            tilName.setEndIconOnClickListener {
                if (this@AddPlayerActivity.viewModel.getName() != "") {
                    tilName.isHelperTextEnabled = false
                    this@AddPlayerActivity.viewModel.addNewPlayer()
                } else {
                    tilName.isHelperTextEnabled = true
                    tilName.helperText = "Enter player name"
                }
            }
            btnNext.setOnClickListener {
                if (this@AddPlayerActivity.viewModel.getListPlayer().size > 1) {
                    val i = Intent(this@AddPlayerActivity, AddScoringActivity::class.java)
                    i.putExtra("data", this@AddPlayerActivity.viewModel.name.value)
                    val bundle = Bundle()
                    bundle.putParcelableArrayList(
                        "list",
                        ArrayList(this@AddPlayerActivity.viewModel.getListPlayer())
                    )
                    i.putExtra("data2", bundle)
                    startActivity(i)
                } else {
                    toast("Minimum players is 2")
                }
            }
        }
        binding.rv.let {
            with(it) {
                layoutManager =
                    LinearLayoutManager(this@AddPlayerActivity)
                setHasFixedSize(true)
                adapter = playerAdapter
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
                                view.forEachVisibleHolder { holder: PlayerAdapter.ViewHolder ->
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
                                view.forEachVisibleHolder { holder: PlayerAdapter.ViewHolder ->
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
                                view.forEachVisibleHolder { holder: PlayerAdapter.ViewHolder ->
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
                        recyclerView.forEachVisibleHolder { holder: PlayerAdapter.ViewHolder ->
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
        observer()
    }

    private fun observer() {
        observe(viewModel.listPlayer) {
            playerAdapter.setData(it)
        }
    }

    inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.forEachVisibleHolder(
        action: (T) -> Unit
    ) {
        for (i in 0 until childCount) {
            action(getChildViewHolder(getChildAt(i)) as T)
        }
    }

    private fun showDialog() {
        if (!this.isFinishing) {
            val dialog = AvatarDialog(this, callBack = {
                setImage(it, binding.iv)
                viewModel.setAvatar(it)
            })
            dialog.setList(RawData.getAnimalList())
            dialog.setCancelable(true)
            dialog.show()
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