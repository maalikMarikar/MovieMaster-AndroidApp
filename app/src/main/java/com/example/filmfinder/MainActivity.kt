package com.example.filmfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivity: ComponentActivity(){
    lateinit var db: AppDatabase
    lateinit var movieDao: MovieDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(this, AppDatabase::class.java, "MyDB").build()
        movieDao = db.movieDao()
        setContent{
            GUI(movieDao)
        }
    }
}

@Composable
fun GUI(movieDao: MovieDao){
    var scope = rememberCoroutineScope()
    var myMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Button(
            onClick = {

                val testMovie = Movie(
                    Title = "The Shawshank Redemption 123",
                    Year = "1994",
                    Rated = "R",
                    Released = "14 Oct 1994",
                    Runtime = "142 min",
                    Genre = "Drama",
                    Director = "Frank Darabont",
                    Writer = "Stephen King, Frank Darabont",
                    Actors = "Tim Robbins, Morgan Freeman, Bob Gunton",
                    Plot = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
                )
                scope.launch {
                    movieDao.insertMovie(testMovie)
                }
            }

        ) {
            Text("Insert Movie!")
        }

        Button(
            onClick = {
                scope.launch {
                    myMovies = movieDao.getSavedMovies()
                }
            }
        ) {
            Text("View movies!")
        }

        Button(
            onClick = {
                scope.launch {
                    movieDao.deleteById("The Shawshank Redemption 123")
                }
            }
        ) {
            Text("View movies!")
        }

        if(myMovies.isNotEmpty()){
            Text(text = myMovies.get(0).toString())
        }
    }
}

