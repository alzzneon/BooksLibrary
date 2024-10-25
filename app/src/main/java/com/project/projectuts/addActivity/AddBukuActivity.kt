package com.project.projectuts.addActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.databinding.ActivityAddBukuBinding
import com.project.projectuts.model.Buku
import com.project.projectuts.viewModel.BukuViewModel

class AddBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBukuBinding

    private lateinit var viewModel: BukuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bukuDao = AplikasiDatabase.getDatabase(this).bukuDao()
        viewModel = BukuViewModel(bukuDao)

        binding.buttonAddBuku.setOnClickListener {
            val judul = binding.editTextJudul.text.toString()
            val pengarang = binding.editTextPengarang.text.toString()
            val tahunTerbit = binding.editTextTahunTerbit.text.toString().toIntOrNull() ?: 0

            val buku = Buku(judul = judul, pengarang = pengarang, tahunTerbit = tahunTerbit)
            viewModel.insertBuku(buku)
            finish()
        }
    }
}
