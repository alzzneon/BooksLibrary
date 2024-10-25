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
import com.project.projectuts.dao.BukuDao
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.model.Buku
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull

@RunWith(AndroidJUnit4::class)
class BukuDaoTest {

    private lateinit var database: AplikasiDatabase
    private lateinit var bukuDao: BukuDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AplikasiDatabase::class.java
        ).build()
        bukuDao = database.bukuDao()
    }

    @After
    fun clodeDB() {
        database.close()
    }

    @Test
    fun testInsertBuku() = runBlocking {
        val buku = Buku(judul = "Kotlin Programming", pengarang = "Bima Cakra", tahunTerbit = 2024)

        bukuDao.insertBuku(buku)

        assertEquals(buku.judul, "Kotlin Programming")
        assertEquals(buku.pengarang, "Bima Cakra")
        assertEquals(buku.tahunTerbit, 2024)
    }

    @Test
    fun testUpdateBuku() = runBlocking {
        val buku = Buku(id = 1, judul = "Kotlin Programming", pengarang = "Bima Cakra", tahunTerbit = 2024)
        bukuDao.insertBuku(buku)
        val insertedBukuId = buku.id

        val updatedBuku = buku.copy(judul = "Kitab Jarkom")
        bukuDao.updateBuku(updatedBuku)

        val retrievedBuku = bukuDao.getBukuById(insertedBukuId)
        assertEquals("Kitab Jarkom", retrievedBuku?.judul)
        assertEquals("Bima Cakra", retrievedBuku?.pengarang)
        assertEquals(2024, retrievedBuku?.tahunTerbit)
    }

    @Test
    fun testDeleteBuku() = runBlocking {
        val buku = Buku(id = 1, judul = "Kotlin Programming", pengarang = "Bima Cakra", tahunTerbit = 2024)
        bukuDao.insertBuku(buku)

        val retrieveBuku = bukuDao.getBukuById(buku.id)
        assertNotNull(retrieveBuku)
        assertEquals("Kotlin Programming", retrieveBuku?.judul)

        bukuDao.deleteBuku(buku)

        val deleteBuku = bukuDao.getBukuById(buku.id)
        assertNull(deleteBuku)
    }
}


