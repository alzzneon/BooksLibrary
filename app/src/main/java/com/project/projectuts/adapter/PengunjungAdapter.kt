package com.project.projectuts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.projectuts.R
import com.project.projectuts.adapter.BukuAdapter.BukuViewHolder
import com.project.projectuts.databinding.ListBukuBinding
import com.project.projectuts.list.ListPengunjungActivity
import com.project.projectuts.model.Buku
import com.project.projectuts.model.Pengunjung

class PengunjungAdapter(
    private val onDeleteClick: (Pengunjung) -> Unit
) : ListAdapter<Pengunjung, PengunjungAdapter.PengunjungViewHolder>(PengunjungDiffCallback()) {

    class PengunjungViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTextView: TextView = itemView.findViewById(R.id.textNamaPengunjung)
        val tanggalKunjunganTextView: TextView = itemView.findViewById(R.id.textTanggalKunjungan)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDeletePengunjung)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PengunjungViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pengunjung, parent, false)
        return PengunjungViewHolder(view)
    }

    override fun onBindViewHolder(holder: PengunjungViewHolder, position: Int) {
        val pengunjung = getItem(position)
        holder.namaTextView.text = pengunjung.nama
        holder.tanggalKunjunganTextView.text = pengunjung.tanggalKunjungan

        holder.deleteButton.setOnClickListener {
            onDeleteClick(pengunjung)
        }
    }
    class PengunjungDiffCallback : DiffUtil.ItemCallback<Pengunjung>() {
        override fun areItemsTheSame(oldItem: Pengunjung, newItem: Pengunjung): Boolean {
            // Assuming Pengunjung has a unique id field. Adjust according to your model
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pengunjung, newItem: Pengunjung): Boolean {
            return oldItem == newItem
        }
    }
}
