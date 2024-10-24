package com.project.projectuts.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.projectuts.repository.PengunjungRepository
import com.project.projectuts.viewModel.PengunjungViewModel

class PengunjungViewModelFactory(private val repository: PengunjungRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PengunjungViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PengunjungViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
