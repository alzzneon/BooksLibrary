package com.project.projectuts.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.projectuts.addActivity.AddBukuActivity
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.R
import com.project.projectuts.adapter.BukuAdapter
import com.project.projectuts.model.Buku
import com.project.projectuts.viewModel.BukuViewModel
import com.project.projectuts.databinding.ActivityListBukuBinding

class   ListBuku : AppCompatActivity() {

    private lateinit var binding: ActivityListBukuBinding
    private lateinit var bukuAdapter: BukuAdapter

    private lateinit var viewModel: BukuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvListBuku.layoutManager = LinearLayoutManager(this)

        val bukuDao = AplikasiDatabase.getDatabase(this).bukuDao()
        viewModel = BukuViewModel(bukuDao)

        binding.tambahBuku.setOnClickListener {
            val intent = Intent(this, AddBukuActivity::class.java)
            startActivity(intent)
        }

        viewModel.allBuku.observe(this, Observer { books ->
            books?.let {
                bukuAdapter = BukuAdapter(it,
                    onEditClick = { buku -> showEditDialog(buku) },
                    onDeleteClick = { buku -> showDeleteConfirmationDialog(buku) }
                )
                binding.rvListBuku.adapter = bukuAdapter
            }
        })
    }

    private fun showEditDialog(buku: Buku) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null)

        val edtJudul = dialogView.findViewById<EditText>(R.id.edt_judul)
        val edtPengarang = dialogView.findViewById<EditText>(R.id.edt_pengarang)
        val edtTahunTerbit = dialogView.findViewById<EditText>(R.id.edt_tahunTerbit)

        edtJudul.setText(buku.judul)
        edtPengarang.setText(buku.pengarang)
        edtTahunTerbit.setText(buku.tahunTerbit.toString())

        AlertDialog.Builder(this)
            .setTitle("Edit Buku")
            .setView(dialogView)
            .setPositiveButton("Simpan") { dialog, _ ->
                val updatedBuku = Buku(
                    id = buku.id,
                    judul = edtJudul.text.toString(),
                    pengarang = edtPengarang.text.toString(),
                    tahunTerbit = edtTahunTerbit.text.toString().toInt()
                )
                viewModel.updateBuku(updatedBuku)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showDeleteConfirmationDialog(buku: Buku) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Buku")
            .setMessage("Apakah Anda yakin ingin menghapus buku '${buku.judul}'?")
            .setPositiveButton("Ya") { dialog, _ ->
                viewModel.deleteBuku(buku)
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}