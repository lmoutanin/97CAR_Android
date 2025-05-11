package com.pm.carslim.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.carslim.data.models.Client
import com.pm.carslim.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ClientViewModel : ViewModel() {
    // Liste des clients
    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    open val clients: StateFlow<List<Client>> = _clients

    // Client sélectionné
    private val _selectedClient = MutableStateFlow<Client?>(null)

    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchClients()
    }

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

    // Méthode pour sélectionner un client
    fun selectClient(client: Client?) {
        _selectedClient.value = client
    }

    open fun addClient(client: Client) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.addClient(client)
                Log.d("API", "ajout d'un client")
                fetchClients() // Rafraîchir la liste après ajout
            } catch (e: Exception) {
                Log.e("API_ERROR", "Erreur lors de l'ajout d'un client: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    open fun editClient(client: Client) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.editClient(client.id_client.toString(),client)
                Log.d("API", "modification un client")
                fetchClients() // Rafraîchir la liste après la modification
            } catch (e: Exception) {
                Log.e("API_ERROR", "Erreur lors de  la modification d'un client: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    open fun deleteClient(idClient: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteClientById(idClient)
                if (response.isSuccessful) {
                    Log.d("API", "Client supprimé avec succès")
                    fetchClients() // Rafraîchir la liste après la suppression
                } else {
                    Log.e("API_ERROR", "Échec de la suppression: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Erreur lors de la suppression d'un client: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

