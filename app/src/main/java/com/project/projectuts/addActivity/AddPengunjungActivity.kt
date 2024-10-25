package com.project.projectuts.addActivity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.databinding.ActivityAddPengunjungBinding
import com.project.projectuts.model.Pengunjung
import com.project.projectuts.viewModel.PengunjungViewModel
import androidx.lifecycle.Observer
import java.util.Calendar

class AddPengunjungActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPengunjungBinding
    private lateinit var viewModel: PengunjungViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPengunjungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pengunjungDao = AplikasiDatabase.getDatabase(this).pengunjungDao()
        viewModel = PengunjungViewModel(pengunjungDao)

        binding.editTextTanggalKunjungan.setOnClickListener {
            showDatePickerDialog()
        }

        binding.buttonAddPengunjung.setOnClickListener {
            val nama = binding.editTextNamaPengunjung.text.toString()
            val tanggalKunjungan = binding.editTextTanggalKunjungan.text.toString()

            if (nama.isNotBlank() && tanggalKunjungan.isNotBlank()) {
                val pengunjung = Pengunjung(nama = nama, tanggalKunjungan = tanggalKunjungan)
                viewModel.insertPengunjung(pengunjung)
                finish()
            } else {
                Toast.makeText(this, "Nama dan Tanggal Kunjungan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.statusMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            clearInputs()
            finish()
        })
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
                binding.editTextTanggalKunjungan.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun clearInputs() {
        binding.editTextNamaPengunjung.text.clear()
        binding.editTextTanggalKunjungan.text.clear()
    }
}
