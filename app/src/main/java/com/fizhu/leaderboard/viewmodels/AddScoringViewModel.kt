package com.fizhu.leaderboard.viewmodels

import androidx.lifecycle.MutableLiveData
import com.fizhu.leaderboard.data.models.Player
import com.fizhu.leaderboard.data.models.Point
import com.fizhu.leaderboard.data.raw.RawData
import com.fizhu.leaderboard.data.repository.Repository
import com.fizhu.leaderboard.utils.base.BaseViewModel

/**
 * Created by fizhu on 07,July,2020
 * https://github.com/Fizhu
 */

class AddScoringViewModel(
    private val repository: Repository
) : BaseViewModel() {

    val name = MutableLiveData<String>()
    val listPlayer = MutableLiveData<List<Player>>()

    val icon = MutableLiveData<String>()
    val pointString = MutableLiveData<String>()
    private var point = 0

    val listPoint = MutableLiveData<List<Point>>()
    private val listPointTemp = mutableListOf<Point>()

    init {
        icon.value = RawData.getObjectList()[50]
        pointString.value = point.toString()
    }

    fun setPoint(p: String) {
        point = p.toInt()
    }

    fun setIcon(icon: String) {
        this.icon.value = icon
    }

    fun setGameName(name: String) {
        this.name.value = name
    }

    fun setListPlayer(listPlayer: List<Player>) {
        this.listPlayer.value = listPlayer
    }

    fun incrementPoint() {
        point += 1
        pointString.value = point.toString()
    }

    fun decrementPoint() {
        point -= 1
        pointString.value = point.toString()
    }

    fun addNewPoint() {
        listPointTemp.add(Point(pointString.value?.toInt(), icon.value))
        listPoint.postValue(listPointTemp)
    }

    fun removePoint(point: Point) {
        listPointTemp.remove(point)
    }
}