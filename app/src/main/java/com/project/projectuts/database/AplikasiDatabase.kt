package com.project.projectuts.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.projectuts.dao.BookDao
import com.project.projectuts.dao.PeminjamanDao
import com.project.projectuts.dao.PengunjungDao
import com.project.projectuts.model.Book
import com.project.projectuts.model.Peminjaman
import com.project.projectuts.model.Pengunjung

@Database(entities = [Book::class, Peminjaman::class, Pengunjung::class], version = 1)
abstract class AplikasiDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun peminjamanDao(): PeminjamanDao
    abstract fun pengunjungDao(): PengunjungDao

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
