package com.example.filmfinder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class WatchLater: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieDao = (application as App).movieDao
        setContent{
            DisplayAllMovies(movieDao)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DisplayAllMovies(movieDao: MovieDao){
    val vScroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    var movies by rememberSaveable { mutableStateOf(mutableListOf<Movie>()) }

    scope.launch {
        movies = movieDao.getSavedMovies().toMutableList()
    }
    Column(Modifier.fillMaxSize().background(color = Color.Gray).verticalScroll(vScroll), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        if(movies.isNotEmpty()){
            movies.forEach{m->
                Column(Modifier.padding(5.dp).border(2.dp, color = Color.Black).padding(5.dp)) {
                    Text("🎬 ${m?.Title}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Rated: ${m?.Year}", fontWeight = FontWeight.Bold)
                    Text("Rated: ${m?.Rated}", fontWeight = FontWeight.Bold)
                    Text("Released: ${m?.Released}", fontWeight = FontWeight.Bold)
                    Text("Runtime: ${m?.Runtime}", fontWeight = FontWeight.Bold)
                    Text("Genre: ${m?.Genre}", fontWeight = FontWeight.Bold)
                    Text("Director: ${m?.Director}", fontWeight = FontWeight.Bold)
                    Text("Writer: ${m?.Writer}", fontWeight = FontWeight.Bold)
                    Text("Actors: ${m?.Actors}\n", fontWeight = FontWeight.Bold)
                    Text("Plot: ${m?.Plot}", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                }
                Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Button(onClick = {
                        scope.launch {
                            movieDao.deleteMovie(m)
                            movies = movieDao.getSavedMovies().toMutableList()
                        }
                    }) { Text("Delete") }
                }
            }
        }
        else{
            Text(text = "No Movies in Database ⚠\uFE0F", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp))
        }
    }
}


