package com.project.BooksLibrary

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import com.project.BooksLibrary.dao.BookDao
import com.project.BooksLibrary.database.AplikasiDatabase
import com.project.BooksLibrary.model.Book
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull

@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var database: AplikasiDatabase
    private lateinit var bookDao: BookDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AplikasiDatabase::class.java
        ).build()
        bookDao = database.bukuDao()
    }

    @After
    fun clodeDB() {
        database.close()
    }

    @Test
    fun testInsertBuku() = runBlocking {
        val book = Book(judul = "Kotlin Programming", pengarang = "Bima Cakra", tahunTerbit = 2024)

        bookDao.insertBuku(book)

        assertEquals(book.judul, "Kotlin Programming")
        assertEquals(book.pengarang, "Bima Cakra")
        assertEquals(book.tahunTerbit, 2024)
    }

    @Test
    fun testUpdateBuku() = runBlocking {
        val book = Book(id = 1, judul = "Kotlin Programming", pengarang = "Bima Cakra", tahunTerbit = 2024)
        bookDao.insertBuku(book)
        val insertedBukuId = book.id

        val updatedBuku = book.copy(judul = "Kitab Jarkom")
        bookDao.updateBuku(updatedBuku)

        val retrievedBuku = bookDao.getBukuById(insertedBukuId)
        assertEquals("Kitab Jarkom", retrievedBuku?.judul)
        assertEquals("Bima Cakra", retrievedBuku?.pengarang)
        assertEquals(2024, retrievedBuku?.tahunTerbit)
    }

    @Test
    fun testDeleteBuku() = runBlocking {
        val book = Book(id = 1, judul = "Kotlin Programming", pengarang = "Bima Cakra", tahunTerbit = 2024)
        bookDao.insertBuku(book)

        val retrieveBuku = bookDao.getBukuById(book.id)
        assertNotNull(retrieveBuku)
        assertEquals("Kotlin Programming", retrieveBuku?.judul)

        bookDao.deleteBuku(book)

        val deleteBuku = bookDao.getBukuById(book.id)
        assertNull(deleteBuku)
    }
}


