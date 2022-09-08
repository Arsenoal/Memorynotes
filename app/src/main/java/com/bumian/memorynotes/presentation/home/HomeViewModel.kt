package com.bumian.memorynotes.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumian.memorynotes.repo.api.room.note.Note
import com.bumian.memorynotes.repo.auth.AuthRepo
import com.bumian.memorynotes.repo.notes.NotesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeViewModel(
    private val notesRepo: NotesRepo,
    private val userRepo: AuthRepo
): ViewModel() {

    val allNotesLiveData by lazy { MutableLiveData<List<Note>>() }

    val userNameLiveData by lazy { MutableLiveData<String>() }

    val filteredNotesLiveData by lazy { MutableLiveData<List<Note>>() }

    val noteRemovedLiveData by lazy { MutableLiveData<Unit>() }

    fun loadUserName() {
        viewModelScope.launch {
            userRepo.getUser()?.name?.let {
                withContext(Dispatchers.Main) {
                    userNameLiveData.value = it
                }
            }
        }
    }

    fun getNotes() {
        viewModelScope.launch {
            val notes = notesRepo.getAll()
            println(notes)
            withContext(Dispatchers.Main) { allNotesLiveData.value = notes }
        }
    }

    fun removeNote(note: Note) {
        viewModelScope.launch {
            notesRepo.removeNote(note)
            withContext(Dispatchers.Main) { noteRemovedLiveData.value = Unit }
        }
    }

    fun filterNotes(startDate: Date, endDate: Date) {
        viewModelScope.launch {
            val notes = notesRepo.filterNotes(startDate, endDate)
            withContext(Dispatchers.Main) { filteredNotesLiveData.value = notes }
        }
    }

}