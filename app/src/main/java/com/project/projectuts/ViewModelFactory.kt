package com.project.projectuts

import BukuViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val repository: BukuRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BukuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BukuViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
