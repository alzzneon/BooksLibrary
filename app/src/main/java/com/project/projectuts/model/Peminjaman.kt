package com.project.projectuts.model
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "peminjam",
    foreignKeys = [
        ForeignKey(
            entity = Buku::class,
            parentColumns = ["id"],
            childColumns = ["bukuId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Pengunjung::class,
            parentColumns = ["id"],
            childColumns = ["pengunjungId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Peminjaman(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pengunjungId: Int,  // Foreign Key ke Pengunjung
    val bukuId: Int,        // Foreign Key ke Buku
    val tanggalPinjam: String
)


