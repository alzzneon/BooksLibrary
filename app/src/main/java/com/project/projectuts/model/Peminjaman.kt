    package com.project.projectuts.model

    import androidx.room.Entity
    import androidx.room.PrimaryKey

    @Entity(tableName = "peminjam")
    data class Peminjaman(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val namaPeminjam: String,
        val bukuDipinjam: String,
        val tglDipinjam: String
    )
