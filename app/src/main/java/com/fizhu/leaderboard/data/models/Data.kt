package com.fizhu.leaderboard.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fizhu on 06,July,2020
 * https://github.com/Fizhu
 */

@Entity(tableName = "player_table")
data class Player(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose @SerializedName("id")
    var id: Int? = null,
    @Expose @SerializedName("name")
    var name: String? = null,
    @Expose @SerializedName("avatar")
    var avatar: String? = null
)

@Entity(tableName = "game_table")
data class Game(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose @SerializedName("id")
    var id: Int? = null,
    @Expose @SerializedName("name")
    var name: String? = null,
    @Expose @SerializedName("date")
    var date: String? = null,
    @Expose @SerializedName("status")
    var boolean: Boolean? = false,
    @Expose @SerializedName("player_count")
    var playerCount: Int? = null,
    @Expose @SerializedName("total_round")
    var totalRound: Int? = null
)

@Entity(tableName = "score_table")
data class Score(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose @SerializedName("id")
    var id: Int? = null,
    @Expose @SerializedName("player_id")
    var playerId: Int? = null,
    @Expose @SerializedName("game_id")
    var gameId: Int? = null,
    @Expose @SerializedName("point")
    var point: Int? = null,
)
