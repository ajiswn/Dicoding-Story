package com.ajiswn.dicodingstory.view.addnewstory

import androidx.lifecycle.ViewModel
import com.ajiswn.dicodingstory.data.Repository
import java.io.File

class AddNewStoryViewModel(private val repository: Repository) : ViewModel() {
   fun uploadImage(multipartBody: File, description: String) = repository.addNewStory(multipartBody, description)
}