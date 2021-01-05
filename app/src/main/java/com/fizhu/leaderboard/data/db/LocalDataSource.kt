package com.fizhu.leaderboard.data.db

import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.data.models.Player
import com.fizhu.leaderboard.data.models.Score
import com.fizhu.leaderboard.utils.ext.doBack
import com.fizhu.leaderboard.utils.ext.loge
import com.fizhu.leaderboard.utils.ext.logi
import io.reactivex.Observable

/**
 * Created by fizhu on 07,July,2020
 * https://github.com/Fizhu
 */
open class LocalDataSource constructor(
    private val appDao: AppDao
) {

    val getAllGame = appDao.getAllGame

    val getLastestGame = appDao.getLastestGame

    fun insertGame(game: Game) = appDao.insertGame(game)

    fun getGameById(id: Int) = appDao.getGameById(id)

    fun insertPlayers(listPlayer: List<Player>) = appDao.insertPlayers(listPlayer)

    fun insertScores(listScore: List<Score>) = appDao.insertScores(listScore)

}