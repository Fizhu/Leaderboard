package com.fizhu.leaderboard.data.repository

import com.fizhu.leaderboard.data.models.Game
import io.reactivex.Observable

/**
 * Created by fizhu on 22,May,2020
 * Hvyz.anbiya@gmail.com
 */
interface Repository {
    val getAllGame: Observable<List<Game>>
    fun getGameById(id: Int): Observable<Game>
    fun insertGame(game: Game)
}