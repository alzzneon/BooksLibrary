package com.project.books_library.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "lending",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["title"],
            childColumns = ["book_title"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Visitors::class,
            parentColumns = ["name"],
            childColumns = ["visitor_name"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Lending(
    @PrimaryKey(autoGenerate = true) val id_lending: Int? = null,
    var visitor_name: String,
    var book_title: String,
    var lending_date: String,
    var return_date: String? = null,
    var status: String
)
