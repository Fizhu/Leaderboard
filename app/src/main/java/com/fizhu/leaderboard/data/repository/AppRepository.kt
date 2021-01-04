package com.fizhu.leaderboard.data.repository

import com.fizhu.leaderboard.data.db.LocalDataSource
import com.fizhu.leaderboard.data.models.Game
import io.reactivex.Observable

/**
 * Created by fizhu on 22,May,2020
 * Hvyz.anbiya@gmail.com
 */
open class AppRepository constructor(
    private val db: LocalDataSource
) : Repository {
    override val getAllGame: Observable<List<Game>>
        get() = db.getAllGame

    override fun getGameById(id: Int): Observable<Game> = db.getGameById(id)

    override fun insertGame(game: Game) = db.insertGame(game)


}