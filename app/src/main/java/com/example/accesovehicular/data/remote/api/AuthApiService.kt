package com.example.accesovehicular.data.remote.api

import com.example.accesovehicular.data.remote.dto.LoginRequestDto
import com.example.accesovehicular.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
}
