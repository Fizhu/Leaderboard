package com.fizhu.leaderboard.viewmodels

import androidx.lifecycle.MutableLiveData
import com.fizhu.leaderboard.data.models.Player
import com.fizhu.leaderboard.utils.base.BaseViewModel

/**
 * Created by fizhu on 07,July,2020
 * https://github.com/Fizhu
 */

class CreateNewViewModel : BaseViewModel() {

    val name = MutableLiveData<String>()

    fun getName() = name.value?:""
}