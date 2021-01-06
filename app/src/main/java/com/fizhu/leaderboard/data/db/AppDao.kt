package com.fizhu.leaderboard.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.data.models.Player
import com.fizhu.leaderboard.data.models.Score
import io.reactivex.Observable

/**
 * Created by fizhu on 04,January,2021
 * https://github.com/Fizhu
 */

@Dao
interface AppDao {

    @get:Query("SELECT * FROM game_table")
    val getAllGame: Observable<List<Game>>

    @get:Query("SELECT * FROM game_table ORDER BY id DESC LIMIT 1")
    val getLastestGame: Observable<List<Game>>

    @Query("SELECT * FROM game_table WHERE id = :id LIMIT 1")
    fun getGameById(id: Int): Observable<List<Game>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: Game)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayers(listPlayer: List<Player>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScores(listScore: List<Score>)

    @Query("SELECT * FROM player_table WHERE gameId = :gameId")
    fun getPlayerByIdGame(gameId: Int): Observable<List<Player>>

}