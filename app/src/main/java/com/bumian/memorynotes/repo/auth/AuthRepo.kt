package com.bumian.memorynotes.repo.auth

import com.bumian.memorynotes.repo.api.room.user.UserName

interface AuthRepo {
    suspend fun enterWith(userName: String): Boolean

    suspend fun getUser(): UserName?
}