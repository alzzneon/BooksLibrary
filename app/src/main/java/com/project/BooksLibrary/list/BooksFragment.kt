package com.project.BooksLibrary.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.BooksLibrary.API.repository.BookRepository
import com.project.BooksLibrary.API.RetrofitInstance
import com.project.BooksLibrary.R
import com.project.BooksLibrary.database.AplikasiDatabase
import com.project.BooksLibrary.adapter.BukuAdapter
import com.project.BooksLibrary.viewModel.BukuViewModel
import com.project.BooksLibrary.databinding.FragmentBooksBinding

class BooksFragment : Fragment() {

    private var _binding: FragmentBooksBinding? = null
    private val binding get() = _binding!!
    private lateinit var bukuAdapter: BukuAdapter
    private lateinit var booksViewModel: BukuViewModel

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
        bukuAdapter = BukuAdapter()
        binding.rvListBuku.adapter = bukuAdapter

        val bookDao = AplikasiDatabase.getDatabase(requireContext()).bookDao()
        val bookRepository = BookRepository(RetrofitInstance.apiService, requireContext(), bookDao)
        booksViewModel = BukuViewModel(bookRepository)

        errorMessage()
        observeViewModel()
        booksViewModel.fetchBooks()

        binding.btnAddBook.setOnClickListener {
            findNavController().navigate(R.id.action_booksFragment_to_addBookFragment3)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            booksViewModel.refreshBooks()
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
                bukuAdapter.submitList(books)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
