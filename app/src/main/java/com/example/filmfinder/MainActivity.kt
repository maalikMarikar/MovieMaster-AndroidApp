package com.example.filmfinder

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


class MainActivity: ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            GUI_MainScreen()
        }
    }
}

@Composable
fun GUI_MainScreen(){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            val intent = Intent(context, HardCodedMovie::class.java)
            context.startActivity(intent)
        }) {
            Text("Add Movies to DB")
        }

        Button(onClick = {
            val intent = Intent(context, MovieFinderScreen::class.java)
            context.startActivity(intent)
        }) {
            Text("Search for Movies")
        }

        Button(onClick = {
            val intent = Intent(context, ActorFinderScreen::class.java)
            context.startActivity(intent)
        }) {
            Text("Search for Actors")
        }
    }

}

