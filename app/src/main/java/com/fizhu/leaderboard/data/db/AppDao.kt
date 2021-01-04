package com.fizhu.leaderboard.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fizhu.leaderboard.data.models.Game
import io.reactivex.Observable

/**
 * Created by fizhu on 04,January,2021
 * https://github.com/Fizhu
 */

@Dao
interface AppDao {

    @get:Query("SELECT * FROM game_table")
    val getAllGame: Observable<List<Game>>

    @Query("SELECT * FROM game_table WHERE id = :id LIMIT 1")
    fun getGameById(id: Int): Observable<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: Game)

}