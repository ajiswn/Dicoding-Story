package com.ajiswn.dicodingstory.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajiswn.dicodingstory.data.Repository
import com.ajiswn.dicodingstory.data.remote.response.ErrorResponse
import com.ajiswn.dicodingstory.data.remote.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String): LiveData<RegisterResponse> {
        val result = MutableLiveData<RegisterResponse>()
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.register(name, email, password)
                result.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                result.postValue(RegisterResponse(error = true, message = errorMessage))
            }
            _isLoading.value = false
        }
        return result
    }
}