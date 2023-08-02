import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import client.PasteyClient
import entities.ClipNote
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

object ApplicationState : ViewModel() {
    private var _clipNote = mutableStateListOf<ClipNote>()
    val clipNote: List<entities.ClipNote> = _clipNote

    val scope = viewModelScope

    init {
            getNotes()
    }

    fun addAll(results: List<ClipNote>) {
        _clipNote.addAll(results)
    }

    fun setNotes(results: List<ClipNote>) {
        _clipNote.clear()
        _clipNote.addAll(results)
    }

    fun getNotes() {
        scope.launch {
        val notes = PasteyClient.getNotes()
        setNotes(notes)
//            delay(4000)
//            getNotes()
        }
    }

    fun clearNotes() {
        _clipNote.clear()
    }

    fun addClipNote(note: ClipNote) {
        if (note.date == null || note.date == "") {
            note.date = Clock.System.now().toString()
        }
        scope.launch {
            PasteyClient.createNote(note)
        }
        _clipNote.add(note)
    }

    fun removeClipNoteAt(index: Int) {
        _clipNote.removeAt(index)
    }

    fun removeClipNoteById(id: Long) {
        scope.launch {
            PasteyClient.deleteNote(id)
        }
        _clipNote.removeIf { it.id == id }
    }

    fun updateClipNoteAt(index: Int, note: ClipNote) {
        scope.launch {
            PasteyClient.updateNotes(note)
        }
        _clipNote[index] = note
    }

    fun updateClipNoteById(id: Long, note: ClipNote) {
        scope.launch {
            PasteyClient.updateNotes(note)
        }
        _clipNote[_clipNote.indexOfFirst { it.id == id }] = note

    }

}