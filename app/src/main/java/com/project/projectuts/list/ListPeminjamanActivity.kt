package com.project.projectuts.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.R
import com.project.projectuts.adapter.PeminjamanAdapter
import com.project.projectuts.addActivity.AddPeminjamanActivity
import com.project.projectuts.databinding.ActivityListPeminjamanBinding
import com.project.projectuts.factory.PeminjamanViewModelFactory
import com.project.projectuts.dao.PeminjamanDao
import com.project.projectuts.viewmodel.PeminjamanViewModel // Pastikan ini diimpor

class ListPeminjamanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListPeminjamanBinding
    private lateinit var peminjamanAdapter: PeminjamanAdapter
    private val viewModel: PeminjamanViewModel by viewModels {
        PeminjamanViewModelFactory(AplikasiDatabase.getDatabase(this).peminjamanDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPeminjamanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewPeminjam.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty list
        peminjamanAdapter = PeminjamanAdapter(emptyList()) { peminjaman ->
            viewModel.deletePeminjaman(peminjaman)
        }
        binding.recyclerViewPeminjam.adapter = peminjamanAdapter

        // Observe LiveData
        viewModel.allPeminjaman.observe(this) { peminjamanList ->
            peminjamanAdapter.updatePeminjamanList(peminjamanList)
        }

        val addButton = findViewById<FloatingActionButton>(R.id.fab_add_peminjam)
        addButton.setOnClickListener {
            startActivity(Intent(this, AddPeminjamanActivity::class.java))
        }
    }
}
