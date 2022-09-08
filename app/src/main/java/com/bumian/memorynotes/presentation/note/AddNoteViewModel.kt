package com.bumian.memorynotes.presentation.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumian.memorynotes.repo.api.room.note.Note
import com.bumian.memorynotes.repo.notes.NotesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddNoteViewModel(
    private val notesRepo: NotesRepo
): ViewModel() {

    val noteAddedLiveData by lazy { MutableLiveData<Unit>() }

    fun addNote(note: Note) {
        viewModelScope.launch {
            notesRepo.addNote(note)
            withContext(Dispatchers.Main) { noteAddedLiveData.value = Unit }
        }
    }
}