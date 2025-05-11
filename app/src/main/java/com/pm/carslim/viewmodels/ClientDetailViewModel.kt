package com.pm.carslim.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.carslim.data.models.Client
import com.pm.carslim.data.models.Facture
import com.pm.carslim.data.models.Voiture
import com.pm.carslim.data.remote.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClientDetailViewModel(private val api: ApiService) : ViewModel() {

    private val _client = MutableStateFlow<Client?>(null)
    val client: StateFlow<Client?> = _client.asStateFlow()

    private val _voitures = MutableStateFlow<List<Voiture>>(emptyList())
    val voitures: StateFlow<List<Voiture>> = _voitures.asStateFlow()

    private val _factures = MutableStateFlow<List<Facture>>(emptyList())
    val factures: StateFlow<List<Facture>> = _factures.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

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
                val clientVoitures = api.getVoitures(clientId)
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
}