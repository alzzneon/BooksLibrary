package com.project.projectuts.list

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.projectuts.API.BookRepository
import com.project.projectuts.API.RetrofitInstance
import com.project.projectuts.R
import com.project.projectuts.addActivity.AddBukuActivity
import com.project.projectuts.database.AplikasiDatabase
import com.project.projectuts.adapter.BukuAdapter
import com.project.projectuts.viewModel.BukuViewModel
import com.project.projectuts.databinding.ActivityListBukuBinding
import com.project.projectuts.model.Book

class ListBuku : AppCompatActivity() {

    private lateinit var binding: ActivityListBukuBinding
    private lateinit var bukuAdapter: BukuAdapter
    private lateinit var booksViewModel: BukuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvListBuku.layoutManager = LinearLayoutManager(this)
        bukuAdapter = BukuAdapter()
        binding.rvListBuku.adapter = bukuAdapter

        val bookDao = AplikasiDatabase.getDatabase(this).bookDao()
        val bookRepository = BookRepository(RetrofitInstance.apiService, this, bookDao)
        booksViewModel = BukuViewModel(bookRepository)

        booksViewModel.syncStatusLiveData.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }
        booksViewModel.syncData()

        errorMessage()
        observeViewModel()
        booksViewModel.fetchBooks()

        binding.tambahBuku.setOnClickListener {
            val intent = Intent(this, AddBukuActivity::class.java)
            startActivity(intent)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            booksViewModel.refreshBooks()
        }
    }

    // belum di fix
    private fun showEditDialog(book: Book) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null)

        val edtJudul = dialogView.findViewById<EditText>(R.id.edt_judul)
        val edtGenre = dialogView.findViewById<EditText>(R.id.edt_genre)
        val edtPengarang = dialogView.findViewById<EditText>(R.id.edt_pengarang)
        val edtTahunTerbit = dialogView.findViewById<EditText>(R.id.edt_tahunTerbit)

        edtJudul.setText(book.title)
        edtGenre.setText(book.genre)
        edtPengarang.setText(book.author)
        edtTahunTerbit.setText(book.year_publish.toString())

        AlertDialog.Builder(this)
            .setTitle("Edit Book")
            .setView(dialogView)
            .setPositiveButton("Simpan") { dialog, _ ->
                val updatedBook = Book(
                    id = book.id,
                    title = edtJudul.text.toString(),
                    genre = edtGenre.text.toString(),
                    author = edtPengarang.text.toString(),
                    year_publish = edtTahunTerbit.text.toString().toInt(),
                    description = book.description,
                    image_url = book.image_url
                )
                booksViewModel.updateBook(updatedBook)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    // belum di fix
    private fun showDeleteConfirmationDialog(book: Book) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Book")
            .setMessage("Apakah Anda yakin ingin menghapus book '${book.title}'?")
            .setPositiveButton("Ya") { dialog, _ ->
                booksViewModel.deleteBook(book)
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun errorMessage() {
        booksViewModel.errorLiveData.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun observeViewModel() {
        booksViewModel.booksLiveData.observe(this) { books ->
            if (books != null) {
                bukuAdapter.submitList(books)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}