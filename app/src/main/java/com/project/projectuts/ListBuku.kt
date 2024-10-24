package com.project.projectuts

import BukuViewModel
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListBuku : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bukuAdapter: BukuAdapter

    private val viewModel: BukuViewModel by viewModels {
        ViewModelFactory(BukuRepository(AplikasiDatabase.getDatabase(this).bukuDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_buku)

        recyclerView = findViewById(R.id.rv_listBuku)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addButton = findViewById<FloatingActionButton>(R.id.tambahBuku)
        addButton.setOnClickListener {
            val intent = Intent(this, AddBukuActivity::class.java)
            startActivity(intent)
        }

        // Observe LiveData from the ViewModel
        viewModel.allBuku.observe(this, Observer { books ->
            books?.let {
                bukuAdapter = BukuAdapter(it,
                    onEditClick = { buku -> showEditDialog(buku) },
                    onDeleteClick = { buku -> showDeleteConfirmationDialog(buku) }
                )
                recyclerView.adapter = bukuAdapter
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
