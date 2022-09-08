package com.bumian.memorynotes

import android.app.Application
import com.google.firebase.FirebaseApp

class NotesApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
    }
}