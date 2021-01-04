package com.fizhu.leaderboard.data.db

import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.utils.ext.doBack
import com.fizhu.leaderboard.utils.ext.loge
import com.fizhu.leaderboard.utils.ext.logi

/**
 * Created by fizhu on 07,July,2020
 * https://github.com/Fizhu
 */
open class LocalDataSource constructor(
    private val appDao: AppDao
) {

    val getAllGame = appDao.getAllGame

    fun insertGame(game: Game) {
        doBack(
            action = {
                appDao.insertGame(game)
            },
            success = { logi("success insert user to db") },
            error = { loge("failed insert user to db") }
        )
    }

    fun getGameById(id: Int) = appDao.getGameById(id)

}