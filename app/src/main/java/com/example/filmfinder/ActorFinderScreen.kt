package com.example.filmfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
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
    val scope = rememberCoroutineScope()
    var actorInput by rememberSaveable { mutableStateOf("") }
    var movies by rememberSaveable { mutableStateOf(mutableListOf<Movie>()) }
    Column(
        Modifier.fillMaxSize().background(Color.Gray),
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
                label = { Text("Enter actor's name")}
            )

            Spacer(Modifier.height(5.dp))
            Button(onClick = {
                scope.launch {
                    val pattern = "%${actorInput}%"
                    movies = movieDao.getByActor(pattern).toMutableList()
                }


            }) { Text("Search") }
        }

        movies.forEach{m->
            Text(m.Title)
        }


    }
}