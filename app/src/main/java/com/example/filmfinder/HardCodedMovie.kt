package com.example.filmfinder

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import kotlinx.coroutines.launch


class HardCodedMovie: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val movieDao = (application as App).movieDao // References the movieDao init.. in App class
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        setContent {
            GUI_HardCodedMovie(movieDao, sharedPref)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun GUI_HardCodedMovie(movieDao: MovieDao, sharedPref: android.content.SharedPreferences){
    val scope = rememberCoroutineScope()
    val vScroll = rememberScrollState()

    //hard-coded movies to be added to database
    val toAdd by rememberSaveable { mutableStateOf(mutableListOf<Movie>(
        Movie(
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
        ),
        Movie(
            Title = "Batman: The Dark Knight Returns, Part 1",
            Year = "2012",
            Rated = "PG-13",
            Released = "25 Sep 2012",
            Runtime = "76 min",
            Genre = "Animation, Action, Crime, Drama, Thriller",
            Director = "Jay Oliva",
            Writer = "Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman",
            Actors = "Peter Weller, Ariel Winter, David Selby, Wade Williams",
            Plot = "Batman has not been seen for ten years. A new breed of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back into the cape and cowl. But, does he still have what it takes to fight crime in a new era?"
        ),
        Movie(
            Title = "The Lord of the Rings: The Return of the King",
            Year = "2003",
            Rated = "PG-13",
            Released = "17 Dec 2003",
            Runtime = "201 min",
            Genre = "Action, Adventure, Drama",
            Director = "Peter Jackson",
            Writer = "J.R.R. Tolkien, Fran Walsh, Philippa Boyens",
            Actors = "Elijah Wood, Viggo Mortensen, Ian McKellen",
            Plot = "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
        ),
        Movie(
            Title = "Inception",
            Year = "2010",
            Rated = "PG-13",
            Released = "16 Jul 2010",
            Runtime = "148 min",
            Genre = "Action, Adventure, Sci-Fi",
            Director = "Christopher Nolan",
            Writer = "Christopher Nolan",
            Actors = "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
            Plot = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster."
        ),
        Movie(
            Title = "The Matrix",
            Year = "1999",
            Rated = "R",
            Released = "31 Mar 1999",
            Runtime = "136 min",
            Genre = "Action, Sci-Fi",
            Director = "Lana Wachowski, Lilly Wachowski",
            Writer = "Lilly Wachowski, Lana Wachowski",
            Actors = "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
            Plot = "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence."
        )

    )) }

    var moviesAdded by rememberSaveable { mutableStateOf(sharedPref.getBoolean("mvAdded", false)) } // if false only user can add movies, else cannot add (Popup shows)
    var showDialog = rememberSaveable { mutableStateOf(false) } // if true only pop up shows
    var displayMovies by rememberSaveable { mutableStateOf(false) } // if true movie details are displayed

    var movies by rememberSaveable { mutableStateOf(mutableListOf<Movie>()) }

    Column(
        Modifier.fillMaxSize().background(color = Color.Gray).padding(5.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start
        ){
            Text("\uD83C\uDFAC Movies to add", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, textAlign = TextAlign.Center), modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(4.dp))
            Text("\uD83C\uDFAC 'The Shawshank Redemption'", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center))
            Spacer(Modifier.height(4.dp))
            Text("\uD83C\uDFAC 'The Dark Knight Returns, Part 1'", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Left))
            Spacer(Modifier.height(4.dp))
            Text("\uD83C\uDFAC 'The Lord of the Rings: The Return of the King'", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Left))
            Spacer(Modifier.height(4.dp))
            Text("\uD83C\uDFAC 'Inception'", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Left))
            Spacer(Modifier.height(4.dp))
            Text("\uD83C\uDFAC 'The Matrix'", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Left))
        }
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    scope.launch {
                        if(!moviesAdded){
                            toAdd.forEach{ movie ->
                                movieDao.insertMovie(movie)
                                moviesAdded = true
                                sharedPref.edit().putBoolean("mvAdded", moviesAdded).apply()
                                displayMovies = false
                            }


                        }
                        else{
                            showDialog.value = true
                        }
                    }


                }
            ) {
                Text("Add")
            }

            Spacer(Modifier.width(4.dp))

            Button(
                enabled = moviesAdded,
                onClick = {
                    scope.launch {

//                        val shawShank = movieDao.getByTitle("The Shawshank Redemption")
//                        movies.add(shawShank)
//                        val darkKnight = movieDao.getByTitle("Batman: The Dark Knight Returns, Part 1")
//                        movies.add(darkKnight)
//                        val lordOfTheRings = movieDao.getByTitle("The Lord of the Rings: The Return of the King")
//                        movies.add(lordOfTheRings)
//                        val Inception = movieDao.getByTitle("Inception")
//                        movies.add(Inception)
//                        val matrix = movieDao.getByTitle("The Matrix")
//                        movies.add(matrix)

                        movies = movieDao.getSavedMovies().toMutableList()


                        displayMovies = true




                    }

                }
            ) {
                Text("View")
            }
            Spacer(Modifier.width(4.dp))
            Button(
                onClick = {
                    scope.launch {
                        toAdd.forEach{ movie->
                            movieDao.deleteMovie(movie)
                        }
                        moviesAdded=false
                        sharedPref.edit().putBoolean("mvAdded", moviesAdded).apply()
                        displayMovies = false
                        movies.clear()
                    }
                }

            ) {
                Text("Delete")
            }
        }
        if(showDialog.value){
            ShowPopUpWindow(showDialog)
        }



        if (displayMovies) {
            Column(Modifier.verticalScroll(vScroll)) {
                movies.forEach { m ->

                    Column(Modifier.padding(5.dp).border(2.dp, color = Color.Black).padding(5.dp)) {
                        Text("🎬 ${m?.Title} (${m?.Year})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Rated: ${m?.Rated}", fontWeight = FontWeight.Bold)
                        Text("Released: ${m?.Released}", fontWeight = FontWeight.Bold)
                        Text("Runtime: ${m?.Runtime}", fontWeight = FontWeight.Bold)
                        Text("Genre: ${m?.Genre}", fontWeight = FontWeight.Bold)
                        Text("Director: ${m?.Director}", fontWeight = FontWeight.Bold)
                        Text("Writer: ${m?.Writer}", fontWeight = FontWeight.Bold)
                        Text("Actors: ${m?.Actors}", fontWeight = FontWeight.Bold)
                        Text("Plot: ${m?.Plot}", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }



    }
}

@Composable
fun ShowPopUpWindow(showDialog: MutableState<Boolean>){
    AlertDialog(
        onDismissRequest = { showDialog.value = false},
        title = {Text("Movie Already Added ⚠\uFE0F")},
        text = { Text("Cannot add since a movie already exists in the Database!")},
        confirmButton = {
            Button(onClick = {showDialog.value = false}){
                Text("Close")
            }
        }
    )
}