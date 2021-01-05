package com.fizhu.leaderboard.viewmodels

import androidx.lifecycle.MutableLiveData
import com.fizhu.leaderboard.data.models.Game
import com.fizhu.leaderboard.data.repository.Repository
import com.fizhu.leaderboard.utils.base.BaseViewModel
import com.fizhu.leaderboard.utils.ext.loge
import com.fizhu.leaderboard.utils.ext.route

/**
 * Created by fizhu on 07,July,2020
 * https://github.com/Fizhu
 */

class LeaderboardViewModel(
    private val repository: Repository
) : BaseViewModel() {

    val listGame = MutableLiveData<List<Game>>()

    fun getListGame() {
        compositeDisposable.route(repository.getAllGame,
            io = {
                if (it.isNotEmpty()) {
                    listGame.postValue(it)
                } else {
                }
            },
            error = {
                loge(it.localizedMessage)
            })
    }

}