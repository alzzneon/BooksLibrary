package com.project.books_library.list_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.books_library.R
import com.project.books_library.api.RetrofitInstance
import com.project.books_library.api.repository.VisitorRepository
import com.project.books_library.database.AplikasiDatabase
import com.project.books_library.view_model.VisitorViewModel
import com.project.books_library.adapter.VisitorAdapter
import com.project.books_library.databinding.FragmentVisitorsBinding
import com.project.books_library.model.Visitors

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

        visitorAdapter = VisitorAdapter(requireContext(), { visitor ->
            deleteVisitor(visitor)
        }, { visitor ->
            editVisitor(visitor)
        })
        binding.rvVisitors.adapter = visitorAdapter

        val visitorDao = AplikasiDatabase.getDatabase(requireContext()).visitorDao()
        val visitorRepository = VisitorRepository(RetrofitInstance.apiService, requireContext(), visitorDao)
        visitorViewModel = VisitorViewModel(visitorRepository)

        observeViewModel()
        binding.addVisitor.setOnClickListener {
            findNavController().navigate(R.id.action_visitorsFragment_to_addVisitorFragment2)
        }
    }

    private fun observeViewModel() {
        visitorViewModel.visitorsLiveData.observe(viewLifecycleOwner) { visitors ->
            visitors?.let {
                visitorAdapter.submitList(it)
            }
        }

        visitorViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteVisitor(visitor: Visitors) {
        visitorViewModel.deleteVisitor(visitor)
    }

    private fun editVisitor(visitor: Visitors) {
        visitorViewModel.editVisitor(visitor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}