package com.project.projectuts.factory

import com.project.projectuts.viewModel.BukuViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.projectuts.repository.BukuRepository

class ViewModelFactory(private val repository: BukuRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BukuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BukuViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
