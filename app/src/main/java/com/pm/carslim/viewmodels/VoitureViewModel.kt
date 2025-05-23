package com.pm.carslim.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.carslim.data.models.Client
import com.pm.carslim.data.models.Voiture
import com.pm.carslim.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class VoitureViewModel : ViewModel() {


    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message // Message ajout ou erreur


    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    open val clients: StateFlow<List<Client>> = _clients    // Liste des clients

    init {
        fetchClients()
    }

    // Methode pour obtenir tout les clients
    fun fetchClients() {
        viewModelScope.launch {
            try {
                val clientsResponse = RetrofitInstance.api.getClients()
                _clients.value = RetrofitInstance.api.getClients()
                Log.d("API_REPONSE", "Clients reçus: ${clientsResponse.size}")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Erreur lors de la récupération: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Methode pour ajout une voiture
    open fun addVoiture(voiture: Voiture) {
        viewModelScope.launch {
            try {
                addMessage(RetrofitInstance.api.addVoiture(voiture))
                Log.d("API", "ajout d'une facture")

            } catch (e: Exception) {
                addMessage()
                Log.e("API_ERROR", "Erreur lors de l'ajout d'une facture: ${e.message}")
                e.printStackTrace()

            }
        }
    }

    // Methode pour  message
    fun addMessage(response: retrofit2.Response<ResponseBody>? = null) {
        var message = ""
        if (response != null) message =
            JSONObject(response.body()?.string() ?: "").getString("message") else message = "ERREUR"
        _message.value = message
    }

}