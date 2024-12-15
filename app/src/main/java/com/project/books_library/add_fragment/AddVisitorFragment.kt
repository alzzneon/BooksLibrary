package com.project.books_library.add_fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.books_library.R
import com.project.books_library.api.repository.VisitorRepository
import com.project.books_library.api.RetrofitInstance.apiService
import com.project.books_library.database.AplikasiDatabase
import com.project.books_library.databinding.FragmentAddVisitorBinding
import com.project.books_library.model.Visitors
import com.project.books_library.view_model.VisitorViewModel
import java.util.Calendar
import java.util.Locale

class AddVisitorFragment : Fragment() {

    private lateinit var binding: FragmentAddVisitorBinding
    private lateinit var visitorViewModel: VisitorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddVisitorBinding.inflate(inflater, container, false)

        val visitorDao = AplikasiDatabase.getDatabase(requireContext()).visitorDao()
        val visitorRepository = VisitorRepository(apiService, requireContext(), visitorDao)
        visitorViewModel = VisitorViewModel(visitorRepository)

        val genderArray = resources.getStringArray(R.array.gender_array)
        val genderAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genderArray)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = genderAdapter

        val purposeArray = resources.getStringArray(R.array.visit_purpose_array)
        val purposeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,purposeArray)
        purposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerVisitPurpose.adapter = purposeAdapter

        binding.editTextVisitDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.buttonAddVisitor.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val visitDate = binding.editTextVisitDate.text.toString()
            val gender = binding.spinnerGender.selectedItem.toString()
            val address = binding.editTextAddress.text.toString()
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            val email = binding.editTextEmail.text.toString()
            val visitPurpose = binding.spinnerVisitPurpose.selectedItem.toString()

            if (name.isEmpty() || visitDate.isEmpty() || gender.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || visitPurpose.isEmpty()) {
                showErrorMessages(name, visitDate, gender, address, phoneNumber)
                return@setOnClickListener
            }

            val visitor = Visitors(
                name = name,
                visit_date = visitDate,
                gender = gender,
                address = address,
                phone_number = phoneNumber,
                email = email,
                visit_purpose = visitPurpose
            )
            visitorViewModel.insertVisitor(visitor)
            visitorViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            Toast.makeText(requireContext(), "Pengunjung berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
//            visitorViewModel.fetchVisitors()
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
                binding.editTextVisitDate.setText(selectedDateTime)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun showErrorMessages(name: String, visitDate: String, address: String, phoneNumber: String, email: String) {
        if (name.isEmpty()) binding.editTextName.error = "Nama tidak boleh kosong"
        if (visitDate.isEmpty()) binding.editTextVisitDate.error = "Tanggal kunjungan tidak boleh kosong"
        if (address.isEmpty()) binding.editTextAddress.error = "Alamat tidak boleh kosong"
        if (phoneNumber.isEmpty()) binding.editTextPhoneNumber.error = "Nomor telepon tidak boleh kosong"
        if (email.isEmpty()) binding.editTextEmail.error = "Email tidak boleh kosong"
    }
}
