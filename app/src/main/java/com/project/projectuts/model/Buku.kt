package com.project.projectuts.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buku")
data class Buku(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val genre: String,
    val pengarang: String,
    val tahunTerbit: Int,
    val tanggalDitambahkan : Long
)
