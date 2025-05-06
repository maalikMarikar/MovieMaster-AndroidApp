/*
Name ; Abdul Maalik Naina Marikar
IIT Id: 20231142
UOW Id: W 2051903
Demo Video Link: https://drive.google.com/file/d/1g5W75kW3aDgpBrXfJ6dAj5gSBKRhF3Ur/view?usp=drive_link
*/

package com.example.filmfinder

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room


class MainActivity: ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        val list = intent.getStringArrayListExtra("data") ?: arrayListOf()
        super.onCreate(savedInstanceState)
        setContent{
            GUI_MainScreen(list)
        }
    }
}

@Composable
fun GUI_MainScreen(list: MutableList<String>){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var movies by rememberSaveable { mutableStateOf(list.toMutableList()) }
    movies = list
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Button(onClick = {
                    val intent = Intent(context, HardCodedMovie::class.java)
                    context.startActivity(intent)
                }, shape = CutCornerShape(12.dp)
                ) {
                    Text("Add Movies to DB")
                }
                Spacer(Modifier.width(5.dp))
                Button(onClick = {
                    val intent = Intent(context, MovieFinderScreen::class.java)
                    context.startActivity(intent)
                }, shape = CutCornerShape(12.dp)
                    ) {
                    Text("Search for Movies")
                }
            }

            Button(onClick = {
                val intent = Intent(context, WatchLater::class.java)
                context.startActivity(intent)
            }, shape = CutCornerShape(12.dp)
            ) {
                Text("View Watch-Later")
            }
            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Button(onClick = {
                    val intent = Intent(context, ActorFinderScreen::class.java)
                    context.startActivity(intent)
                }, shape = CutCornerShape(12.dp)
                ) {
                    Text("Search for Actors")
                }
                Spacer(Modifier.width(5.dp))
                Button(onClick = {
                    val intent = Intent(context, PartialTitleFinder::class.java)
                    intent.putExtra("data", list.toTypedArray())
                    context.startActivity(intent)
                }, shape = CutCornerShape(12.dp)
                ) {
                    Text("Partial Title Search")
                }
            }
        }
    }

}

