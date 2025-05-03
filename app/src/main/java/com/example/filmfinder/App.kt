package com.example.filmfinder

import android.app.Application
import androidx.room.Room

class App : Application() {
    lateinit var database: AppDatabase
    lateinit var movieDao: MovieDao

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "moviedatabase").fallbackToDestructiveMigration().build()
        movieDao = database.movieDao()
    }
}
