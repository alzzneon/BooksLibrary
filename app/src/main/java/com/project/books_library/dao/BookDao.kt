package com.project.books_library.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.books_library.model.Book

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListBooks(books: List<Book>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Query("SELECT * FROM books")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: Int): Book

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteById(id: Int?)
}
