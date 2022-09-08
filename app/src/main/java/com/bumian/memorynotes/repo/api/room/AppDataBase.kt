package com.bumian.memorynotes.repo.api.room

import android.content.Context
import androidx.room.*
import com.bumian.memorynotes.repo.api.room.note.Note
import com.bumian.memorynotes.repo.api.room.note.NotesDao
import com.bumian.memorynotes.repo.api.room.user.UserDao
import com.bumian.memorynotes.repo.api.room.user.UserName

private const val APP_DATABASE = "APP_DATABASE"

@Database(
    entities = [
        Note::class,
        UserName::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDataBase: RoomDatabase() {

    abstract fun noteDao(): NotesDao

    abstract fun userDao(): UserDao

    companion object {

        var db: AppDataBase? = null

        fun instance(context: Context): AppDataBase {

            if(db == null) {
                db = Room.databaseBuilder(context, AppDataBase::class.java, APP_DATABASE)
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return db!!
        }
    }
}