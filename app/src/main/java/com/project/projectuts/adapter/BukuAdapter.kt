package com.project.projectuts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.projectuts.databinding.HeaderItemBinding
import com.project.projectuts.databinding.ListBukuBinding
import com.project.projectuts.model.Buku

class BukuAdapter(
    private val onEditClick: (Buku) -> Unit,
    private val onDeleteClick: (Buku) -> Unit
) : ListAdapter<Buku, RecyclerView.ViewHolder>(RowCallback()) {

    enum class ITEM_VIEW_TYPE { HEADER, BUKU }

    class BukuViewHolder(private val binding: ListBukuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(buku: Buku, onEditClick: (Buku) -> Unit, onDeleteClick: (Buku) -> Unit) {
            binding.tvJudulBuku.text = buku.judul
            binding.tvPengarangBuku.text = buku.pengarang
            binding.tvTahunTerbit.text = buku.tahunTerbit.toString()
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

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderItemBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    fun submitBooksByGenre(books: List<Buku>) {
        val items = mutableListOf<Buku>()
        val sortedBooks = books.sortedBy { it.genre }
        var lastGenre: String? = null
        sortedBooks.forEach { buku ->
            if (buku.genre != lastGenre) {
                items.add(Buku(id = 0, judul = "", genre = buku.genre, pengarang = "", tahunTerbit = 0))
                lastGenre = buku.genre
            }
            items.add(buku)
        }
        submitList(items)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE.HEADER.ordinal -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE.BUKU.ordinal -> BukuViewHolder.from(parent)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val buku = getItem(position)

        if (buku.judul.isEmpty()) {
            (holder as HeaderViewHolder).bind(buku.genre)
        } else {
            (holder as BukuViewHolder).bind(buku, onEditClick, onDeleteClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).judul.isEmpty()) {
            ITEM_VIEW_TYPE.HEADER.ordinal
        } else {
            ITEM_VIEW_TYPE.BUKU.ordinal
        }
    }

    class RowCallback : DiffUtil.ItemCallback<Buku>() {
        override fun areItemsTheSame(oldItem: Buku, newItem: Buku): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Buku, newItem: Buku): Boolean {
            return oldItem == newItem
        }
    }
}