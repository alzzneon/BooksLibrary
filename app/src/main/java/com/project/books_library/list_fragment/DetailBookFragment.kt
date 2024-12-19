package com.project.books_library.list_fragment

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.project.books_library.R
import com.project.books_library.api.RetrofitInstance
import com.project.books_library.api.repository.BookRepository
import com.project.books_library.database.AplikasiDatabase
import com.project.books_library.databinding.FragmentDetailBookBinding
import com.project.books_library.model.Book
import com.project.books_library.view_model.BookViewModel

class DetailBookFragment : Fragment() {

    private lateinit var bookViewModel: BookViewModel
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
            bookViewModel = BookViewModel(repository)

            observeViewModel()
            bookViewModel.fetchBookById(bookId)
        }
        binding.btnDelete.setOnClickListener {
            currentBook?.let { book ->
                showDeleteConfirmationDialog(requireContext(), book)
            }
        }

        binding.btnEdit.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("BOOK_ID", bookId)
            }
            findNavController().navigate(R.id.action_detailBookFragment_to_editBookFragment, bundle)
        }

    }

    private fun observeViewModel() {
        bookViewModel.bookLiveData.observe(viewLifecycleOwner) { book ->
            currentBook = book
            binding.tvDetailTitle.text = book.title
            binding.tvDetailGenre.text = "Genre: ${book.genre}"
            binding.tvDetailAuthor.text = "Pengarang: ${book.author}"
            binding.tvDetailYearPublish.text = "Tahun Terbit: ${book.year_publish}"
            binding.tvDetailDescription.text = book.description

            Glide.with(binding.ivDetailCover.context)
                .load(book.image_url)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_not_available)
                .into(binding.ivDetailCover)
        }

        bookViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.tvDetailDescription.text = errorMessage
        }
    }

    private fun showDeleteConfirmationDialog(context: Context, book: Book) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Konfirmasi Hapus")
        builder.setMessage("\nUntuk Menghapus Pastika Terhubung ke Internet\nApakah Anda yakin ingin menghapus ${book.title}?")
        builder.setPositiveButton("Ya") { dialog, _ ->
            bookViewModel.deleteBook(book)
            Toast.makeText(context, "${book.title} berhasil dihapus!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.booksFragment)
            dialog.dismiss()
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}
