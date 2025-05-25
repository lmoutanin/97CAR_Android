package com.pm.carslim.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.carslim.data.models.Client
import com.pm.carslim.data.models.Voiture
import com.pm.carslim.data.models.VoitureComplete
import com.pm.carslim.data.remote.ApiService
import com.pm.carslim.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class VoitureDetailViewModel(private val api: ApiService) : ViewModel() {

    private val _voitureDetail = MutableStateFlow<VoitureComplete?>(null)
    val voitureDetail: StateFlow<VoitureComplete?> = _voitureDetail.asStateFlow()

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    open val clients: StateFlow<List<Client>> = _clients    // Liste des clients

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message // Message

    private val TAG = "VoitureDetailViewModel"


    init {
        fetchClients()
    }

    // Methode pour obtenir tout les clients
    fun fetchClients() {
        viewModelScope.launch {
            try {
                _isLoading.value = true // pour l'effet de chargement
                val clientsResponse = RetrofitInstance.api.getClients()
                _clients.value = RetrofitInstance.api.getClients()
                _isLoading.value = false
                Log.d("API_RESPONSE", "Clients reçus: ${clientsResponse.size}")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Erreur lors de la récupération: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Méthode pour charger une voiture spécifique
    fun loadVoitureDetail(voitureId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val voitureDetails = api.getVoitureById(voitureId)
                _voitureDetail.value = voitureDetails
                Log.d(TAG, "Détails de la voiture chargée : $voitureDetails")
            } catch (e: Exception) {
                _voitureDetail.value = null
                _error.value = "Impossible de charger la voiture : ${e.message}"
                Log.e(TAG, "Erreur de chargement voiture", e)
            } finally {
                _isLoading.value = false
            }


        }
    }


    // Methode pour supprimer  une voiture
    open fun deleteVoiture(idVoiture: Int) {
        viewModelScope.launch {
            try {
                addMessage(RetrofitInstance.api.deleteVoitureById(idVoiture))
                Log.d("API", "Voiture supprimé avec succès")

            } catch (e: Exception) {
                addMessage()
                Log.e("API_ERROR", "Erreur lors de la suppression d'une voiture: ${e.message}")
                e.printStackTrace()

            }
        }

    }

    //Methode pour modifier une voiture
    open fun editVoiture(voiture: Voiture) {
        viewModelScope.launch {
            try {
                addMessage(
                    RetrofitInstance.api.editVoitureById(
                        voiture.id_voiture!!.toInt(),
                        voiture
                    )
                )
                Log.d("API", "modification une voiture")

            } catch (e: Exception) {
                addMessage()
                Log.e("API_ERROR", "Erreur lors de  la modification d'une voiture: ${e.message}")
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