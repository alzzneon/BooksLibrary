package com.project.books_library.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.books_library.databinding.ItemVisitorBinding
import com.project.books_library.model.Visitors

class VisitorAdapter(private val context: Context, private val onDeleteClick: (Visitors) -> Unit) : ListAdapter<Visitors, VisitorAdapter.VisitorViewHolder>(RowCallback()) {

    class VisitorViewHolder(private val binding: ItemVisitorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(visitor: Visitors, context: Context, onDeleteClick: (Visitors) -> Unit) {
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
        }

        private fun showDeleteConfirmationDialog(context: Context, visitor: Visitors, onDeleteClick: (Visitors) -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Konfirmasi Hapus")
            builder.setMessage("Apakah Anda yakin ingin menghapus ${visitor.name}?")
            builder.setPositiveButton("Ya") { dialog, _ ->
                onDeleteClick(visitor)
                Toast.makeText(context, "${visitor.name} berhasil dihapus!.", Toast.LENGTH_LONG).show()
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
        holder.bind(visitor, context, onDeleteClick)
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