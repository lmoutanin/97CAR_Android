package com.pm.carslim.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.carslim.data.models.Client
import com.pm.carslim.data.models.Facture
import com.pm.carslim.data.models.Voiture
import com.pm.carslim.data.remote.ApiService
import com.pm.carslim.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class ClientDetailViewModel(private val api: ApiService) : ViewModel() {

    private val _client = MutableStateFlow<Client?>(null)
    val client: StateFlow<Client?> = _client.asStateFlow()

    private val _voiture = MutableStateFlow<Voiture?>(null)
    val voiture: StateFlow<Voiture?> = _voiture.asStateFlow()

    private val _voitures = MutableStateFlow<List<Voiture>>(emptyList())
    val voitures: StateFlow<List<Voiture>> = _voitures.asStateFlow()

    private val _factures = MutableStateFlow<List<Facture>>(emptyList())
    val factures: StateFlow<List<Facture>> = _factures.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message // Message


    // Méthode pour charger les détails du client
    fun loadClient(clientId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.getClientById(clientId)
                _client.value = response
                _error.value = null
                Log.d("ClientDetailViewModel", "Client chargé : $response")
            } catch (e: Exception) {
                _error.value = "Impossible de charger les détails du client : ${e.message}"
                Log.e("ClientDetailViewModel", "Erreur de chargement client", e)
            } finally {
                _isLoading.value = false
            }

            try {
                val clientVoitures = api.getVoituresByClientId(clientId)
                _voitures.value = clientVoitures
                Log.d("ClientDetailViewModel", "Voitures chargées : $clientVoitures")
            } catch (e: Exception) {
                _voitures.value = emptyList()
                Log.e("ClientDetailViewModel", "Erreur de chargement des voitures", e)
            }

            try {
                val clientFactures = api.getFacturesByClientId(clientId)
                _factures.value = clientFactures
                Log.d("ClientDetailViewModel", "Factures chargées : $clientFactures")
            } catch (e: Exception) {
                _factures.value = emptyList()
                Log.e("ClientDetailViewModel", "Erreur de chargement des factures", e)
            }
        }
    }


    // Methode pour supprimer  un client
    open fun deleteClient(idClient: Int) {
        viewModelScope.launch {
            try {
                addMessage(RetrofitInstance.api.deleteClientById(idClient))
                Log.d("API", "Client supprimé avec succès")

            } catch (e: Exception) {
                addMessage()
                Log.e("API_ERROR", "Erreur lors de la suppression d'un client: ${e.message}")
                e.printStackTrace()

            }
        }

    }

    //Methode pour modifier un client
    open fun editClient(client: Client) {
        viewModelScope.launch {
            try {
                addMessage(RetrofitInstance.api.editClientById(client.id_client!!.toInt(), client))
                Log.d("API", "modification un client")

            } catch (e: Exception) {
                addMessage()
                Log.e("API_ERROR", "Erreur lors de  la modification d'un client: ${e.message}")
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