package com.project.books_library.list_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.books_library.api.RetrofitInstance
import com.project.books_library.api.repository.BookRepository
import com.project.books_library.database.AplikasiDatabase
import com.project.books_library.databinding.FragmentEditBookBinding
import com.project.books_library.model.Book
import com.project.books_library.view_model.BookViewModel

class EditBookFragment : Fragment() {

    private lateinit var bookRepository: BookRepository
    private lateinit var bookViewModel: BookViewModel
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

        val bookDao = AplikasiDatabase.getDatabase(requireContext()).bookDao()
        bookRepository = BookRepository(RetrofitInstance.apiService, requireContext(), bookDao)
        bookViewModel = BookViewModel(bookRepository)

        bookId = arguments?.getInt("BOOK_ID", -1) ?: -1
        if (bookId != -1) {
            fetchBookData(bookId)
        }
        binding.buttonSaveBookEdit.setOnClickListener {
            updateBook()
        }
        bookViewModel.errorLiveData.observe(viewLifecycleOwner){ errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
        bookViewModel.bookLiveData.observe(viewLifecycleOwner) { book ->
            currentBook = book
            book?.let {
                binding.editTextEditBookTitle.setText(it.title)
                binding.editTextEditBookGenre.setText(it.genre)
                binding.editTextEditBookAuthor.setText(it.author)
                binding.editTextEditBookPublishYear.setText(it.year_publish.toString())
                binding.editTextEditBookDescription.setText(it.description)
                binding.editTextImageUrl.setText(it.image_url)
            }
        }
    }

    private fun fetchBookData(id: Int) {
        bookViewModel.fetchBookById(id)
    }

    private fun updateBook() {
        val updatedBook = currentBook?.copy(
            title = binding.editTextEditBookTitle.text.toString(),
            genre = binding.editTextEditBookGenre.text.toString(),
            author = binding.editTextEditBookAuthor.text.toString(),
            year_publish = binding.editTextEditBookPublishYear.text.toString().toIntOrNull() ?: 0,
            description = binding.editTextEditBookDescription.text.toString(),
            image_url = binding.editTextImageUrl.text.toString()
        )

        updatedBook?.let { book ->
            bookViewModel.updateBook(book)
            Toast.makeText(requireContext(), "Buku berhasil diupdate", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}
