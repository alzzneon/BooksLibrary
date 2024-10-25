package com.project.projectuts.list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.projectuts.R
import com.project.projectuts.adapter.PeminjamanAdapter
import com.project.projectuts.addActivity.AddPeminjamanActivity
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.databinding.ActivityListPeminjamanBinding
import com.project.projectuts.viewModel.PeminjamanViewModel

class ListPeminjamanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListPeminjamanBinding
    private lateinit var peminjamanAdapter: PeminjamanAdapter
    private lateinit var viewModel: PeminjamanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPeminjamanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val peminjamanDao = AplikasiDatabase.getDatabase(this).peminjamanDao()
        viewModel = PeminjamanViewModel(peminjamanDao)

        binding.recyclerViewPeminjam.layoutManager = LinearLayoutManager(this)

        peminjamanAdapter = PeminjamanAdapter(emptyList()) { peminjaman ->
            viewModel.deletePeminjaman(peminjaman)
        }
        binding.recyclerViewPeminjam.adapter = peminjamanAdapter

        viewModel.allPeminjaman.observe(this) { peminjamanList ->
            peminjamanAdapter.updatePeminjamanList(peminjamanList)
        }
        viewModel.statusMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        val addButton = findViewById<FloatingActionButton>(R.id.fab_add_peminjam)
        addButton.setOnClickListener {
            startActivity(Intent(this, AddPeminjamanActivity::class.java))
        }
    }
}
