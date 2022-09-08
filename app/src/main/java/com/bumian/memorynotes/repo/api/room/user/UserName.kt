package com.bumian.memorynotes.repo.api.room.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserName(
    @PrimaryKey
    val name: String
)