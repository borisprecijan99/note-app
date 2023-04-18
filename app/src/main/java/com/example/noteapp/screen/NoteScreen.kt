package com.example.noteapp.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteapp.components.NoteButton
import com.example.noteapp.components.NoteInputText
import com.example.noteapp.data.NotesDataSource
import com.example.noteapp.model.Note
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showSystemUi = true)
@Composable
fun NoteScreen(
    notes: List<Note> = NotesDataSource().loadNotes(),
    onAddNote: (Note) -> Unit = {},
    onRemoveNote: (Note) -> Unit = {}
) {
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    Column {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Text(text = "Note App")
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoteInputText(
                text = title,
                label = "Title",
                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
                onTextChange = {
                    title = it
                }
            )
            NoteInputText(
                text = description,
                label = "Description",
                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
                onTextChange = {
                    description = it
                }
            )
            NoteButton(
                text = "Add note",
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        val note = Note(title = title, description = description)
                        onAddNote(note)
                        title = ""
                        description = ""
                        Toast.makeText(context, "Note added", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
        Divider(modifier = Modifier.padding(10.dp))
        LazyColumn {
            items(items = notes) {
                NoteRow(
                    note = it,
                    onClick = {
                        onRemoveNote(it)
                        Toast.makeText(context, "Note removed", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteRow(
    note: Note,
    onClick: (Note) -> Unit = {}
) {
    Surface(
        onClick = {
            onClick(note)
        },
        shape = RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp),
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(text = note.title, style = MaterialTheme.typography.subtitle2)
            Text(text = note.description, style = MaterialTheme.typography.subtitle1)
            Text(
                text = note.entryDate.format(DateTimeFormatter.ofPattern("EEE, d MMM")),
                style = MaterialTheme.typography.caption
            )
        }
    }
}