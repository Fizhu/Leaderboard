package com.fizhu.leaderboard.ui.leaderboard

import android.content.Intent
import android.os.Bundle
import android.widget.EdgeEffect
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fizhu.leaderboard.R
import com.fizhu.leaderboard.adapters.ScoreAdapter
import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.databinding.ActivityLeaderboardBinding
import com.fizhu.leaderboard.ui.dialog.PointDialog
import com.fizhu.leaderboard.ui.dialog.RoundDialog
import com.fizhu.leaderboard.ui.main.MainActivity
import com.fizhu.leaderboard.utils.AppConstants
import com.fizhu.leaderboard.utils.ext.observe
import com.fizhu.leaderboard.viewmodels.LeaderboardViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import org.koin.androidx.viewmodel.ext.android.viewModel

class LeaderboardActivity : AppCompatActivity() {
    private val viewModel by viewModel<LeaderboardViewModel>()
    private lateinit var binding: ActivityLeaderboardBinding
    private lateinit var scoreAdapter: ScoreAdapter
    private var isMain = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_leaderboard)
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this
        onInit()
    }

    private fun onInit() {
        intent.getParcelableExtra<Game>("data")?.let {
            viewModel.setGameData(it)
            viewModel.getListPoint(it.id ?: 0)
        }
        intent.getBooleanExtra("isMain", false).let {
            isMain = it
        }
        binding.toolbar.setNavigationOnClickListener {
            if (isMain) {
                finish()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        binding.fabNextRound.setOnClickListener {
            showDialogRound()
        }
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.end) {
                showDialogEnd()
                true
            } else {
                false
            }
        }
        scoreAdapter = ScoreAdapter { showDialogPoint(it) }
        scoreAdapter.clearPoints()
        binding.rv.let {
            with(it) {
                layoutManager =
                    LinearLayoutManager(this@LeaderboardActivity)
                setHasFixedSize(true)
                adapter = scoreAdapter
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
                                view.forEachVisibleHolder { holder: ScoreAdapter.ViewHolder ->
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
                                view.forEachVisibleHolder { holder: ScoreAdapter.ViewHolder ->
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
                                view.forEachVisibleHolder { holder: ScoreAdapter.ViewHolder ->
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
                        recyclerView.forEachVisibleHolder { holder: ScoreAdapter.ViewHolder ->
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
        setChart()
        observer()
    }

    private fun observer() {
        observe(viewModel.game) {
            viewModel.getListScore(it.id ?: 0)
            viewModel.setTotalRoumd(it.totalRound ?: 0)
        }
        observe(viewModel.listScore) {
            scoreAdapter.setData(it)
        }
        observe(viewModel.chartDataSets) {
            val data = LineData(it)
            binding.chart.data = data
            binding.chart.invalidate()
        }
    }

    inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.forEachVisibleHolder(
        action: (T) -> Unit
    ) {
        for (i in 0 until childCount) {
            action(getChildViewHolder(getChildAt(i)) as T)
        }
    }

    override fun onBackPressed() {
        if (isMain) {
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun showDialogPoint(position: Int) {
        if (!this.isFinishing) {
            val dialog = PointDialog(this, callBack = {
                scoreAdapter.setPoint(it, position)
            })
            dialog.setList(viewModel.listPoint.value ?: emptyList())
            dialog.setCancelable(true)
            dialog.show()
        }
    }

    private fun showDialogRound() {
        if (!this.isFinishing) {
            val dialog = RoundDialog(this, callBack = {
                viewModel.updateScore(scoreAdapter.listPoint)
            })
            dialog.setCancelable(true)
            dialog.show()
        }
    }

    private fun showDialogEnd() {
        if (!this.isFinishing) {
            val dialog = RoundDialog(this, callBack = {

            })
            dialog.setTitle("End the game")
            dialog.setDesc("Are you sure want to end this game?")
            dialog.setCancelable(true)
            dialog.show()
        }
    }

    private fun setChart() {
        with(binding.chart) {
            setDrawGridBackground(false)
            description.isEnabled = false
            setDrawBorders(false)
            axisLeft.isEnabled = true
            axisLeft.textColor =
                ContextCompat.getColor(this@LeaderboardActivity, R.color.md_white_1000)
            axisRight.isEnabled = false
            xAxis.setDrawAxisLine(true)
            xAxis.setDrawGridLines(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.textColor =
                ContextCompat.getColor(this@LeaderboardActivity, R.color.md_white_1000)
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(false)

            val l: Legend = legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.textColor = ContextCompat.getColor(this@LeaderboardActivity, R.color.md_white_1000)
            l.setDrawInside(false)
        }
    }

}