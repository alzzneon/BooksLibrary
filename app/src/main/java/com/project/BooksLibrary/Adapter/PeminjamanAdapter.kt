package com.project.BooksLibrary.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.BooksLibrary.databinding.ItemPeminjamanBinding
import com.project.BooksLibrary.Model.Peminjaman

class PeminjamanAdapter(

    private val onDeleteClick: (Peminjaman) -> Unit
) : ListAdapter<Peminjaman, PeminjamanAdapter.PeminjamanViewHolder>(RowCallbackAdapter()) {

    class PeminjamanViewHolder(private val binding: ItemPeminjamanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(peminjaman: Peminjaman, onDeleteClick: (Peminjaman) -> Unit) {
            binding.textViewNamaPeminjam.text = peminjaman.namaPeminjam
            binding.textViewBukuDipinjam.text = peminjaman.bukuDipinjam
            binding.textViewTanggalPinjam.text = peminjaman.tglDipinjam

            binding.buttonDelete.setOnClickListener {
                onDeleteClick(peminjaman)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeminjamanViewHolder {
        val binding = ItemPeminjamanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeminjamanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeminjamanViewHolder, position: Int) {
        val peminjaman = getItem(position)
        holder.bind(peminjaman, onDeleteClick)
    }

    class RowCallbackAdapter: DiffUtil.ItemCallback<Peminjaman>() {
        override fun areItemsTheSame(oldItem: Peminjaman, newItem: Peminjaman): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Peminjaman, newItem: Peminjaman): Boolean {
            return  oldItem == newItem
        }
    }
}
