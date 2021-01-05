package com.fizhu.leaderboard.viewmodels

import androidx.lifecycle.MutableLiveData
import com.fizhu.leaderboard.data.models.Player
import com.fizhu.leaderboard.data.raw.RawData
import com.fizhu.leaderboard.utils.base.BaseViewModel

/**
 * Created by fizhu on 07,July,2020
 * https://github.com/Fizhu
 */

class AddPlayerViewModel : BaseViewModel() {

    val name = MutableLiveData<String>()
    val playerName = MutableLiveData<String>()
    val avatar = MutableLiveData<String>()
    val listPlayer = MutableLiveData<List<Player>>()
    private val listPlayerTemp = mutableListOf<Player>()

    init {
        avatar.value = RawData.getAnimalList()[47]
    }

    fun getName() = playerName.value ?: ""

    fun setAvatar(avatar: String) {
        this.avatar.value = avatar
    }

    fun setGameName(name: String) {
        this.name.value = name
    }

    fun getListPlayer() = listPlayerTemp

    fun addNewPlayer() {
        listPlayerTemp.add(Player(null, playerName.value, avatar.value))
        listPlayer.postValue(listPlayerTemp)
    }

    fun removePlayer(player: Player) {
        listPlayerTemp.remove(player)
    }
}