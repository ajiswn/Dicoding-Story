package com.ajiswn.dicodingstory.view.main

import androidx.lifecycle.*
import com.ajiswn.dicodingstory.R
import com.ajiswn.dicodingstory.data.Repository
import com.ajiswn.dicodingstory.data.pref.UserModel
import com.ajiswn.dicodingstory.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout() }
    }

    fun fetchStories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _stories.postValue(repository.getStories().listStory)
                _errorMessage.postValue(null)
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message ?: R.string.error_unknown}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}