package com.project.projectuts

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.databinding.ActivityAddPengunjungBinding

class AddPengunjungActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPengunjungBinding
    private val viewModel: PengunjungViewModel by viewModels {
        PengunjungViewModelFactory(PengunjungRepository(AplikasiDatabase.getDatabase(this).pengunjungDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPengunjungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddPengunjung.setOnClickListener {
            val nama = binding.editTextNamaPengunjung.text.toString()
            val tanggalKunjungan = binding.editTextTanggalKunjungan.text.toString()

            // Validasi input sebelum menambahkan pengunjung
            if (nama.isNotBlank() && tanggalKunjungan.isNotBlank()) {
                val pengunjung = Pengunjung(nama = nama, tanggalKunjungan = tanggalKunjungan)
                viewModel.insertPengunjung(pengunjung)
                finish() // Kembali ke layar sebelumnya
            } else {
                // Tampilkan pesan kesalahan jika input tidak valid
                Toast.makeText(this, "Nama dan Tanggal Kunjungan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
