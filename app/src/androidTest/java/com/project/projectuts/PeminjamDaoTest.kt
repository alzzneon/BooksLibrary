package com.project.projectuts

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.project.projectuts.dao.PeminjamanDao
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.model.Buku
import com.project.projectuts.model.Peminjaman
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PeminjamDaoTest {
    private lateinit var database: AplikasiDatabase
    private lateinit var peminjamanDao: PeminjamanDao

    @Before
    fun setUP(){
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AplikasiDatabase::class.java
        ).build()
        peminjamanDao = database.peminjamanDao()
    }
    @After
    fun closeDB(){
        database.peminjamanDao()
    }
    @Test
    fun testInsertPengunjung() = runBlocking {
        val peminjaman = Peminjaman(namaPeminjam = "Mohamad Arung", bukuDipinjam = "Harry Potter", tglDipinjam = "2024")

        peminjamanDao.insertPeminjaman(peminjaman)

        assertEquals(peminjaman.namaPeminjam, "Mohamad Arung")
        assertEquals(peminjaman.bukuDipinjam, "Harry Potter")
        assertEquals(peminjaman.tglDipinjam, 2024)
    }
    @Test
    fun testDeletePeminjaman() = runBlocking {
        val peminjaman = Peminjaman(namaPeminjam = "Mohamad Arung", bukuDipinjam = "Harry Potter", tglDipinjam = "2024")
    }
}