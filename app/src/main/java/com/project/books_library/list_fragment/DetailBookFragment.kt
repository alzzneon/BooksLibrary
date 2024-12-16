package com.project.books_library.list_fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.project.books_library.api.RetrofitInstance
import com.project.books_library.api.repository.BookRepository
import com.project.books_library.database.AplikasiDatabase
import com.project.books_library.databinding.FragmentDetailBookBinding
import com.project.books_library.model.Book
import com.project.books_library.view_model.BookViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailBookFragment : Fragment() {

    private lateinit var detailViewModel: BookViewModel
    private lateinit var binding: FragmentDetailBookBinding
    private var currentBook: Book? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookId = arguments?.getInt("BOOK_ID", -1) ?: -1
        if (bookId != -1) {
            val bookDao = AplikasiDatabase.getDatabase(requireContext()).bookDao()
            val repository = BookRepository(RetrofitInstance.apiService, requireContext(), bookDao)
            detailViewModel = BookViewModel(repository)

            observeViewModel()
            detailViewModel.fetchBookById(bookId)
        }

        // Set listener untuk tombol DELETE
        binding.btnDelete.setOnClickListener {
            currentBook?.let { deleteBook(it) }
        }

    }

    private fun observeViewModel() {
        detailViewModel.bookLiveData.observe(viewLifecycleOwner) { book ->
            currentBook = book // Simpan data buku untuk digunakan saat delete
            binding.tvDetailTitle.text = book.title
            binding.tvDetailGenre.text = "Genre: ${book.genre}"
            binding.tvDetailAuthor.text = "Pengarang: ${book.author}"
            binding.tvDetailYearPublish.text = "Tahun Terbit: ${book.year_publish}"
            binding.tvDetailDescription.text = book.description

            Glide.with(binding.ivDetailCover.context)
                .load(book.image_url)
                .into(binding.ivDetailCover)
        }

        detailViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.tvDetailDescription.text = errorMessage
        }
    }

    private fun deleteBook(book: Book) {
        val bookDao = AplikasiDatabase.getDatabase(requireContext()).bookDao()
        val repository = BookRepository(RetrofitInstance.apiService, requireContext(), bookDao)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.deleteBook(book)
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "Buku berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack() // Kembali ke daftar buku
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "Gagal menghapus buku: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
