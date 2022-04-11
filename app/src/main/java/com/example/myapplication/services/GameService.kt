package com.example.myapplication.services

import com.example.myapplication.model.Game.Game
import com.example.myapplication.model.Game.GameToCreate
import retrofit2.http.*

interface GameService {
    @GET("games/")          suspend fun listAll(): List<Game>
    @GET("games/{id}")      suspend fun get(@Path("id") id: Long): Game
    @GET("games/oneUser")   suspend fun getGamesOneUser(): List<Game>
    @POST("games")          suspend fun create(@Body g: GameToCreate): Game
    @PUT("games/{id}")      suspend fun update(@Path("id") id: Long, @Body g: Game)
    @DELETE("games/{id}")   suspend fun delete(@Path("id") id: Long)
}