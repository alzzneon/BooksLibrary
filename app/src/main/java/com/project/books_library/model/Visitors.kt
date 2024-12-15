package com.project.books_library.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visitors")
data class Visitors(
    @PrimaryKey(autoGenerate = true) val id_visitor: Int? = null,
    val name: String,
    val visit_date: String,
    val gender: String,
    val address: String,
    val phone_number: String,
    val email: String,
    val visit_purpose: String
)