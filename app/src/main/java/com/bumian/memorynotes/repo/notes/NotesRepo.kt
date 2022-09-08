package com.bumian.memorynotes.repo.notes

import com.bumian.memorynotes.repo.api.room.note.Note
import java.util.*

interface NotesRepo {

    suspend fun getAll(): List<Note>

    suspend fun addNote(note: Note)

    suspend fun removeNote(note: Note)

    suspend fun filterNotes(startDate: Date, endDate: Date): List<Note>
}