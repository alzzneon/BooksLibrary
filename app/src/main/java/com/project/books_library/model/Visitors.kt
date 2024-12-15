package com.project.books_library.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visitors")
data class Visitors(
    @PrimaryKey(autoGenerate = true) var id_visitor: Int? = null,
    var name: String,
    var visit_date: String,
    var gender: String,
    var address: String,
    var phone_number: String,
    var email: String,
    var visit_purpose: String
)