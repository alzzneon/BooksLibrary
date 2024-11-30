package com.project.projectuts.addActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.databinding.ActivityAddBukuBinding
import com.project.projectuts.model.Book
import com.project.projectuts.viewModel.BukuViewModel

class AddBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBukuBinding
//    private lateinit var bookViewModel: BukuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        val bookDao = AplikasiDatabase.getDatabase(this).bookDao()
//        bookViewModel = BukuViewModel(bookDao)
//
//        binding.buttonAddBuku.setOnClickListener {
//            val judul = binding.editTextJudulBuku.text.toString()
//            val genre = binding.editTextGenre.text.toString()
//            val pengarang = binding.editTextPengarang.text.toString()
//            val tahunTerbitString = binding.editTextTahunTerbit.text.toString()
//
//            if (judul.isEmpty() || genre.isEmpty() || pengarang.isEmpty() || tahunTerbitString.isEmpty()) {
//                binding.editTextJudulBuku.error = "Judul tidak boleh kosong"
//                binding.editTextGenre.error = "Genre tidak boleh kosong"
//                binding.editTextPengarang.error = "Pengarang tidak boleh kosong"
//                binding.editTextTahunTerbit.error = "Tahun terbit tidak boleh kosong"
//                return@setOnClickListener
//            }
//
//            val tahunTerbit = tahunTerbitString.toIntOrNull()
//            if (tahunTerbit == null || tahunTerbit <= 0) {
//                binding.editTextTahunTerbit.error = "Tahun terbit tidak valid"
//                return@setOnClickListener
//            }
//
//            val book = Book(judul = judul, genre = genre, pengarang = pengarang, tahunTerbit = tahunTerbit)
//            viewModel.insertBuku(book)
//            finish()
//        }
    }
}
