package com.project.projectuts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PengunjungViewModelFactory(private val repository: PengunjungRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PengunjungViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PengunjungViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
