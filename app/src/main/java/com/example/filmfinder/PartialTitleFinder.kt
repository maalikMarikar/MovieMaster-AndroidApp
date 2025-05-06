package com.example.filmfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PartialTitleFinder: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{
            GUI()
        }
    }
}

@Composable
fun GUI(){
    var input by rememberSaveable { mutableStateOf("") }
    val vScroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    var movies by rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    var searchClicked by rememberSaveable { mutableStateOf(false) }


        Column(
            Modifier.fillMaxWidth().fillMaxHeight().background(color = Color.Gray),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = input,
                onValueChange = {input = it},
                label = {Text("Keyword")}
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                scope.launch {
                    movies = getMovies(input)
                    searchClicked = true
                }
            }) {
                Text("Search")
            }



            Column(Modifier.verticalScroll(vScroll)){
                if(movies.isNotEmpty()){
                    searchClicked = false
                    movies.forEach{m->
                        Row(Modifier.padding(5.dp).fillMaxWidth()){
                            Text(text = m, style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 15.sp))
                        }
                    }
                }
                else if(movies.isEmpty() && searchClicked){
                    Text(text = "No results found ⚠\uFE0F", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp))
                }
            }
        }
}

suspend fun getMovies(keyWord: String): MutableList<String>{
    val urlString = "https://www.omdbapi.com/?s=" + keyWord + "&apikey=76ce7c20"
    val url = URL(urlString)
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

    val movies = parseJson(stb)
    return movies
}


fun parseJson(stb: StringBuilder): MutableList<String> {
    val movieList = mutableListOf<String>()
    val jsonObject = JSONObject(stb.toString())

    if (jsonObject.getString("Response") == "True") {
        val searchArray = jsonObject.getJSONArray("Search")
        for (i in 0 until searchArray.length()) {
            val obj = searchArray.getJSONObject(i)
            val title = obj.getString("Title")
            val year = obj.getString("Year")
            val movie = "$title ($year)"
            movieList.add(movie)
        }
    }

    return movieList
}

