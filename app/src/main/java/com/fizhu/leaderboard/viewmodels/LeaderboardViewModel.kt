package com.fizhu.leaderboard.viewmodels

import androidx.lifecycle.MutableLiveData
import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.data.models.Point
import com.fizhu.leaderboard.data.models.Score
import com.fizhu.leaderboard.data.repository.Repository
import com.fizhu.leaderboard.utils.base.BaseViewModel
import com.fizhu.leaderboard.utils.ext.doBack
import com.fizhu.leaderboard.utils.ext.loge
import com.fizhu.leaderboard.utils.ext.logi
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
    private var totalRoundTemp = 0

    fun setTotalRoumd(totalRound: Int) {
        totalRoundTemp = totalRound
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

    private fun getGameById(id: Int) {
        compositeDisposable.route(repository.getGameById(id),
            main = {
                if (it.isNotEmpty()) {
                    setGameData(it[0])
                }
            },
            error = {
                loge(it.localizedMessage)
            })
    }

    private fun getListScore() = listScore.value ?: emptyList()

    fun updateScore(listPoint: List<Point>) {
        val list = mutableListOf<Score>()
        getListScore().forEachIndexed { index, score ->
            listPoint[index].points?.let { p ->
                list.add(
                    Score(
                        id = score.id,
                        player_name = score.player_name,
                        player_avatar = score.player_avatar,
                        gameId = score.gameId,
                        point = score.point?.plus(p)
                    )
                )
            }
        }
        doBack(
            action = {
                repository.updateScore(list)
            },
            success = {
                logi("success insert data to db")
                updateGameRound()
            },
            error = { loge("failed insert data to db") }
        )
    }

    private fun updateGameRound() {
        val g = Game(
            id = game.value?.id,
            name = game.value?.name,
            date = game.value?.date,
            status = game.value?.status,
            playerCount = game.value?.playerCount,
            totalRound = totalRoundTemp.plus(1)
        )
        doBack(
            action = {
                repository.updateGame(g)
            },
            success = {
                logi("success insert data to db")
                getGameById(game.value?.id ?: 0)
            },
            error = { loge("failed insert data to db") }
        )
    }

}