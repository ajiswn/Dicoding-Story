package com.ajiswn.dicodingstory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ajiswn.dicodingstory.data.Repository
import com.ajiswn.dicodingstory.di.Injection
import com.ajiswn.dicodingstory.view.addnewstory.AddNewStoryViewModel
import com.ajiswn.dicodingstory.view.login.LoginViewModel
import com.ajiswn.dicodingstory.view.main.MainViewModel
import com.ajiswn.dicodingstory.view.signup.SignupViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            SignupViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(AddNewStoryViewModel::class.java)) {
            AddNewStoryViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}