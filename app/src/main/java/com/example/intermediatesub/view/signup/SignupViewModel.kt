package com.example.intermediatesub.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermediatesub.data.response.RegisterResponse
import com.example.intermediatesub.repository.Repository
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: Repository) : ViewModel() {

    private val _resultRegister = MutableLiveData<RegisterResponse>()
    val resultRegister: LiveData<RegisterResponse> = _resultRegister

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _resultRegister.value = repository.registerUser(name, email, password)
        }
    }
}