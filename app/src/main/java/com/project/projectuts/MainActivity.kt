package com.project.projectuts

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.projectuts.addActivity.AddPeminjamanActivity
import com.project.projectuts.databinding.ActivityMainBinding
import com.project.projectuts.list.ListBuku
import com.project.projectuts.list.ListPengunjungActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tambahkan listener untuk button List Buku
        binding.buttonBuku.setOnClickListener {
            val intent = Intent(this, ListBuku::class.java)
            startActivity(intent) // Membuka ListBuku saat tombol diklik
        }

        // Tambahkan listener untuk button List Pengunjung
        binding.buttonListPengunjung.setOnClickListener {
            // Memulai ListPengunjungActivity saat tombol ditekan
            val intent = Intent(this, ListPengunjungActivity::class.java)
            startActivity(intent)
        }

        // Tambahkan listener untuk button Add Peminjaman
        binding.buttonAddPeminjaman.setOnClickListener {
            val intent = Intent(this, AddPeminjamanActivity::class.java)
            startActivity(intent)
        }
    }
}
