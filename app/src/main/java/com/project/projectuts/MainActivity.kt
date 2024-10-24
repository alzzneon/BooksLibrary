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

        // Listener untuk button List Buku
        binding.buttonBuku.setOnClickListener {
            val intent = Intent(this, ListBuku::class.java)
            startActivity(intent)
        }

        // Listener untuk button List Pengunjung
        binding.buttonListPengunjung.setOnClickListener {
            val intent = Intent(this, ListPengunjungActivity::class.java)
            startActivity(intent)
        }

        // Listener untuk button List Peminjaman
        binding.buttonListPeminjaman.setOnClickListener {
            startActivity(Intent(this, ListPeminjamanActivity::class.java))
        }
    }
}
