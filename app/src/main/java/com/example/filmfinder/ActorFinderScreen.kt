package com.example.filmfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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

class ActorFinderScreen: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieDao = (application as App).movieDao
        setContent{
            GUI_ActorFinder(movieDao)
        }
    }
}
@Composable
fun GUI_ActorFinder(movieDao: MovieDao){
    val vScroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    var actorInput by rememberSaveable { mutableStateOf("") }
    var movies by rememberSaveable { mutableStateOf(mutableListOf<Movie>()) }
    var searchClicked by rememberSaveable { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().background(Color.Gray).padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = actorInput,
                onValueChange = {actorInput = it},
                label = { Text("Actor Name")}
            )

            Spacer(Modifier.height(5.dp))
            Button(onClick = {
                scope.launch {
                    val pattern = "%${actorInput}%"
                    movies = movieDao.getByActor(pattern).toMutableList()
                    searchClicked = true
                }


            }) { Text("Search") }
        }

        Column (
            Modifier.fillMaxWidth().padding(4.dp).verticalScroll(vScroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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
                        Text("Actors: ${m?.Actors}\n\n", fontWeight = FontWeight.Bold)
                        Text("Plot: ${m?.Plot}", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
            else if(movies.isEmpty() && searchClicked){
                Text(text = "No results found ⚠\uFE0F", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp))
            }
        }


    }
}