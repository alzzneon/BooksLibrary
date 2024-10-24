package com.project.projectuts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.projectuts.model.PeminjamanRelasi
import com.project.projectuts.databinding.ItemPeminjamanBinding

class PeminjamanAdapter(private var peminjamanList: List<PeminjamanRelasi>) :
    RecyclerView.Adapter<PeminjamanAdapter.PeminjamanViewHolder>() {

    class PeminjamanViewHolder(private val binding: ItemPeminjamanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(peminjaman: PeminjamanRelasi) {
            binding.namaPengunjungText.text = peminjaman.nama
            binding.judulBukuText.text = peminjaman.judul
            binding.tanggalPinjamText.text = peminjaman.tanggalPinjam
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeminjamanViewHolder {
        val binding = ItemPeminjamanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeminjamanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeminjamanViewHolder, position: Int) {
        holder.bind(peminjamanList[position])
    }

    override fun getItemCount(): Int {
        return peminjamanList.size
    }

    // Jika Anda ingin memperbarui data dalam adapter
    fun updateData(newList: List<PeminjamanRelasi>) {
        peminjamanList = newList
        notifyDataSetChanged()
    }
}
