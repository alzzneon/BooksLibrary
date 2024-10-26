package com.project.projectuts

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import com.project.projectuts.dao.PeminjamanDao
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.model.Peminjaman

@RunWith(AndroidJUnit4::class)
class PeminjamanDaoTest {

    private lateinit var database: AplikasiDatabase
    private lateinit var peminjamanDao: PeminjamanDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AplikasiDatabase::class.java
        ).build()
        peminjamanDao = database.peminjamanDao()
    }

    @After
    fun closeDB() {
        database.close()
    }

    @Test
    fun testInsertPeminjaman() = runBlocking {
        val peminjaman = Peminjaman(namaPeminjam = "Aldi N F", bukuDipinjam = "alien itu ada", tglDipinjam = "2024-10-25")

        peminjamanDao.insertPeminjaman(peminjaman)


        assertEquals(peminjaman.namaPeminjam,"Aldi N F")
        assertEquals(peminjaman.bukuDipinjam,"alien itu ada")
        assertEquals(peminjaman.tglDipinjam,"2024-10-25")
    }

    @Test
    fun testUpdatePeminjaman() = runBlocking {
        val peminjaman = Peminjaman(id = 1, namaPeminjam = "Aldi N F", bukuDipinjam = "Karma", tglDipinjam = "2024-10-25")
        peminjamanDao.insertPeminjaman(peminjaman)

        val updatedPeminjaman = peminjaman.copy(bukuDipinjam = "Karma")
        peminjamanDao.updatePeminjaman(updatedPeminjaman)

        val retrievedPeminjaman = peminjamanDao.getPeminjamanByid(peminjaman.id)
        assertEquals("Karma", retrievedPeminjaman?.bukuDipinjam)
        assertEquals("Aldi N F", retrievedPeminjaman?.namaPeminjam)
        assertEquals("2024-10-25", retrievedPeminjaman?.tglDipinjam)
    }

    @Test
    fun testDeletePeminjaman() = runBlocking {
        val peminjaman = Peminjaman(id = 1, namaPeminjam = "Aldi N F", bukuDipinjam = "alien itu ada", tglDipinjam = "2024-10-25")
        peminjamanDao.insertPeminjaman(peminjaman)

        val retrievedPeminjaman = peminjamanDao.getPeminjamanByid(peminjaman.id)
        assertNotNull(retrievedPeminjaman)
        assertEquals("alien itu ada", retrievedPeminjaman?.bukuDipinjam)

        peminjamanDao.deletePeminjaman(peminjaman)

        val deletedPeminjaman = peminjamanDao.getPeminjamanByid(peminjaman.id)
        assertNull(deletedPeminjaman)
    }
}
