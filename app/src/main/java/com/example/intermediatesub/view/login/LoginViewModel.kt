package com.example.intermediatesub.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermediatesub.data.pref.UserModel
import com.example.intermediatesub.data.response.LoginResponse
import com.example.intermediatesub.repository.Repository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _resultLogin = MutableLiveData<LoginResponse>()
    val resultLogin: LiveData<LoginResponse> = _resultLogin


    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _resultLogin.value = repository.loginUser(email, password)
        }
    }
}