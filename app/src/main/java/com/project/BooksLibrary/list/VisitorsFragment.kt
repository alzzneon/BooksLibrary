package com.project.BooksLibrary.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.BooksLibrary.API.RetrofitInstance
import com.project.BooksLibrary.API.repository.VisitorRepository
import com.project.BooksLibrary.database.AplikasiDatabase
import com.project.BooksLibrary.databinding.FragmentVisitorsBinding
import com.project.BooksLibrary.viewModel.VisitorViewModel
import com.project.BooksLibrary.adapter.VisitorAdapter

class VisitorsFragment : Fragment() {

    private var _binding: FragmentVisitorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var visitorAdapter: VisitorAdapter
    private lateinit var visitorViewModel: VisitorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvVisitors.layoutManager = LinearLayoutManager(requireContext())
        visitorAdapter = VisitorAdapter()
        binding.rvVisitors.adapter = visitorAdapter

        val visitorDao = AplikasiDatabase.getDatabase(requireContext()).visitorDao()
        val visitorRepository = VisitorRepository(RetrofitInstance.apiService, requireContext(), visitorDao)
        visitorViewModel = VisitorViewModel(visitorRepository)

        observeViewModel()
        visitorViewModel.fetchVisitors()
    }

    private fun observeViewModel() {
        visitorViewModel.visitorsLiveData.observe(viewLifecycleOwner) { visitors ->
            if (visitors != null) {
                visitorAdapter.submitList(visitors)
            }
        }
        visitorViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
