package com.project.books_library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.project.books_library.api.RetrofitInstance
import com.project.books_library.api.repository.BookRepository
import com.project.books_library.database.AplikasiDatabase
import com.project.books_library.databinding.FragmentEditBookBinding
import com.project.books_library.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditBookFragment : Fragment() {

    private lateinit var binding: FragmentEditBookBinding
    private var currentBook: Book? = null
    private var bookId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil argument (Book ID) yang dikirim dari DetailBookFragment
        bookId = arguments?.getInt("BOOK_ID", -1) ?: -1

        if (bookId != -1) {
            fetchBookData(bookId)
        }

        // Setup tombol untuk menyimpan perubahan buku
        binding.buttonSaveBookEdit.setOnClickListener {
            updateBook()
        }
    }

    private fun fetchBookData(id: Int) {
        val bookDao = AplikasiDatabase.getDatabase(requireContext()).bookDao()
        val repository = BookRepository(RetrofitInstance.apiService, requireContext(), bookDao)

        lifecycleScope.launch(Dispatchers.IO) {
            val book = repository.getBookById(id) // Ambil buku berdasarkan ID
            lifecycleScope.launch(Dispatchers.Main) {
                book?.let {
                    currentBook = it
                    // Set data ke input form
                    binding.editTextEditBookTitle.setText(it.title)
                    binding.editTextEditBookGenre.setText(it.genre)
                    binding.editTextEditBookAuthor.setText(it.author)
                    binding.editTextEditBookPublishYear.setText(it.year_publish.toString())
                    binding.editTextEditBookDescription.setText(it.description)
                    binding.editTextImageUrl.setText(it.image_url)
                }
            }
        }
    }

    private fun updateBook() {
        val bookDao = AplikasiDatabase.getDatabase(requireContext()).bookDao()
        val repository = BookRepository(RetrofitInstance.apiService, requireContext(), bookDao)

        // Ambil input dari user
        val updatedBook = currentBook?.copy(
            title = binding.editTextEditBookTitle.text.toString(),
            genre = binding.editTextEditBookGenre.text.toString(),
            author = binding.editTextEditBookAuthor.text.toString(),
            year_publish = binding.editTextEditBookPublishYear.text.toString().toIntOrNull() ?: 0,
            description = binding.editTextEditBookDescription.text.toString(),
            image_url = binding.editTextImageUrl.text.toString()
        )

        // Update buku ke database
        updatedBook?.let { book ->
            lifecycleScope.launch(Dispatchers.IO) {
                repository.updateBook(book)
                lifecycleScope.launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Buku berhasil diupdate", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack() // Navigasi kembali
                }
            }
        }
    }
}
