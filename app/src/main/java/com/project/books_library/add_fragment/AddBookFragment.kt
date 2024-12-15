package com.project.books_library.add_fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.books_library.api.repository.BookRepository
import com.project.books_library.api.RetrofitInstance.apiService
import com.project.books_library.database.AplikasiDatabase
import com.project.books_library.databinding.FragmentAddBookBinding
import com.project.books_library.model.Book
import com.project.books_library.view_model.BookViewModel

class AddBookFragment : Fragment() {

    private lateinit var binding: FragmentAddBookBinding
    private lateinit var bookViewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBookBinding.inflate(inflater, container, false)

        val bookDao = AplikasiDatabase.getDatabase(requireContext()).bookDao()
        val bookRepository = BookRepository(apiService, requireContext(), bookDao)
        bookViewModel = BookViewModel(bookRepository)

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

            val book = Book(
                title = judul,
                genre = genre,
                author = pengarang,
                year_publish = tahunTerbit,
                description = deskripsi,
                image_url = imageUrl
            )
            bookViewModel.insertBuku(book)
            bookViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            Toast.makeText(requireContext(), "Buku berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
        return binding.root
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
