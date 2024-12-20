package com.project.books_library.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    indices = [Index(value = ["title"], unique = true)]
)
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val genre: String,
    val author: String,
    val year_publish: Int,
    val description: String,
    val image_url: String
)