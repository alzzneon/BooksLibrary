package com.project.projectuts
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pengunjung")
data class Pengunjung(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val tanggalKunjungan: String
)
