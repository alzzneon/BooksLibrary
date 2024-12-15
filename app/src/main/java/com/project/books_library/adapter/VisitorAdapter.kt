package com.project.books_library.adapter

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
import com.project.books_library.databinding.ItemVisitorBinding
import com.project.books_library.model.Visitors

class VisitorAdapter(
    private val context: Context,
    private val onDeleteClick: (Visitors) -> Unit,
    private val onEditClick: (Visitors) -> Unit
) : ListAdapter<Visitors, VisitorAdapter.VisitorViewHolder>(RowCallback()) {

    class VisitorViewHolder(private val binding: ItemVisitorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(visitor: Visitors, context: Context, onDeleteClick: (Visitors) -> Unit, onEditClick: (Visitors) -> Unit) {
            binding.tvNameVisitor.text = visitor.name
            binding.tvVisitDate.text = "Visit Date: ${visitor.visit_date.take(10)}"
            binding.tvGender.text = "Gender: ${visitor.gender}"
            binding.tvAddress.text = "Address: ${visitor.address}"
            binding.tvPhone.text = "Phone: ${visitor.phone_number}"
            binding.tvEmail.text = "Email: ${visitor.email}"
            binding.tvVisitPurpose.text = "Purpose: ${visitor.visit_purpose}"

            binding.btnDelete.setOnClickListener {
                showDeleteConfirmationDialog(context, visitor, onDeleteClick)
            }
            binding.btnEdit.setOnClickListener {
                showEditDialog(context, visitor, onEditClick)
            }
        }

        private fun showEditDialog(context: Context, visitor: Visitors, onEditClick: (Visitors) -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Edit Visitor")

            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_visitor, null)
            builder.setView(dialogView)

            val editName = dialogView.findViewById<EditText>(R.id.editName)
            val editVisitDate = dialogView.findViewById<EditText>(R.id.editVisitDate)
            val editGender = dialogView.findViewById<EditText>(R.id.editGender)
            val editAddress = dialogView.findViewById<EditText>(R.id.editAddress)
            val editPhone = dialogView.findViewById<EditText>(R.id.editPhone)
            val editEmail = dialogView.findViewById<EditText>(R.id.editEmail)
            val editPurpose = dialogView.findViewById<EditText>(R.id.editPurpose)

            editName.setText(visitor.name)
            editVisitDate.setText(visitor.visit_date)
            editGender.setText(visitor.gender)
            editAddress.setText(visitor.address)
            editPhone.setText(visitor.phone_number)
            editEmail.setText(visitor.email)
            editPurpose.setText(visitor.visit_purpose)

            builder.setPositiveButton("Simpan") { dialog, _ ->
                visitor.name = editName.text.toString()
                visitor.visit_date = editVisitDate.text.toString()
                visitor.gender = editGender.text.toString()
                visitor.address = editAddress.text.toString()
                visitor.phone_number = editPhone.text.toString()
                visitor.email = editEmail.text.toString()
                visitor.visit_purpose = editPurpose.text.toString()

                onEditClick(visitor)
                Toast.makeText(context, "${visitor.name} berhasil diperbarui!", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            builder.setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }

        private fun showDeleteConfirmationDialog(context: Context, visitor: Visitors, onDeleteClick: (Visitors) -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Konfirmasi Hapus")
            builder.setMessage("Apakah Anda yakin ingin menghapus ${visitor.name}?")
            builder.setPositiveButton("Ya") { dialog, _ ->
                onDeleteClick(visitor)
                Toast.makeText(context, "${visitor.name} berhasil dihapus!", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }

        companion object {
            fun from(parent: ViewGroup): VisitorViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemVisitorBinding.inflate(layoutInflater, parent, false)
                return VisitorViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitorViewHolder {
        return VisitorViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VisitorViewHolder, position: Int) {
        val visitor = getItem(position)
        holder.bind(visitor, context, onDeleteClick, onEditClick)
    }

    class RowCallback : DiffUtil.ItemCallback<Visitors>() {
        override fun areItemsTheSame(oldItem: Visitors, newItem: Visitors): Boolean {
            return oldItem.id_visitor == newItem.id_visitor
        }

        override fun areContentsTheSame(oldItem: Visitors, newItem: Visitors): Boolean {
            return oldItem == newItem
        }
    }
}