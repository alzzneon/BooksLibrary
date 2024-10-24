package com.project.projectuts.model

data class PeminjamanRelasi(
    val id: Int,               // ID Peminjaman
    val pengunjungId: Int,     // ID Pengunjung
    val nama: String,          // Nama Pengunjung (dari pengunjung)
    val bukuId: Int,           // ID Buku yang dipinjam
    val judul: String,         // Judul Buku (dari buku)
    val pengarang: String,     // Pengarang Buku (dari buku)
    val tanggalPinjam: String   // Tanggal Peminjaman
)
