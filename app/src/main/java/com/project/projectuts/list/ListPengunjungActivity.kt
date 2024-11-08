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
import com.project.projectuts.R
import com.project.projectuts.adapter.PengunjungAdapter
import com.project.projectuts.databinding.ActivityListPengunjungBinding
import com.project.projectuts.viewModel.PengunjungViewModel

class ListPengunjungActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListPengunjungBinding
    private lateinit var pengunjungAdapter: PengunjungAdapter
    private lateinit var viewModel: PengunjungViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPengunjungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pengunjungDao = AplikasiDatabase.getDatabase(this).pengunjungDao()
        viewModel = PengunjungViewModel(pengunjungDao)

        binding.recyclerViewPengunjung.layoutManager = LinearLayoutManager(this)

        viewModel.allPengunjung.observe(this) { pengunjungList ->
            pengunjungAdapter.submitList(pengunjungList)
        }

        viewModel.statusMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        binding.fabAddPengunjung.setOnClickListener {
            startActivity(Intent(this, AddPengunjungActivity::class.java))
        }
    }
}
