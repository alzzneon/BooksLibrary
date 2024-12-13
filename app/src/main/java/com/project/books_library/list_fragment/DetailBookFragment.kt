package com.project.books_library.list_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.project.books_library.api.RetrofitInstance
import com.project.books_library.api.repository.BookRepository
import com.project.books_library.database.AplikasiDatabase
import com.project.books_library.databinding.FragmentDetailBookBinding
import com.project.books_library.view_model.BookViewModel

class DetailBookFragment : Fragment() {

    private lateinit var detailViewModel: BookViewModel
    private lateinit var binding: FragmentDetailBookBinding

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
    }

    private fun observeViewModel() {
        detailViewModel.bookLiveData.observe(viewLifecycleOwner) { book ->
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
}
