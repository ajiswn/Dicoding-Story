package com.ajiswn.dicodingstory.data.remote.retrofit

import com.ajiswn.dicodingstory.data.remote.response.AddNewStoryResponse
import com.ajiswn.dicodingstory.data.remote.response.LoginResponse
import com.ajiswn.dicodingstory.data.remote.response.RegisterResponse
import com.ajiswn.dicodingstory.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddNewStoryResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String
    ): StoryResponse
}