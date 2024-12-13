package com.project.BooksLibrary.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val genre: String,
    val author: String,
    val year_publish: Int,
    val description: String,
    val image_url: String
)