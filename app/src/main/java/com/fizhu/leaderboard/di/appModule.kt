package com.fizhu.leaderboard.di

import androidx.room.Room
import com.fizhu.leaderboard.data.db.Db
import com.fizhu.leaderboard.data.db.LocalDataSource
import com.fizhu.leaderboard.data.repository.AppRepository
import com.fizhu.leaderboard.data.repository.Repository
import com.fizhu.leaderboard.viewmodels.AddPlayerViewModel
import com.fizhu.leaderboard.viewmodels.AddScoringViewModel
import com.fizhu.leaderboard.viewmodels.CreateNewViewModel
import com.fizhu.leaderboard.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by fizhu on 23,May,2020
 * Hvyz.anbiya@gmail.com
 */

val repositoryModule = module {
    single<Repository> { AppRepository(get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), Db::class.java, "leaderboard_db")
            .fallbackToDestructiveMigration().build()
    }
    single { get<Db>().appDao() }
}

val dataSourceModule = module {
    single { LocalDataSource(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { CreateNewViewModel() }
    viewModel { AddPlayerViewModel() }
    viewModel { AddScoringViewModel(get()) }
}

val appModule =
    listOf(databaseModule, dataSourceModule, repositoryModule, viewModelModule)