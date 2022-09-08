package com.bumian.memorynotes.repo.auth

import com.bumian.memorynotes.repo.api.room.AppDataBase
import com.bumian.memorynotes.repo.api.room.user.UserName

class RoomAuth(
    private val db: AppDataBase
): AuthRepo {

    override suspend fun enterWith(userName: String) : Boolean {
        return  if(db.userDao().findUserBy(userName) != null) {
            true
        } else {
            db.userDao().addUser(UserName(userName))
            false
        }
    }

    override suspend fun getUser(): UserName? {
        return db.userDao().getUser()
    }
}