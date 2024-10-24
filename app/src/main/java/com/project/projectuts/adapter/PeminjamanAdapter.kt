package com.project.projectuts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.projectuts.databinding.ItemPeminjamanBinding
import com.project.projectuts.model.Peminjaman

class PeminjamanAdapter(
    private var peminjamanList: List<Peminjaman>,
    private val onDeleteClick: (Peminjaman) -> Unit
) : RecyclerView.Adapter<PeminjamanAdapter.PeminjamanViewHolder>() {

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
        val peminjaman = peminjamanList[position]
        holder.bind(peminjaman, onDeleteClick)
    }

    override fun getItemCount(): Int = peminjamanList.size

    fun updatePeminjamanList(newList: List<Peminjaman>) {
        peminjamanList = newList
        notifyDataSetChanged()
    }
}
