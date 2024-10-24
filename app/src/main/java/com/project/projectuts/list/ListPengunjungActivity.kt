package com.project.projectuts.list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.projectuts.addActivity.AddPengunjungActivity
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.factory.PengunjungViewModelFactory
import com.project.projectuts.R
import com.project.projectuts.adapter.PengunjungAdapter
import com.project.projectuts.databinding.ActivityListPengunjungBinding
import com.project.projectuts.repository.PengunjungRepository
import com.project.projectuts.viewModel.PengunjungViewModel

class ListPengunjungActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListPengunjungBinding
    private lateinit var pengunjungAdapter: PengunjungAdapter
    private val viewModel: PengunjungViewModel by viewModels {
        PengunjungViewModelFactory(PengunjungRepository(AplikasiDatabase.getDatabase(this).pengunjungDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPengunjungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewPengunjung.layoutManager = LinearLayoutManager(this)

        viewModel.allPengunjung.observe(this, { pengunjungList ->
            pengunjungAdapter = PengunjungAdapter(pengunjungList,
                onDeleteClick = { pengunjung -> viewModel.deletePengunjung(pengunjung) }
            )
            binding.recyclerViewPengunjung.adapter = pengunjungAdapter
        })
        viewModel.statusMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        val addButton = findViewById<FloatingActionButton>(R.id.fab_add_pengunjung)
        addButton.setOnClickListener {
            startActivity(Intent(this, AddPengunjungActivity::class.java))
        }
    }
}
