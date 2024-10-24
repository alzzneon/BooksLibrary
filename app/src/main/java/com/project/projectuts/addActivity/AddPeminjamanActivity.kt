package com.project.projectuts.addActivity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.databinding.ActivityAddPeminjamanBinding
import com.project.projectuts.model.Peminjaman
import com.project.projectuts.viewModel.PeminjamanViewModel

class AddPeminjamanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPeminjamanBinding
    private val viewModel: PeminjamanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPeminjamanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddPeminjaman.setOnClickListener {
            val pengunjungId = binding.editTextPengunjungId.text.toString().toInt()
            val bukuId = binding.editTextBukuId.text.toString().toInt()
            val tanggalPinjam = binding.editTextTanggalPinjam.text.toString()

            val peminjaman = Peminjaman(pengunjungId = pengunjungId, bukuId = bukuId, tanggalPinjam = tanggalPinjam)
            viewModel.insertPeminjaman(peminjaman)
            finish() // Kembali ke layar sebelumnya
        }
    }
}
