package com.project.projectuts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PengunjungAdapter(
    private val pengunjungList: List<Pengunjung>,
    private val onDeleteClick: (Pengunjung) -> Unit
) : RecyclerView.Adapter<PengunjungAdapter.PengunjungViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PengunjungViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pengunjung, parent, false)
        return PengunjungViewHolder(view)
    }

    override fun onBindViewHolder(holder: PengunjungViewHolder, position: Int) {
        val pengunjung = pengunjungList[position]
        holder.namaTextView.text = pengunjung.nama
        holder.tanggalKunjunganTextView.text = pengunjung.tanggalKunjungan

        holder.deleteButton.setOnClickListener {
            onDeleteClick(pengunjung)
        }
    }

    override fun getItemCount(): Int = pengunjungList.size

    class PengunjungViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTextView: TextView = itemView.findViewById(R.id.textNamaPengunjung)
        val tanggalKunjunganTextView: TextView = itemView.findViewById(R.id.textTanggalKunjungan)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDeletePengunjung)
    }
}
