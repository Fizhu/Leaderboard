package com.fizhu.leaderboard.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.data.models.Player
import com.fizhu.leaderboard.data.models.Point
import com.fizhu.leaderboard.data.models.Score


/**
 * Created by fizhu on 06,July,2020
 * https://github.com/Fizhu
 */
@Database(
    entities = [Player::class, Game::class, Score::class, Point::class],
    version = 1, exportSchema = false
)
abstract class Db : RoomDatabase() {

    // --- DAO ---
    abstract fun appDao(): AppDao

    companion object {

        // --- SINGLETON ---
        @Volatile
        private var INSTANCE: Db? = null

        fun getInstance(context: Context): Db {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Db::class.java,
                    "leaderboard_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}