package com.project.books_library.adapter

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.books_library.R
import com.project.books_library.databinding.ItemLendingBinding
import com.project.books_library.model.Lending
import java.util.Calendar
import java.util.Locale

class LendingAdapter(
    private val context: Context,
    private val onDeleteClick: (Lending) -> Unit,
    private val onEditClick: (Lending) -> Unit
): ListAdapter<Lending, LendingAdapter.LendingViewHolder>(RowCallback()) {

    class LendingViewHolder(private val binding: ItemLendingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lending: Lending, context: Context, onDeleteClick: (Lending) -> Unit, onEditClick: (Lending) -> Unit) {
            binding.tvBookName.text = lending.book_title
            binding.tvBorrowerName.text = lending.visitor_name
            binding.tvLendingDate.text = lending.lending_date
            binding.tvLendingReturn.text = lending.return_date ?: "Belum Dikembalikan"
            binding.tvLendingStatus.text = lending.status

            binding.btnDeleteBorrow.setOnClickListener {
                showDeleteConfirmationDialog(context,lending,onDeleteClick)
            }
            binding.btnEditBorrow.setOnClickListener {
                showDialogEdit(context, lending, onEditClick)
            }

        }

        private fun showDialogEdit(context: Context, lending: Lending, onEditClick: (Lending) -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Update Status Pengembalian")

            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_lending, null)
            builder.setView(dialogView)

            val editNameVisitor = dialogView.findViewById<EditText>(R.id.edt_name_visitor)
            val editBookTitle = dialogView.findViewById<EditText>(R.id.edt_book)
            val editLendingDate = dialogView.findViewById<EditText>(R.id.edt_lending_date)
            val editReturnDate = dialogView.findViewById<EditText>(R.id.edt_return_date)

            editNameVisitor.setText(lending.visitor_name)
            editBookTitle.setText(lending.book_title)
            editLendingDate.setText(lending.lending_date)
            editReturnDate.setText(lending.return_date)

            editLendingDate.setOnClickListener {
                showDatePickerDialog(context, editLendingDate)
            }
            editReturnDate.setOnClickListener {
                showDatePickerDialog(context, editReturnDate)
            }

            builder.setPositiveButton("Simpan") { dialog, _ ->
                lending.visitor_name = editNameVisitor.text.toString()
                lending.book_title = editBookTitle.text.toString()
                lending.lending_date = editLendingDate.text.toString()
                lending.return_date = editReturnDate.text.toString()
                lending.status = if (lending.return_date.isNullOrEmpty()) {
                    "Borrowed"
                } else {
                    "Returned"
                }

                onEditClick(lending)
                Toast.makeText(context, "${lending.visitor_name} berhasil diperbarui!", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            builder.setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }

        private fun showDatePickerDialog(context: Context, editText: EditText) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)
                val selectedDateTime = String.format(
                    Locale.US,
                    "%04d-%02d-%02dT%02d:%02d:00.000Z",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay,
                    currentHour,
                    currentMinute
                )
                editText.setText(selectedDateTime)
            }, year, month, day)

            datePickerDialog.show()
        }
        private fun showDeleteConfirmationDialog(context: Context, lending: Lending, onDeleteClick: (Lending) -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Konfirmasi Hapus")
            builder.setMessage("\nUntuk Menghapus Pastika Terhubung ke Internet\nApakah Anda yakin ingin menghapus Peminjaman atas nama ${lending.visitor_name}?")
            builder.setPositiveButton("Ya") { dialog, _ ->
                onDeleteClick(lending)
                Toast.makeText(context, "${lending.visitor_name} berhasil dihapus!", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }

        companion object {
            fun from(parent: ViewGroup): LendingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLendingBinding.inflate(layoutInflater, parent, false)
                return LendingViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LendingViewHolder {
        return LendingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LendingViewHolder, position: Int) {
        val lending = getItem(position)
        holder.bind(lending,context, onDeleteClick, onEditClick)
    }

    class RowCallback : DiffUtil.ItemCallback<Lending>() {
        override fun areItemsTheSame(oldItem: Lending, newItem: Lending): Boolean {
            return oldItem.id_lending == newItem.id_lending
        }

        override fun areContentsTheSame(oldItem: Lending, newItem: Lending): Boolean {
            return oldItem == newItem
        }
    }
}
