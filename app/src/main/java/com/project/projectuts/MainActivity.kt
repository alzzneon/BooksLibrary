package com.project.projectuts

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.databinding.ActivityMainBinding
import com.project.projectuts.list.ListBuku
import com.project.projectuts.list.ListPeminjamanActivity
import com.project.projectuts.list.ListPengunjungActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardBuku.setOnClickListener {
            val intent = Intent(this, ListBuku::class.java)
            startActivity(intent)
        }

        binding.cardPengunjung.setOnClickListener {
            val intent = Intent(this, ListPengunjungActivity::class.java)
            startActivity(intent)
        }

        binding.cardPeminjaman.setOnClickListener {
            startActivity(Intent(this, ListPeminjamanActivity::class.java))
        }
    }
}
