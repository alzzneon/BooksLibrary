package com.project.projectuts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.projectuts.databinding.ListBukuBinding
import com.project.projectuts.model.Buku

class BukuAdapter(
    private val bukuList: List<Buku>,
    private val onEditClick: (Buku) -> Unit,
    private val onDeleteClick: (Buku) -> Unit
) : RecyclerView.Adapter<BukuAdapter.BukuViewHolder>() {

    class BukuViewHolder(private val binding: ListBukuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(buku: Buku, onEditClick: (Buku) -> Unit, onDeleteClick: (Buku) -> Unit) {
            binding.tvJudulBuku.text = buku.judul
            binding.tvPengarangBuku.text = buku.pengarang

            binding.ibEdit.setOnClickListener { onEditClick(buku) }
            binding.ibDelete.setOnClickListener { onDeleteClick(buku) }
            //binding.btnDetail.se

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val binding = ListBukuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BukuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = bukuList[position]
        holder.bind(buku, onEditClick, onDeleteClick)
    }

    override fun getItemCount(): Int {
        return bukuList.size
    }
}
