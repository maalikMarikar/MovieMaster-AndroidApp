package com.example.filmfinder

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey
    var Title: String,
    var Year: String?,
    var Rated: String?,
    var Released: String?,
    var Runtime: String?,
    var Genre: String?,
    var Director: String?,
    var Writer: String?,
    var Actors: String?,
    var Plot: String?
)