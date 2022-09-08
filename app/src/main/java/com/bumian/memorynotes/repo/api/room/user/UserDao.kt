package com.bumian.memorynotes.repo.api.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM username WHERE name=:name")
    suspend fun findUserBy(name: String): UserName?

    @Query("SELECT * FROM username")
    suspend fun getUser(): UserName?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userName: UserName)

}