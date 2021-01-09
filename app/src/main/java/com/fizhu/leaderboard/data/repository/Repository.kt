package com.fizhu.leaderboard.data.repository

import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.data.models.Player
import com.fizhu.leaderboard.data.models.Point
import com.fizhu.leaderboard.data.models.Score
import io.reactivex.Observable

/**
 * Created by fizhu on 22,May,2020
 * Hvyz.anbiya@gmail.com
 */
interface Repository {
    val getAllGame: Observable<List<Game>>
    val getLastestGame: Observable<List<Game>>
    fun getGameById(id: Int): Observable<List<Game>>
    fun insertGame(game: Game)
    fun insertPlayers(listPlayer: List<Player>)
    fun insertScores(listScore: List<Score>)
    fun getPlayerByIdGame(gameId: Int): Observable<List<Player>>
    fun getScoreByIdGame(gameId: Int): Observable<List<Score>>
    fun insertPoints(listPoint: List<Point>)
}