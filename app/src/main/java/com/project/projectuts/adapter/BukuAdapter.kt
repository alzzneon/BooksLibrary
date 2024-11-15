package com.project.projectuts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.projectuts.databinding.HeaderItemBinding
import com.project.projectuts.databinding.ListBukuBinding
import com.project.projectuts.extension.setDate
import com.project.projectuts.model.Buku

class BukuAdapter(
    private val onEditClick: (Buku) -> Unit,
    private val onDeleteClick: (Buku) -> Unit
) : ListAdapter<Buku, RecyclerView.ViewHolder>(RowCallback()) {
    private var dataGenre = arrayListOf<String>()

    class BukuViewHolder(private val binding: ListBukuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(buku: Buku, onEditClick: (Buku) -> Unit, onDeleteClick: (Buku) -> Unit) {
            binding.tvJudulBuku.text = buku.judul
            binding.tvPengarangBuku.text = buku.pengarang
            binding.tvTahunTerbit.text = buku.tahunTerbit.toString()
            binding.tvTanggalDitambahkan.setDate(buku.tanggalDitambahkan)

            binding.ibEdit.setOnClickListener { onEditClick(buku) }
            binding.ibDelete.setOnClickListener { onDeleteClick(buku) }
        }

        companion object {
            fun from(parent: ViewGroup): BukuViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListBukuBinding.inflate(layoutInflater, parent, false)
                return BukuViewHolder(binding)
            }
        }
    }

    class HeaderViewHolder(private val binding: HeaderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: String) {
            binding.headerGenre.text = genre
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE.BUKU.ordinal -> BukuViewHolder.from(parent)
            ITEM_VIEW_TYPE.GENRE.ordinal -> {
                val headerGenreBinding = HeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(headerGenreBinding)
            }
            else -> BukuViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BukuViewHolder -> {
                val buku = getItem(position) as Buku
                holder.bind(buku, onEditClick, onDeleteClick)
            }
            is HeaderViewHolder -> {
                val genreBuku = dataGenre[position]
                holder.bind(genreBuku)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)){
            is Buku -> ITEM_VIEW_TYPE.BUKU.ordinal
            is Buku -> ITEM_VIEW_TYPE.GENRE.ordinal
            else -> ITEM_VIEW_TYPE.HEADER.ordinal
        }
    }

    class RowCallback : DiffUtil.ItemCallback<Buku>() {
        override fun areItemsTheSame(oldItem: Buku, newItem: Buku): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Buku, newItem: Buku): Boolean {
            return oldItem == newItem
        }
    }

    enum class ITEM_VIEW_TYPE{HEADER, BUKU, GENRE}
}