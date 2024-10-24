
package com.project.projectuts.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.projectuts.dao.PeminjamanDao            
import com.project.projectuts.viewmodel.PeminjamanViewModel

class PeminjamanViewModelFactory(private val peminjamanDao: PeminjamanDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeminjamanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeminjamanViewModel(peminjamanDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
