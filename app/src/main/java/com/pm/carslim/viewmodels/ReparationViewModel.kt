package com.pm.carslim.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.carslim.data.models.Facture
import com.pm.carslim.data.models.Reparation
import com.pm.carslim.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

open class ReparationViewModel : ViewModel() {

    open fun addReparation (reparation: Reparation) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.addReparation(reparation)
                Log.d("API", "ajout d'une reparation")

            } catch (e: Exception) {
                Log.e("API_ERROR", "Erreur lors de l'ajout d'une reparation: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}