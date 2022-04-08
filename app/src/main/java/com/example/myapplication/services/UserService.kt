package com.example.myapplication.services

import com.example.myapplication.model.User
import com.example.myapplication.model.UserToLogin
import retrofit2.http.*

interface UserService {
    @GET("users/")          suspend fun listAll(): List<User>
    @GET("users/{id}")      suspend fun get(@Path("id") id: Long): User
    @POST("users")          suspend fun create(@Body u: User): User
    @PUT("users/{id}")      suspend fun update(@Path("id") id: Int, @Body u: User)
    @DELETE("users/{id}")   suspend fun delete(@Path("id") id: Int)
    @POST("users/login")    suspend fun login(@Body u: UserToLogin): Long
}