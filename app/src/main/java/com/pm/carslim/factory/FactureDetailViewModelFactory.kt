package com.pm.carslim.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pm.carslim.data.remote.RetrofitInstance
import com.pm.carslim.viewmodels.FactureDetailViewModel


class FactureDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FactureDetailViewModel::class.java)) {
            return FactureDetailViewModel(RetrofitInstance.api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}