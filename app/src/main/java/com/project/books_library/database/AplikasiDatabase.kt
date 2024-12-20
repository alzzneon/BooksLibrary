package com.project.books_library.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.books_library.dao.BookDao
import com.project.books_library.dao.LendingDao
import com.project.books_library.dao.VisitorDao
import com.project.books_library.model.Book
import com.project.books_library.model.Lending
import com.project.books_library.model.Visitors

@Database(entities = [Book::class, Visitors::class, Lending::class], version = 1)
abstract class AplikasiDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun lendingDao(): LendingDao
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
