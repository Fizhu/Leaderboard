package com.fizhu.leaderboard.ui.splash

import android.os.Bundle
import com.fizhu.leaderboard.ui.main.MainActivity
import com.fizhu.leaderboard.databinding.ActivitySplashBinding
import com.fizhu.leaderboard.utils.AppConstants
import com.fizhu.leaderboard.utils.base.BaseActivity
import com.fizhu.leaderboard.utils.ext.delay
import com.fizhu.leaderboard.utils.ext.startActivityWithFade
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by fizhu on 04,January,2021
 * https://github.com/Fizhu
 */
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSplash()
    }

    private fun initSplash() {
        compositeDisposable.delay(AppConstants.SPLASH_TIME_MILLISECOND) {
            startActivityWithFade(this, MainActivity::class.java)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}