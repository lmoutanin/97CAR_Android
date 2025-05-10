package com.pm.carslim.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.carslim.data.models.Voiture
import com.pm.carslim.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

class VoitureViewModel : ViewModel() {


    open fun addVoiture(voiture: Voiture) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.addVoiture(voiture)
                Log.d("API", "ajout d'une facture")

            } catch (e: Exception) {
                Log.e("API_ERROR", "Erreur lors de l'ajout d'une facture: ${e.message}")
                e.printStackTrace()
            }
        }
    }

}