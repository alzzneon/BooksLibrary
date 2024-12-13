package com.project.BooksLibrary.Database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.BooksLibrary.Dao.BookDao
import com.project.BooksLibrary.Dao.PeminjamanDao
import com.project.BooksLibrary.Dao.VisitorDao
import com.project.BooksLibrary.Model.Book
import com.project.BooksLibrary.Model.Peminjaman
import com.project.BooksLibrary.Model.Visitors

@Database(entities = [Book::class, Peminjaman::class, Visitors::class], version = 1)
abstract class AplikasiDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun peminjamanDao(): PeminjamanDao
    abstract fun visitorDao(): VisitorDao

    companion object {
        @Volatile
        private var INSTANCE: AplikasiDatabase? = null

        fun getDatabase(context: Context): AplikasiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AplikasiDatabase::class.java,
                    "db_library"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
