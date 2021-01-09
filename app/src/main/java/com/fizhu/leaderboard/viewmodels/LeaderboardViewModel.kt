package com.fizhu.leaderboard.viewmodels

import androidx.lifecycle.MutableLiveData
import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.data.models.Point
import com.fizhu.leaderboard.data.models.Score
import com.fizhu.leaderboard.data.repository.Repository
import com.fizhu.leaderboard.utils.base.BaseViewModel
import com.fizhu.leaderboard.utils.ext.loge
import com.fizhu.leaderboard.utils.ext.route

/**
 * Created by fizhu on 07,July,2020
 * https://github.com/Fizhu
 */

class LeaderboardViewModel(
    private val repository: Repository
) : BaseViewModel() {

    val listScore = MutableLiveData<List<Score>>()
    val listPoint = MutableLiveData<List<Point>>()
    val game = MutableLiveData<Game>()
    val totalRound = MutableLiveData<String>()

    fun setTotalRoumd(totalRound: Int) {
        this.totalRound.value = "Total Round : $totalRound"
    }

    fun setGameData(game: Game) {
        this.game.value = game
    }

    fun getListScore(id: Int) {
        compositeDisposable.route(repository.getScoreByIdGame(id),
            io = {
                if (it.isNotEmpty()) {
                    listScore.postValue(it.sortedByDescending { score -> score.point })
                }
            },
            error = {
                loge(it.localizedMessage)
            })
    }

    fun getListPoint(id: Int) {
        compositeDisposable.route(repository.getPointByIdGame(id),
            io = {
                if (it.isNotEmpty()) {
                    listPoint.postValue(it)
                }
            },
            error = {
                loge(it.localizedMessage)
            })
    }

    fun getGameById(id: Int) {
        compositeDisposable.route(repository.getGameById(id),
            io = {
                if (it.isNotEmpty()) {
                    setGameData(it[0])
                }
            },
            error = {
                loge(it.localizedMessage)
            })
    }

}