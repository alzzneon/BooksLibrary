package com.project.projectuts.API

import com.project.projectuts.model.Book
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/books")
    suspend fun getBooks(): List<Book>
    @GET("/books/{id}")
    suspend fun getBookById(@Path("id") id: Int): Book
}