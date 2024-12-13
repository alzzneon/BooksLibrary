package com.project.BooksLibrary.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.BooksLibrary.databinding.HeaderItemBinding
import com.project.BooksLibrary.databinding.ItemBookBinding
import com.project.BooksLibrary.Model.Book

class BukuAdapter(private val onItemCLick: (Int) -> Unit): ListAdapter<Book, RecyclerView.ViewHolder>(RowCallback()) {

    enum class ITEM_VIEW_TYPE { HEADER, BUKU }

    class BukuViewHolder(private val binding: ItemBookBinding,private val onItemCLick: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.tvJudul.text = book.title
            binding.tvGenre.text = book.genre
            binding.tvPengarang.text = book.author
            binding.tvTahunTerbit.text = book.year_publish.toString()
            binding.tvDeskripsi.text = book.description

            Glide.with(binding.ivCover.context)
                .load(book.image_url)
                .into(binding.ivCover)
            binding.root.setOnClickListener {
                val bookId = book.id
                if (bookId != null && bookId != -1) (
                    onItemCLick(bookId)
                )
            }
        }

        companion object {
            fun from(parent: ViewGroup, onItemCLick: (Int) -> Unit): BukuViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
                return BukuViewHolder(binding, onItemCLick)
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

    fun submitBooksByGenre(books: List<Book>) {
        val items = mutableListOf<Book>()
        val sortedBooks = books.sortedBy { it.genre }
        var lastGenre: String? = null
        sortedBooks.forEach { buku ->
            if (buku.genre != lastGenre) {
                items.add(Book(id = 0, title = "", genre = buku.genre, author = "", year_publish = 0, description = "", image_url = ""))
                lastGenre = buku.genre
            }
            items.add(buku)
        }
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE.HEADER.ordinal -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE.BUKU.ordinal -> BukuViewHolder.from(parent,onItemCLick)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val buku = getItem(position)
        if (buku.title.isEmpty()) {
            (holder as HeaderViewHolder).bind(buku.genre)
        } else {
            (holder as BukuViewHolder).bind(buku)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).title.isEmpty()) {
            ITEM_VIEW_TYPE.HEADER.ordinal
        } else {
            ITEM_VIEW_TYPE.BUKU.ordinal
        }
    }

    class RowCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
}