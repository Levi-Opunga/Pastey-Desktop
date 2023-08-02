@file:OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class
)

import ApplicationState
import ApplicationState.setNotes
import ApplicationState.updateClipNoteById
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import client.PasteyClient
import ApplicationState as ap

import client.getNotes
import com.example.compose.AppTheme
import com.google.gson.Gson
import com.google.gson.JsonObject
import compose.icons.AllIcons
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.AllIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Clipboard
import compose.icons.fontawesomeicons.solid.Redo
import entities.ClipNote
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.*
import org.jetbrains.skia.impl.Log
import utils.copyToClipboard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(setShowDialog: (Boolean) -> Unit) {
    val clipNotes = remember { ApplicationState.clipNote }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Surface(
        Modifier.fillMaxSize(),
    ) {
        Scaffold(
            Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton({}){
                                Icon(FontAwesomeIcons.Solid.Clipboard, contentDescription = "Clipboard Icon", Modifier.size(20.dp))
                            }
                            Text("Pastey")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Settings")
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                BottomAppBar(
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {


                        IconButton(onClick = {
                            setShowDialog(true)
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add a note")
                        }
                        IconButton(
                            onClick = {
                                ap.getNotes()
                            },
                        ) {
                            Icon(FontAwesomeIcons.Solid.Redo, contentDescription = "Refresh", Modifier.size(20.dp))
                        }
                    }

                }
            }
        ) { paddingValues ->

            LazyVerticalGrid(
                modifier = Modifier.padding(paddingValues),
                columns = GridCells.Fixed(2),
            ) {
                items(
                    count = clipNotes.size,
                    key = { index: Int -> clipNotes[index].id },
                    contentType = { index: Int -> "Clip Text" }
                ) { i ->
                    var note = clipNotes[i]
                    val timeInstant = note.date?.toInstant()?.toLocalDateTime(TimeZone.currentSystemDefault())
                    val day = timeInstant?.dayOfWeek?.name?.lowercase()?.capitalize()
                    val month = timeInstant?.month?.name?.lowercase()?.capitalize()
                    val year = timeInstant?.year
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        border = BorderStroke(5.dp, MaterialTheme.colorScheme.primary),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        )

                    ) {
                        var editMode by remember { mutableStateOf(false) }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text((day + ", " + month + " " + year))

                                IconButton(
                                    onClick = {
                                              ap.removeClipNoteById(note.id)
                                    },

                                    ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Settings")
                                }

                            }
                            Column(
                                Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 2.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.Start,
                            ) {

                                if (editMode) {
                                    var text by remember { mutableStateOf(note.text ?: "") }
                                    TextField(
                                        value = text,
                                        onValueChange = {
                                            text = it
                                            note.text = it
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp),
                                        label = { Text("Note") },
                                        singleLine = false,
                                        maxLines = 10,
                                        shape = MaterialTheme.shapes.medium,
                                    )
                                } else {
                                    Text((note.text ?: "No Title"))
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Button(onClick = {
                                    copyToClipboard(note.text ?: "")

                                }) {
                                    Text("Copy")
                                }
                                if (editMode) {
                                    Button(onClick = {
                                        editMode = false
                                        updateClipNoteById(note.id, note)
                                    }) {
                                        Text("Save")
                                    }
                                } else {
                                    Button(onClick = {
                                        editMode = true
                                    }) {
                                        Text("Edit")
                                    }
                                }
                            }
                        }
                    }

                }
            }

        }
    }


}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pastey",
    ) {
        AppTheme {
            var (showDialog, setShowDialog) = remember { mutableStateOf(false) }
            ShowDialog(showDialog, setShowDialog)
            Content(setShowDialog)
        }

    }
}

@Composable
fun ShowDialog(showDialog: Boolean, setShowDialog: (Boolean) -> Unit) {
    var note by remember { mutableStateOf(ClipNote()) }
    note = note.copy(date = Clock.System.now().toString())
    if (showDialog) {
        Dialog(
            state = rememberDialogState(
                size = DpSize(500.dp, 500.dp),
                position = WindowPosition(Alignment.Center)
            ),
            resizable = true,
            onCloseRequest = { setShowDialog(false) },
            onPreviewKeyEvent = {
                if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                    setShowDialog(false)
                    true
                } else {
                    false
                }
            }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column {
                    Text("Note Title")
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text("Title") },
                        modifier = Modifier
                            .fillMaxWidth()

                    )
                    Text("Note Content")
                    OutlinedTextField(
                        value = note.text ?: "",
                        onValueChange = { note = note.copy(text = it) },
                        label = { Text("Content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        singleLine = false,
                        maxLines = 10,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = note.hidden ?: false,
                            onCheckedChange = {
                                note = note.copy(hidden = it)
                            })
                        Text("Hidden")
                    }
                    Button(
                        onClick = {
                            ap.addClipNote(note)
                            setShowDialog(false)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text("Add Note")
                    }

                }
            }
        }
    }
}


