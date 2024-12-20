package com.project.books_library.api

import com.project.books_library.model.Book
import com.project.books_library.model.Lending
import com.project.books_library.model.Visitors
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/books")
    suspend fun getBooks(): List<Book>

    @GET("/books/{id}")
    suspend fun getBookById(@Path("id") id: Int): Book

    @POST("/books")
    suspend fun insertBook(@Body book: Book)

    @PUT("/books/{id}")
    suspend fun updateBook(@Path("id") id: Int, @Body book: Book): Book

    @DELETE("/books/{id}")
    suspend fun deleteBook(@Path("id") id: Int): Response<Unit>

    @GET("/visitors")
    suspend fun getVisitors(): List<Visitors>

    @PUT("/visitors/{id_visitor}")
    suspend fun editVisitor(@Path("id_visitor") id: Int, @Body visitor: Visitors): Visitors

    @POST("/visitors")
    suspend fun insertVisitors(@Body visitors: Visitors)

    @DELETE("/visitors/{id_visitor}")
    suspend fun deleteVisitor(@Path("id_visitor") id: Int): Response<Unit>

    @GET("/lending")
    suspend fun getLendings(): List<Lending>

    @POST("/lending")
    suspend fun insertLending(@Body lending: Lending)

    @PUT("/lending/{id_lending}")
    suspend fun editLending(@Path("id_lending") id: Int, @Body lending: Lending): Lending

    @DELETE("/lending/{id_lending}")
    suspend fun deleteLending(@Path("id_lending") id: Int): Response<Unit>
}