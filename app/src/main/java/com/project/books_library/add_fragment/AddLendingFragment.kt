package com.project.books_library.add_fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.books_library.api.RetrofitInstance.apiService
import com.project.books_library.api.repository.BookRepository
import com.project.books_library.api.repository.LendingRepository
import com.project.books_library.api.repository.VisitorRepository
import com.project.books_library.database.AplikasiDatabase
import com.project.books_library.databinding.FragmentAddLendingBinding
import com.project.books_library.model.Lending
import com.project.books_library.view_model.BookViewModel
import com.project.books_library.view_model.LendingViewModel
import com.project.books_library.view_model.VisitorViewModel
import java.util.Calendar
import java.util.Locale

class AddLendingFragment : Fragment() {

    private lateinit var binding: FragmentAddLendingBinding
    private lateinit var bookSpinner: Spinner
    private lateinit var visitorSpinner: Spinner
    private lateinit var bookViewModel: BookViewModel
    private lateinit var visitorsViewModel: VisitorViewModel
    private lateinit var lendingViewModel: LendingViewModel
    private lateinit var activeEditText: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddLendingBinding.inflate(inflater, container, false)

        bookSpinner = binding.spinnerBookTitle
        visitorSpinner = binding.spinnerLendingName

        val database = AplikasiDatabase.getDatabase(requireContext())
        val bookRepository = BookRepository(apiService, requireContext(), database.bookDao())
        val visitorsRepository = VisitorRepository(apiService, requireContext(), database.visitorDao())
        val lendingDao = database.lendingDao()
        val lendingRepository = LendingRepository(apiService, requireContext(), lendingDao)

        bookViewModel = BookViewModel(bookRepository)
        visitorsViewModel = VisitorViewModel(visitorsRepository)
        lendingViewModel = LendingViewModel(lendingRepository)

        bookViewModel.bookTitles.observe(viewLifecycleOwner) { titles ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, titles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bookSpinner.adapter = adapter
        }
        visitorsViewModel.visitorNames.observe(viewLifecycleOwner) { names ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            visitorSpinner.adapter = adapter
        }

        binding.editTextBorrowDate.setOnClickListener {
            activeEditText = binding.editTextBorrowDate
            showDatePickerDialog()
        }

        binding.buttonSubmit.setOnClickListener {
            val visitor_name = visitorSpinner.selectedItem.toString()
            val book_title = bookSpinner.selectedItem.toString()
            val lending_date = binding.editTextBorrowDate.text.toString()
            val status = "Borrowed"

            val lending = Lending(
                visitor_name = visitor_name,
                book_title = book_title,
                lending_date = lending_date,
                return_date = null,
                status = status

            )
            lendingViewModel.insertLending(lending)
            lendingViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
            }
            Toast.makeText(requireContext(), "Peminjam berhasil ditambahkan!", Toast.LENGTH_LONG).show()
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)
                val selectedDateTime = String.format(
                    Locale.US,
                    "%04d-%02d-%02dT%02d:%02d:00.000Z",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay,
                    currentHour,
                    currentMinute
                )
                (activeEditText as? EditText)?.setText(selectedDateTime)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}