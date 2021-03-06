package com.fizhu.leaderboard.data.repository

import com.fizhu.leaderboard.data.db.LocalDataSource
import com.fizhu.leaderboard.data.models.*
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

    override fun getPlayerByIdGame(gameId: Int): Observable<List<Player>> =
        db.getPlayerByIdGame(gameId)

    override fun getScoreByIdGame(gameId: Int): Observable<List<Score>> =
        db.getScoreByIdGame(gameId)

    override fun insertPoints(listPoint: List<Point>) =
        db.insertPoints(listPoint)

    override fun getPointByIdGame(gameId: Int): Observable<List<Point>> =
        db.getPointByIdGame(gameId)

    override fun updateScore(listScore: List<Score>) = db.updateScore(listScore)

    override fun updateGame(game: Game) = db.updateGame(game)

    override fun insertScoreLog(listPoint: List<ScoreLog>) = db.insertScoreLog(listPoint)

}