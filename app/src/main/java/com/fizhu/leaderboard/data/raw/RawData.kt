package com.fizhu.leaderboard.data.raw

/**
 * Created by fizhu on 05,January,2021
 * https://github.com/Fizhu
 */
object RawData {

    fun getAnimalList(): List<String> {
        val list = mutableListOf<String>()
        repeat(50) {
            list.add("animal${it + 1}")
        }
        return list
    }

    fun getObjectList(): List<String> {
        val list = mutableListOf<String>()
        repeat(70) {
            list.add("object${it + 1}")
        }
        return list
    }


}