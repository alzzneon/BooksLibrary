package com.project.projectuts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BukuAdapter(
    private val bukuList: List<Buku>,
    private val onEditClick: (Buku) -> Unit,
    private val onDeleteClick: (Buku) -> Unit
) : RecyclerView.Adapter<BukuAdapter.BukuViewHolder>() {

    class BukuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tvJudulBuku)
        val tvPengarang: TextView = itemView.findViewById(R.id.tvPengarangBuku)
        val ibEdit: ImageView = itemView.findViewById(R.id.ibEdit)
        val ibDelete: ImageView = itemView.findViewById(R.id.ibDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_buku, parent, false)
        return BukuViewHolder(view)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = bukuList[position]
        holder.tvJudul.text = buku.judul
        holder.tvPengarang.text = buku.pengarang

        holder.ibEdit.setOnClickListener { onEditClick(buku) }
        holder.ibDelete.setOnClickListener { onDeleteClick(buku) }
    }

    override fun getItemCount(): Int {
        return bukuList.size
    }
}
