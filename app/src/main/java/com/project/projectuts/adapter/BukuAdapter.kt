package com.project.projectuts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.projectuts.databinding.ListBukuBinding
import com.project.projectuts.extension.setDate
import com.project.projectuts.model.Buku

class BukuAdapter(
    private val onEditClick: (Buku) -> Unit,
    private val onDeleteClick: (Buku) -> Unit
) : ListAdapter<Buku, BukuAdapter.BukuViewHolder>(RowCallback()) {

    class BukuViewHolder(private val binding: ListBukuBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): BukuViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListBukuBinding.inflate(layoutInflater, parent, false)
                return BukuViewHolder(binding)
            }
        }

        fun bind(buku: Buku, onEditClick: (Buku) -> Unit, onDeleteClick: (Buku) -> Unit) {
            binding.tvJudulBuku.text = buku.judul
            binding.tvPengarangBuku.text = buku.pengarang
            binding.tvTanggalDitambahkan.setDate(buku.tanggalDitambahkan)

            binding.ibEdit.setOnClickListener { onEditClick(buku) }
            binding.ibDelete.setOnClickListener { onDeleteClick(buku) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        return BukuViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = getItem(position)
        holder.bind(buku, onEditClick, onDeleteClick)
    }

    class RowCallback : DiffUtil.ItemCallback<Buku>() {
        override fun areItemsTheSame(oldItem: Buku, newItem: Buku): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Buku, newItem: Buku): Boolean {
            return oldItem == newItem
        }
    }
}