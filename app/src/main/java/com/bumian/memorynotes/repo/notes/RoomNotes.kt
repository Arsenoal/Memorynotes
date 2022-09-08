package com.bumian.memorynotes.repo.notes

import com.bumian.memorynotes.repo.api.room.note.Note
import com.bumian.memorynotes.repo.api.room.note.NotesDao
import java.util.*

class RoomNotes(
    private val notesDao: NotesDao
): NotesRepo {
    override suspend fun getAll() = notesDao.getNotes()

    override suspend fun addNote(note: Note) = notesDao.insert(note)

    override suspend fun removeNote(note: Note) = notesDao.deleteNote(note.date)

    override suspend fun filterNotes(startDate: Date, endDate: Date) = notesDao.filterNotesByDate(startDate, endDate)

}