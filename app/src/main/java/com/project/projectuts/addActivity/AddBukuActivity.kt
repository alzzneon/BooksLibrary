package com.project.projectuts.addActivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.API.BookRepository
import com.project.projectuts.API.RetrofitInstance.apiService
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.databinding.ActivityAddBukuBinding
import com.project.projectuts.model.Book
import com.project.projectuts.viewModel.BukuViewModel

class AddBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBukuBinding
    private lateinit var bookViewModel: BukuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookDao = AplikasiDatabase.getDatabase(this).bookDao()
        val bookRepository = BookRepository(apiService, this, bookDao)
        bookViewModel = BukuViewModel(bookRepository)
        binding.buttonAddBuku.setOnClickListener {
            val judul = binding.editTextJudulBuku.text.toString()
            val genre = binding.editTextGenre.text.toString()
            val pengarang = binding.editTextPengarang.text.toString()
            val tahunTerbitString = binding.editTextTahunTerbit.text.toString()
            val deskripsi = binding.editTextDeskripsi.text.toString()
            val imageUrl = binding.editTextImageUrl.text.toString()

            if (judul.isEmpty() || genre.isEmpty() || pengarang.isEmpty() || tahunTerbitString.isEmpty() || deskripsi.isEmpty() || imageUrl.isEmpty()) {
                showErrorMessages(judul, genre, pengarang, tahunTerbitString, deskripsi, imageUrl)
                return@setOnClickListener
            }

            val tahunTerbit = tahunTerbitString.toIntOrNull()
            if (tahunTerbit == null || tahunTerbit <= 0) {
                binding.editTextTahunTerbit.error = "Tahun terbit tidak valid"
                return@setOnClickListener
            }

            val book = Book (
                title = judul,
                genre = genre,
                author = pengarang,
                year_publish = tahunTerbit,
                description = deskripsi,
                image_url = imageUrl
            )
            bookViewModel.insertBuku(book)
            bookViewModel.errorLiveData.observe(this) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            bookViewModel.booksLiveData.observe(this) { books ->
                if (books.isNotEmpty()) {
                    Toast.makeText(this, "Buku berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun showErrorMessages(judul: String, genre: String, pengarang: String, tahunTerbitString: String, deskripsi: String, imageUrl: String) {
        if (judul.isEmpty()) binding.editTextJudulBuku.error = "Judul tidak boleh kosong"
        if (genre.isEmpty()) binding.editTextGenre.error = "Genre tidak boleh kosong"
        if (pengarang.isEmpty()) binding.editTextPengarang.error = "Pengarang tidak boleh kosong"
        if (tahunTerbitString.isEmpty()) binding.editTextTahunTerbit.error = "Tahun terbit tidak boleh kosong"
        if (deskripsi.isEmpty()) binding.editTextDeskripsi.error = "Deskripsi tidak boleh kosong"
        if (imageUrl.isEmpty()) binding.editTextImageUrl.error = "URL gambar tidak boleh kosong"
    }
}
