package com.project.projectuts

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.project.projectuts.dao.PengunjungDao
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.model.Pengunjung
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PengunjungDaoTest {
    private lateinit var database: AplikasiDatabase
    private lateinit var pengunjungDao: PengunjungDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AplikasiDatabase::class.java
        ).build()
        pengunjungDao = database.pengunjungDao()
    }

    @After
    fun clodeDB() {
        database.close()
    }
    @Test
    fun testInsertBuku() = runBlocking {
        val pengunjung = Pengunjung(nama= "Alya Azzahra", tanggalKunjungan = "2024")

        pengunjungDao.insertPengunjung(pengunjung)

        assertEquals(pengunjung.nama, "Alya Azzahra")
        assertEquals(pengunjung.tanggalKunjungan, "2024")
    }
    @Test
    fun testDeleteBuku() = runBlocking {
        val pengunjung = Pengunjung(id = 1, nama= "Alya Azzahra", tanggalKunjungan = "2024")
        pengunjungDao.insertPengunjung(pengunjung)

        val retrieveBuku = pengunjungDao.getPengunjungById(pengunjung.id)
        assertNotNull(retrieveBuku)
        assertEquals("Alya Azzahra", retrieveBuku?.nama)

        pengunjungDao.deletePengunjung(pengunjung)

        val deleteBuku = pengunjungDao.getPengunjungById(pengunjung.id)
        assertNull(deleteBuku)
    }
}
