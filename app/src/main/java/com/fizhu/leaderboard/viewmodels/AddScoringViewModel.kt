package com.fizhu.leaderboard.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.data.models.Player
import com.fizhu.leaderboard.data.models.Point
import com.fizhu.leaderboard.data.models.Score
import com.fizhu.leaderboard.data.raw.RawData
import com.fizhu.leaderboard.data.repository.Repository
import com.fizhu.leaderboard.utils.DateTimeHelper
import com.fizhu.leaderboard.utils.SingleLiveEvent
import com.fizhu.leaderboard.utils.base.BaseViewModel
import com.fizhu.leaderboard.utils.ext.doBack
import com.fizhu.leaderboard.utils.ext.loge
import com.fizhu.leaderboard.utils.ext.logi
import com.fizhu.leaderboard.utils.ext.route
import java.util.*

/**
 * Created by fizhu on 07,July,2020
 * https://github.com/Fizhu
 */

class AddScoringViewModel(
    private val repository: Repository
) : BaseViewModel() {

    val name = MutableLiveData<String>()
    val listPlayer = MutableLiveData<List<Player>>()
    val game = MutableLiveData<Game>()

    val icon = MutableLiveData<String>()
    val pointString = MutableLiveData<String>()
    private var point = 0

    val listPoint = MutableLiveData<List<Point>>()
    private val listPointTemp = mutableListOf<Point>()

    private val _isDone = SingleLiveEvent<Boolean>()
    val isDone: LiveData<Boolean>
        get() = _isDone

    init {
        icon.value = RawData.getObjectList()[50]
        pointString.value = point.toString()
    }

    fun setPoint(p: String) {
        point = p.toInt()
    }

    fun setIcon(icon: String) {
        this.icon.value = icon
    }

    fun setGameName(name: String) {
        this.name.value = name
    }

    fun setListPlayer(listPlayer: List<Player>) {
        this.listPlayer.value = listPlayer
    }

    private fun getListPlayer(): List<Player> = listPlayer.value ?: emptyList()

    private fun getListPoint(): List<Point> = listPoint.value ?: emptyList()

    fun incrementPoint() {
        point += 1
        pointString.value = point.toString()
    }

    fun decrementPoint() {
        point -= 1
        pointString.value = point.toString()
    }

    fun addNewPoint() {
        listPointTemp.add(Point(null, null, pointString.value?.toInt(), icon.value))
        listPoint.postValue(listPointTemp)
    }

    fun removePoint(point: Point) {
        listPointTemp.remove(point)
    }

    fun addGameToDb() {
        val game = Game(
            id = null,
            name = name.value,
            date = DateTimeHelper.format(
                Date(),
                DateTimeHelper.FORMAT_DATE_DMY_LONG_MONTH_NO_SEPARATOR
            ),
            status = false,
            playerCount = listPlayer.value?.size,
            totalRound = 0
        )
        doBack(
            action = {
                repository.insertGame(game)
            },
            success = {
                logi("success insert data to db")
                getLastestGame()
            },
            error = { loge("failed insert data to db") }
        )
    }

    private fun getLastestGame() {
        compositeDisposable.route(repository.getLastestGame,
            io = {
                if (it.isNotEmpty()) {
                    val game = it[0]
                    this.game.postValue(it[0])
                    insertPlayersToDb(game)
                    insertPointsToDb(game)
                } else {
                    loge("failed get lastest game data")
                }
            },
            error = {
                loge(it.localizedMessage)
            })
    }

    private fun getPlayerByGameId(game: Game) {
        compositeDisposable.route(repository.getPlayerByIdGame(game.id ?: 0),
            io = {
                if (it.isNotEmpty()) {
                    insertAllScores(game, it)

                } else {
                    loge("failed get lastest game data")
                }
            },
            error = {
                loge(it.localizedMessage)
            })
    }

    private fun insertAllScores(game: Game, listPlayer: List<Player>) {
        val list = mutableListOf<Score>()
        listPlayer.forEach {
            list.add(
                Score(
                    id = null,
                    player_id = it.id,
                    player_name = it.name,
                    player_avatar = it.avatar,
                    gameId = game.id,
                    point = 0
                )
            )
        }
        doBack(
            action = {
                repository.insertScores(list)
            },
            success = {
                logi("success insert data to db")
                _isDone.postValue(true)
            },
            error = { loge("failed insert data to db") }
        )
    }

    private fun insertPlayersToDb(game: Game) {
        val list = mutableListOf<Player>()
        getListPlayer().forEach {
            list.add(
                Player(
                    id = it.id,
                    gameId = game.id,
                    name = it.name,
                    avatar = it.avatar
                )
            )
        }
        doBack(
            action = {
                repository.insertPlayers(list)
            },
            success = {
                logi("success insert data to db")
                getPlayerByGameId(game)
            },
            error = { loge("failed insert data to db") }
        )
    }

    private fun insertPointsToDb(game: Game) {
        val list = mutableListOf<Point>()
        getListPoint().forEach {
            list.add(
                Point(
                    id = null,
                    gameId = game.id,
                    points = it.points,
                    icon = it.icon
                )
            )
        }
        doBack(
            action = {
                repository.insertPoints(list)
            },
            success = {
                logi("success insert data to db")
            },
            error = { loge("failed insert data to db") }
        )
    }

}