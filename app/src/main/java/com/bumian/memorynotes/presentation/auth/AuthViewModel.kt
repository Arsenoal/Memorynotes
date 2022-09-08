package com.bumian.memorynotes.presentation.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumian.memorynotes.repo.auth.AuthRepo
import com.bumian.memorynotes.repo.api.room.user.UserName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val authRepo: AuthRepo
): ViewModel() {

    val userLoginSuccess by lazy { MutableLiveData<Boolean>() }

    val existingUsers by lazy { MutableLiveData<UserName>() }

    fun checkAccount() {
        viewModelScope.launch {
            val user = authRepo.getUser()

            withContext(Dispatchers.Main) {
                if(user != null) {
                    existingUsers.value = user
                }
            }
        }
    }

    fun enterWith(userName: String) {
        viewModelScope.launch {
            val result = authRepo.enterWith(userName)

            withContext(Dispatchers.Main) { userLoginSuccess.value = result }
        }
    }
}