package com.project.BooksLibrary.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.BooksLibrary.databinding.ItemVisitorBinding
import com.project.BooksLibrary.Model.Visitors

class VisitorAdapter : ListAdapter<Visitors, VisitorAdapter.VisitorViewHolder>(RowCallback()) {

    class VisitorViewHolder(private val binding: ItemVisitorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(visitor: Visitors) {
            binding.tvNameVisitor.text = visitor.name
            binding.tvVisitDate.text = "Visit Date: ${visitor.visit_date.take(10)}"
            binding.tvGender.text = "Gender: ${visitor.gender}"
            binding.tvAddress.text = "Address: ${visitor.address}"
            binding.tvPhone.text = "Phone: ${visitor.phone_number}"
            binding.tvEmail.text = "Email: ${visitor.email}"
            binding.tvVisitPurpose.text = "Purpose: ${visitor.visit_purpose}"
        }

        companion object {
            fun from(parent: ViewGroup): VisitorViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemVisitorBinding.inflate(layoutInflater, parent, false)
                return VisitorViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitorViewHolder {
        return VisitorViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VisitorViewHolder, position: Int) {
        val visitor = getItem(position)
        holder.bind(visitor)
    }

    class RowCallback : DiffUtil.ItemCallback<Visitors>() {
        override fun areItemsTheSame(oldItem: Visitors, newItem: Visitors): Boolean {
            return oldItem.id_visitors == newItem.id_visitors
        }

        override fun areContentsTheSame(oldItem: Visitors, newItem: Visitors): Boolean {
            return oldItem == newItem
        }
    }
}