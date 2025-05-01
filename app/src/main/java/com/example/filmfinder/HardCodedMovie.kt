package com.example.filmfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch


lateinit var db: AppDatabase
lateinit var movieDao: MovieDao
class HardCodedMovie: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        db = Room.databaseBuilder(this, AppDatabase::class.java, "moviedatabase").build()
        movieDao = db.movieDao()
        super.onCreate(savedInstanceState)
        setContent {
            GUI_HardCodedMovie()
        }
    }
}

@Composable
fun GUI_HardCodedMovie(){
    val scope = rememberCoroutineScope()
    var movieDisplay by remember { mutableStateOf<Movie?>(null) }
    val shawshank = Movie(
        Title = "The Shawshank Redemption",
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

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("\uD83C\uDFAC Movie Selected: 'The Shawshank Redemption'", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 17.sp))
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    scope.launch {
                        movieDao.insertMovie(shawshank)
                    }
                }
            ) {
                Text("Add")
            }
            Spacer(Modifier.width(4.dp))
            Button(
                onClick = {
                    scope.launch {
                        movieDisplay = movieDao.getByTitle("The Shawshank Redemption")
                    }
                }
            ) {
                Text("View")
            }
            Spacer(Modifier.width(4.dp))
            Button(
                onClick = {
                    scope.launch {
                        movieDao.deleteMovie(shawshank)
                    }
                }
            ) {
                Text("Delete")
            }
            if(movieDisplay==null){
                Text("Above Movie nahh")
            }
            else{
                Text(movieDisplay!!.Plot.toString())
            }
        }
    }
}