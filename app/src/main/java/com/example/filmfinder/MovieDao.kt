package com.example.filmfinder

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    suspend fun getSavedMovies(): List<Movie>

    @Insert
    suspend fun insertMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("DELETE FROM movie WHERE Title LIKE :title")
    suspend fun deleteById(title: String)

    @Query("SELECT * FROM movie WHERE Title LIKE :title")
    suspend fun getByTitle(title: String): Movie

    @Query("SELECT * FROM movie WHERE LOWER(Actors) LIKE LOWER(:actorName)")
    suspend fun getByActor(actorName: String): List<Movie>

}