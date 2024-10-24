package com.project.projectuts.addActivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.databinding.ActivityAddPeminjamanBinding
import com.project.projectuts.model.Peminjaman
import com.project.projectuts.factory.PeminjamanViewModelFactory
import com.project.projectuts.viewmodel.PeminjamanViewModel
import com.project.projectuts.database.AplikasiDatabase

class AddPeminjamanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPeminjamanBinding
    private val viewModel: PeminjamanViewModel by viewModels {
        PeminjamanViewModelFactory(AplikasiDatabase.getDatabase(this).peminjamanDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPeminjamanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editTextNamaPeminjam = binding.editTextNamaPeminjam
        val editTextBukuDipinjam = binding.editTextBukuDipinjam
        val editTextTanggalPinjam = binding.editTextTanggalPinjam
        val buttonAddPeminjaman = binding.buttonAddPeminjaman

        buttonAddPeminjaman.setOnClickListener {
            addPeminjaman(
                editTextNamaPeminjam.text.toString().trim(),
                editTextBukuDipinjam.text.toString().trim(),
                editTextTanggalPinjam.text.toString().trim()
            )
        }

        viewModel.statusMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            clearInputs()
        }
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

    private fun clearInputs() {
        binding.editTextNamaPeminjam.text.clear()
        binding.editTextBukuDipinjam.text.clear()
        binding.editTextTanggalPinjam.text.clear()
    }
}
