package com.fizhu.leaderboard.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.data.repository.Repository
import com.fizhu.leaderboard.utils.SingleLiveEvent
import com.fizhu.leaderboard.utils.base.BaseViewModel
import com.fizhu.leaderboard.utils.ext.loge
import com.fizhu.leaderboard.utils.ext.route

/**
 * Created by fizhu on 07,July,2020
 * https://github.com/Fizhu
 */

class MainViewModel(
    private val repository: Repository
) : BaseViewModel() {

    val listGame = MutableLiveData<List<Game>>()
    private val _isExist = SingleLiveEvent<Boolean>()
    val isExist: LiveData<Boolean>
        get() = _isExist

    fun getListGame() {
        _isExist.postValue(false)
        compositeDisposable.route(repository.getAllGame,
            io = {
                if (it.isNotEmpty()) {
                    _isExist.postValue(true)
                    listGame.postValue(it.filter { game -> game.status == false }.sortedByDescending { game -> game.date })
                } else {
                    _isExist.postValue(false)
                }
            },
            error = {
                _isExist.postValue(false)
                loge(it.localizedMessage)
            })
    }

}