package com.project.books_library.list_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.books_library.R
import com.project.books_library.adapter.LendingAdapter
import com.project.books_library.api.RetrofitInstance
import com.project.books_library.api.repository.LendingRepository
import com.project.books_library.database.AplikasiDatabase
import com.project.books_library.databinding.FragmentLendingBinding
import com.project.books_library.model.Lending
import com.project.books_library.view_model.LendingViewModel


class LendingFragment : Fragment() {

    private var _binding: FragmentLendingBinding? = null
    private val binding get() = _binding!!

    private lateinit var lendingAdapter: LendingAdapter
    private lateinit var lendingViewModel: LendingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListPeminjaman.layoutManager = LinearLayoutManager(requireContext())
        lendingAdapter = LendingAdapter(requireContext(), { lending ->
            deleteLending(lending)
        } , { lending ->
            editLending(lending)
        })
        binding.rvListPeminjaman.adapter = lendingAdapter

        val lendingDao = AplikasiDatabase.getDatabase(requireContext()).lendingDao()
        val lendingRepository = LendingRepository(RetrofitInstance.apiService, requireContext(), lendingDao)
        lendingViewModel = LendingViewModel(lendingRepository)


        observerViewModel()
        binding.btnAddBorrow.setOnClickListener {
            findNavController().navigate(R.id.action_lendingFragment_to_addLendingFragment)
        }
    }

    private fun observerViewModel() {
        lendingViewModel.lendingLiveData.observe(viewLifecycleOwner) { lending ->
            lending?.let {
                lendingAdapter.submitList(lending)
            }
        }

        lendingViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteLending(lending: Lending) {
        lendingViewModel.deleteLending(lending)
    }

    private fun editLending(lending: Lending) {
        lendingViewModel.editLending(lending)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}