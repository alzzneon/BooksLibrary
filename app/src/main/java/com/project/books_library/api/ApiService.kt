package com.project.books_library.api

import androidx.lifecycle.LiveData
import com.project.books_library.model.Book
import com.project.books_library.model.Visitors
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @DELETE("/books/{id}")
    suspend fun deleteBook(@Path("i") id: Int): Book

    @GET("/visitors")
    suspend fun getVisitors(): List<Visitors>

    @POST("/visitors")
    suspend fun insertVisitors(@Body visitors: Visitors)

    @DELETE("/visitors/{id_visitor}")
    suspend fun deleteVisitor(@Path("id_visitor") id: Int): Visitors
}