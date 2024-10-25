package com.project.projectuts.addActivity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.databinding.ActivityAddPeminjamanBinding
import com.project.projectuts.model.Peminjaman
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.viewModel.PeminjamanViewModel
import androidx.lifecycle.Observer
import java.util.Calendar

class AddPeminjamanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPeminjamanBinding
    private lateinit var viewModel: PeminjamanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPeminjamanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editTextNamaPeminjam = binding.editTextNamaPeminjam
        val editTextBukuDipinjam = binding.editTextBukuDipinjam
        val editTextTanggalPinjam = binding.editTextTanggalPinjam
        val buttonAddPeminjaman = binding.buttonAddPeminjaman

        val peminjamanDao = AplikasiDatabase.getDatabase(this).peminjamanDao()
        viewModel = PeminjamanViewModel(peminjamanDao)

        buttonAddPeminjaman.setOnClickListener {
            addPeminjaman(
                editTextNamaPeminjam.text.toString().trim(),
                editTextBukuDipinjam.text.toString().trim(),
                editTextTanggalPinjam.text.toString().trim()
            )
        }
        binding.editTextTanggalPinjam.setOnClickListener {
            showDatePickerDialog()
        }

        viewModel.statusMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            clearInputs()
            finish()
        })
    }

    private fun addPeminjaman(namaPeminjam: String, bukuDipinjam: String, tglDipinjam: String) {
        if (namaPeminjam.isEmpty() || bukuDipinjam.isEmpty() || tglDipinjam.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua field", Toast.LENGTH_SHORT).show()
            return
        }

        val peminjaman = Peminjaman(
            namaPeminjam = namaPeminjam,
            bukuDipinjam = bukuDipinjam,
            tglDipinjam = tglDipinjam
        )

        viewModel.addPeminjaman(peminjaman)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.editTextTanggalPinjam.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun clearInputs() {
        binding.editTextNamaPeminjam.text.clear()
        binding.editTextBukuDipinjam.text.clear()
        binding.editTextTanggalPinjam.text.clear()
    }
}
