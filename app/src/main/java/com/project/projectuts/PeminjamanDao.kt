package com.project.projectuts
import androidx.room.*

@Dao
interface PeminjamanDao {

    // Insert satu peminjaman
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeminjaman(peminjam: Peminjaman)

    // Insert banyak peminjaman sekaligus
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPeminjaman(vararg peminjam: Peminjaman)

    // Update peminjaman
    @Update
    suspend fun updatePeminjaman(peminjam: Peminjaman)

    // Hapus peminjaman
    @Delete
    suspend fun deletePeminjaman(peminjam: Peminjaman)

    // Dapatkan semua data peminjaman
    @Query("SELECT * FROM peminjam")
    suspend fun getAllPeminjaman(): List<Peminjaman>

    // Dapatkan peminjaman berdasarkan ID
    @Query("SELECT * FROM peminjam WHERE id = :peminjamId LIMIT 1")
    suspend fun getPeminjamanById(peminjamId: Int): Peminjaman?

    // Dapatkan semua peminjaman beserta detail pengunjung dan buku
    @Query("""
        SELECT peminjam.id, peminjam.pengunjungId, peminjam.bukuId, peminjam.tanggalPinjam,
               pengunjung.nama AS nama, 
               buku.judul AS judul, 
               buku.pengarang AS pengarang 
        FROM peminjam
        INNER JOIN pengunjung ON peminjam.pengunjungId = pengunjung.id
        INNER JOIN buku ON peminjam.bukuId = buku.id
    """)
    suspend fun getAllPeminjamanWithDetails(): List<PeminjamanRelasi>
}
