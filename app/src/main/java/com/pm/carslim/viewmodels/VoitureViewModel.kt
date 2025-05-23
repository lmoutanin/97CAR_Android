package com.pm.carslim.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.carslim.data.models.Voiture
import com.pm.carslim.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class VoitureViewModel : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message


    open fun addVoiture(voiture: Voiture) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.addVoiture(voiture)
                val message = JSONObject(response.body()?.string() ?: "").getString("message")
                _message.value =  message
                Log.d("API", "ajout d'une facture")

            } catch (e: Exception) {
                Log.e("API_ERROR", "Erreur lors de l'ajout d'une facture: ${e.message}")
                e.printStackTrace()
                _message.value =  "Erreur lors de l'ajout  d'une voiture"
            }
        }
    }

}