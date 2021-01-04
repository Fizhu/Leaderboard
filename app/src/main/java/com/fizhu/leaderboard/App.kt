package com.fizhu.leaderboard

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.fizhu.leaderboard.data.db.Db
import com.fizhu.leaderboard.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by fizhu on 04,January,2021
 * https://github.com/Fizhu
 */

class App : Application() {

    lateinit var db: Db

    override fun onCreate() {
        super.onCreate()
        context = this
        singleton = this

        db = Db.getInstance(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
                /**
                 * The application [Context] made static.
                 * Do **NOT** use this as the context for a view,
                 * this is mostly used to simplify calling of resources
                 * (esp. String resources) outside activities.
                 */
        var context: Context? = null
            private set

        @SuppressLint("StaticFieldLeak")
        var singleton: App? = null
            private set

        val getInstance: App?
            get() = singleton

    }

}