package com.project.projectuts.addActivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.R
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.databinding.ActivityAddPeminjamanBinding
import com.project.projectuts.model.Peminjaman
import com.project.projectuts.factory.PeminjamanViewModelFactory
import com.project.projectuts.viewmodel.PeminjamanViewModel

class AddPeminjamanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPeminjamanBinding
    private val viewModel: PeminjamanViewModel by viewModels {
        PeminjamanViewModelFactory((application as AplikasiDatabase).peminjamanDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPeminjamanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menghubungkan elemen UI
        val editTextNamaPeminjam = binding.editTextNamaPeminjam
        val editTextBukuDipinjam = binding.editTextBukuDipinjam
        val editTextTanggalPinjam = binding.editTextTanggalPinjam
        val buttonAddPeminjaman = binding.buttonAddPeminjaman

        // Menangani klik tombol
        buttonAddPeminjaman.setOnClickListener {
            addPeminjaman(editTextNamaPeminjam.text.toString().trim(),
                editTextBukuDipinjam.text.toString().trim(),
                editTextTanggalPinjam.text.toString().trim())
        }

        // Mengamati status pesan
        viewModel.statusMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            clearInputs()
        }
    }

    private fun addPeminjaman(namaPeminjam: String, bukuDipinjam: String, tglDipinjam: String) {
        // Validasi input
        if (namaPeminjam.isEmpty() || bukuDipinjam.isEmpty() || tglDipinjam.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua field", Toast.LENGTH_SHORT).show()
            return
        }

        // Membuat objek Peminjaman dan menambahkannya ke database
        val peminjaman = Peminjaman(
            namaPeminjam = namaPeminjam,
            bukuDipinjam = bukuDipinjam,
            tglDipinjam = tglDipinjam
        )

        // Tambahkan peminjaman ke ViewModel
        viewModel.addPeminjaman(peminjaman)
    }

    private fun clearInputs() {
        binding.editTextNamaPeminjam.text.clear()
        binding.editTextBukuDipinjam.text.clear()
        binding.editTextTanggalPinjam.text.clear()
    }
}
