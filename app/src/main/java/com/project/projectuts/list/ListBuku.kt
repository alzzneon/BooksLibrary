package com.project.projectuts.list

import com.project.projectuts.viewModel.BukuViewModel
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.projectuts.addActivity.AddBukuActivity
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.R
import com.project.projectuts.factory.ViewModelFactory
import com.project.projectuts.adapter.BukuAdapter
import com.project.projectuts.model.Buku
import com.project.projectuts.repository.BukuRepository
import com.project.projectuts.databinding.ActivityListBukuBinding // Import the generated binding class

class ListBuku : AppCompatActivity() {

    private lateinit var binding: ActivityListBukuBinding // Create a binding variable
    private lateinit var bukuAdapter: BukuAdapter

    private val viewModel: BukuViewModel by viewModels {
        ViewModelFactory(BukuRepository(AplikasiDatabase.getDatabase(this).bukuDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBukuBinding.inflate(layoutInflater) // Inflate the binding
        setContentView(binding.root) // Set the content view to the root of the binding

        binding.rvListBuku.layoutManager = LinearLayoutManager(this)

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
                binding.rvListBuku.adapter = bukuAdapter // Use binding to set the adapter
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
