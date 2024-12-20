package com.ajiswn.dicodingstory.data

import androidx.lifecycle.liveData
import com.ajiswn.dicodingstory.data.pref.UserModel
import com.ajiswn.dicodingstory.data.pref.UserPreference
import com.ajiswn.dicodingstory.data.remote.response.AddNewStoryResponse
import com.ajiswn.dicodingstory.data.remote.response.LoginResponse
import com.ajiswn.dicodingstory.data.remote.response.RegisterResponse
import com.ajiswn.dicodingstory.data.remote.response.StoryResponse
import com.ajiswn.dicodingstory.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name: String, email: String, password: String, ): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    fun addNewStory(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val user = userPreference.getSession().first()
        val token = "Bearer ${user.token}"
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.addNewStory(token, multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddNewStoryResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }

    }

    suspend fun getStories(): StoryResponse{
        val user = userPreference.getSession().first()
        val token = "Bearer ${user.token}"
        return apiService.getStories(token)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(userPref: UserPreference, apiService: ApiService) = Repository(userPref, apiService)
    }
}