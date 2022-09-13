package com.bumian.memorynotes.presentation.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumian.memorynotes.repo.api.room.note.Note
import com.bumian.memorynotes.repo.auth.AuthRepo
import com.bumian.memorynotes.repo.notes.NotesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val IS_GRID_VIEW = "IS_GRID_VIEW"

class HomeViewModel(
    app: Application,
    private val notesRepo: NotesRepo,
    private val userRepo: AuthRepo
): AndroidViewModel(app) {

    private val sharedPreferences by lazy {
        app.getSharedPreferences(null, Context.MODE_PRIVATE)
    }

    val isGridViewLiveData by lazy { MutableLiveData<Boolean>() }

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

    fun fetchViewType() {
        viewModelScope.launch {
            val isGridView = sharedPreferences.getBoolean(IS_GRID_VIEW, false)
            withContext(Dispatchers.Main) { isGridViewLiveData.value = isGridView }
        }
    }

    fun setViewType(isGridView: Boolean) {
        viewModelScope.launch {
            sharedPreferences.edit().putBoolean(IS_GRID_VIEW, isGridView).apply()
            withContext(Dispatchers.Main) { isGridViewLiveData.value = isGridView }
        }
    }

}