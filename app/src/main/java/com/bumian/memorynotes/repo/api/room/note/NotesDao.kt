package com.bumian.memorynotes.repo.api.room.note

import androidx.room.*
import java.util.*

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("DELETE FROM note WHERE date=:noteDate")
    suspend fun deleteNote(noteDate: Date)

    @Query("SELECT * FROM note ORDER BY date")
    suspend fun getNotes(): List<Note>

    @Query("SELECT * FROM note WHERE date >= :startDate AND date <= :endDate")
    suspend fun filterNotesByDate(startDate: Date, endDate: Date): List<Note>
}