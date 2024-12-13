package com.project.BooksLibrary.ListFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.BooksLibrary.API.Repository.BookRepository
import com.project.BooksLibrary.API.RetrofitInstance
import com.project.BooksLibrary.R
import com.project.BooksLibrary.Database.AplikasiDatabase
import com.project.BooksLibrary.Adapter.BukuAdapter
import com.project.BooksLibrary.ViewModel.BookViewModel
import com.project.BooksLibrary.databinding.FragmentBooksBinding

class BooksFragment : Fragment() {

    private var _binding: FragmentBooksBinding? = null
    private val binding get() = _binding!!
    private lateinit var bukuAdapter: BukuAdapter
    private lateinit var booksViewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListBuku.layoutManager = LinearLayoutManager(requireContext())
        bukuAdapter = BukuAdapter {bookId -> onBookClick(bookId)}
        binding.rvListBuku.adapter = bukuAdapter

        val bookDao = AplikasiDatabase.getDatabase(requireContext()).bookDao()
        val bookRepository = BookRepository(RetrofitInstance.apiService, requireContext(), bookDao)
        booksViewModel = BookViewModel(bookRepository)

        errorMessage()
        observeViewModel()
        booksViewModel.fetchBooks()

        binding.btnAddBook.setOnClickListener {
            findNavController().navigate(R.id.action_booksFragment_to_addBookFragment3)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            booksViewModel.fetchBooks()
        }
    }
    private fun errorMessage() {
        booksViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun observeViewModel() {
        booksViewModel.booksLiveData.observe(viewLifecycleOwner) { books ->
            if (books != null) {
                bukuAdapter.submitBooksByGenre(books)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
    private fun onBookClick(bookId: Int) {
        val detailFragment = DetailBookFragment()
        val bundle = Bundle()
        bundle.putInt("BOOK_ID",bookId)
        detailFragment.arguments = bundle

        findNavController().navigate(R.id.action_booksFragment_to_detailBookFragment,bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}