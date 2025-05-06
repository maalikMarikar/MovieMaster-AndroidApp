package com.example.filmfinder

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MovieFinderScreen: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieDao = (application as App).movieDao
        setContent{
            GUI_MovieFinder(movieDao)
        }
    }
}

@Composable
fun GUI_MovieFinder(movieDao: MovieDao){
    var testM by rememberSaveable { mutableStateOf("") }
    var enableBtn by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var movieInput by rememberSaveable { mutableStateOf("") }
    var movieOutput by rememberSaveable { mutableStateOf<Movie?>(null) }
    var searchClicked by rememberSaveable { mutableStateOf(false) }
    val vScroll = rememberScrollState()
    Column(
        Modifier.fillMaxSize().verticalScroll(vScroll).background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = movieInput,
                onValueChange = {movieInput = it},
                label = { Text("Movie Name")}
            )

            Row (
                Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    onClick = {
                        scope.launch {
                            movieOutput = findMovie(movieInput)
                            searchClicked = true
                        }

                    }
                ) {
                    Text("Search")
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    enabled = enableBtn,
                    onClick = {
                        scope.launch {
                            var checkIfInserted = movieDao.getByTitle(movieOutput!!.Title)
                            if(checkIfInserted!=null){
                                testM = "Movie already inserted ⚠\uFE0F"
                            }
                            else{
                            movieDao.insertMovie(movieOutput!!)
                            testM = "Movie added :)"
                            }
                        }
                    }
                ) {
                    Text("Add to Watch Later")
                }
            }
        }
        Column (
            modifier = Modifier
                .then(
                    if(searchClicked && movieOutput!=null){
                        Modifier.padding(12.dp).border(2.dp, Color.Black, RoundedCornerShape(8.dp)).padding(12.dp)
                    }
                    else{
                        Modifier.padding(2.dp)
                    }
            )
        ){
            if(movieOutput!=null){
                enableBtn = true
            }
            else{
                enableBtn = false
            }

            if(movieOutput!=null){
                val movieInfo = listOf(
                    "Year" to movieOutput!!.Year.toString(),
                    "Rated" to movieOutput!!.Rated.toString(),
                    "Release date" to movieOutput!!.Released.toString(),
                    "Runtime" to movieOutput!!.Runtime.toString(),
                    "Genre" to movieOutput!!.Genre.toString(),
                    "Director" to movieOutput!!.Director.toString(),
                    "Writer(s)" to movieOutput!!.Writer.toString(),
                    "Actor(s)" to movieOutput!!.Actors.toString()
                )
                Text(text = "\uD83C\uDFAC Title: ${movieOutput!!.Title.toString()}", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                Spacer(Modifier.height(5.dp))
                movieInfo.forEach { (label, value) ->
                    Row {
                        Text(text = "$label: ", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                        Text(text = value, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                    }
                    Spacer(Modifier.height(5.dp))
                }
                Spacer(Modifier.height(10.dp))
                Text(text = "Plot: ${movieOutput!!.Plot.toString()}", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
            }
            else if(movieOutput==null && searchClicked){
                testM = ""
                Text(text = "No results found ⚠\uFE0F", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp))
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(text = testM, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
    }

}

suspend fun findMovie(movieName: String): Movie?{
    val url_String = "https://www.omdbapi.com/?t=" + movieName + "&apikey=76ce7c20"
    val url = URL(url_String)
    val connect: HttpURLConnection = url.openConnection() as HttpURLConnection

    var stb = StringBuilder()

    withContext(Dispatchers.IO){
        var bf = BufferedReader(InputStreamReader(connect.inputStream))
        var line: String? = bf.readLine()
        while(line!=null){
            stb.append(line)
            line = bf.readLine()
        }
    }

    val movie = parseJSON(stb)
    return movie

}

fun parseJSON(stb: StringBuilder): Movie? {
    try{
        val json = JSONObject(stb.toString())

        if(json.getString("Response")=="False"){
            return null
        }

        val title = json.getString("Title")
        val year = json.getString("Year")
        val rated = json.getString("Rated")
        val released = json.getString("Released")
        val runtime = json.getString("Runtime")
        val genre = json.getString("Genre")
        val director = json.getString("Director")
        val writer = json.getString("Writer")
        val actors = json.getString("Actors")
        val plot = json.getString("Plot")


        return Movie(title, year, rated, released, runtime, genre, director, writer, actors, plot)
    }
    catch (e: Exception){
        return null
    }
}
