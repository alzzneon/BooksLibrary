package com.project.projectuts

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.projectuts.databinding.ActivityListPengunjungBinding

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

        // Mengamati LiveData
        viewModel.allPengunjung.observe(this, { pengunjungList ->
            pengunjungAdapter = PengunjungAdapter(pengunjungList,
                onDeleteClick = { pengunjung -> viewModel.deletePengunjung(pengunjung) }
            )
            binding.recyclerViewPengunjung.adapter = pengunjungAdapter
        })

        val addButton = findViewById<FloatingActionButton>(R.id.fab_add_pengunjung)
        addButton.setOnClickListener {
            startActivity(Intent(this, AddPengunjungActivity::class.java))
        }
    }
}
