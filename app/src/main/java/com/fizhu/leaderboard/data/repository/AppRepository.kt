package com.fizhu.leaderboard.data.repository

import com.fizhu.leaderboard.data.db.LocalDataSource
import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.data.models.Player
import com.fizhu.leaderboard.data.models.Score
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

    override val getLastestGame: Observable<List<Game>>
        get() = db.getLastestGame

    override fun getGameById(id: Int): Observable<List<Game>> = db.getGameById(id)

    override fun insertGame(game: Game) = db.insertGame(game)

    override fun insertPlayers(listPlayer: List<Player>) = db.insertPlayers(listPlayer)

    override fun insertScores(listScore: List<Score>) = db.insertScores(listScore)


}