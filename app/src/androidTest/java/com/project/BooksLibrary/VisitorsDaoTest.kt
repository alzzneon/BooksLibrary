package com.project.BooksLibrary

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.project.BooksLibrary.dao.VisitorDao
import com.project.BooksLibrary.database.AplikasiDatabase
import com.project.BooksLibrary.model.Visitors
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VisitorsDaoTest {
    private lateinit var database: AplikasiDatabase
    private lateinit var visitorDao: VisitorDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AplikasiDatabase::class.java
        ).build()
        visitorDao = database.pengunjungDao()
    }

    @After
    fun clodeDB() {
        database.close()
    }
    @Test
    fun testInsertPengunjung() = runBlocking {
        val pengunjung = Visitors(nama= "Alya Azzahra", tanggalKunjungan = "2024")

        visitorDao.insertPengunjung(pengunjung)

        assertEquals(pengunjung.nama, "Alya Azzahra")
        assertEquals(pengunjung.tanggalKunjungan, "2024")
    }
    @Test
    fun testDeletePengunjung() = runBlocking {
        val pengunjung = Visitors(id = 1, nama= "Alya Azzahra", tanggalKunjungan = "2024")
        visitorDao.insertPengunjung(pengunjung)

        val retrieveBuku = visitorDao.getVisitorById(pengunjung.id)
        assertNotNull(retrieveBuku)
        assertEquals("Alya Azzahra", retrieveBuku?.nama)

        visitorDao.deletePengunjung(pengunjung)

        val deleteBuku = visitorDao.getVisitorById(pengunjung.id)
        assertNull(deleteBuku)
    }
    @Test
    fun testUpdatePengunjung() = runBlocking {
        val pengunjung = Visitors(id = 1, nama= "Mohamad Arung", tanggalKunjungan = "2024")
        visitorDao.insertPengunjung(pengunjung)
        val insertedPengunjungId = pengunjung.id

        val updatedPengunjung = pengunjung.copy(nama = "Alya Azzahra")
        visitorDao.updatePengunjung(updatedPengunjung)

        val retrievedPengunjung = visitorDao.getVisitorById(insertedPengunjungId)
        assertEquals("Alya Azzahra", retrievedPengunjung?.nama)
        assertEquals("2024", retrievedPengunjung?.tanggalKunjungan)
    }
}
