package com.project.books_library.api

import com.project.books_library.model.Book
import com.project.books_library.model.Visitors
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/books")
    suspend fun getBooks(): List<Book>

    @GET("/books/{id}")
    suspend fun getBookById(@Path("id") id: Int): Book

    @POST("/books")
    suspend fun insertBook(@Body book: Book)

    @GET("/visitors")
    suspend fun getVisitors(): List<Visitors>

    @POST("/visitors")
    suspend fun insertVisitors(@Body visitors: Visitors)
}