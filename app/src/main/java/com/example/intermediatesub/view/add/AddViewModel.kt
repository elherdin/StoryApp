package com.example.intermediatesub.view.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermediatesub.data.response.RegisterResponse
import com.example.intermediatesub.repository.Repository
import kotlinx.coroutines.launch
import java.io.File

class AddViewModel(private val repository: Repository) : ViewModel() {

    private val _resultUpload = MutableLiveData<RegisterResponse>()
    val resultUpload: LiveData<RegisterResponse> = _resultUpload

    fun uploadImage(file: File, description: String) {
        viewModelScope.launch {
            repository.getSession().collect {
                _resultUpload.value = repository.uploadImage(file, description, it.token)
            }
        }
    }
}