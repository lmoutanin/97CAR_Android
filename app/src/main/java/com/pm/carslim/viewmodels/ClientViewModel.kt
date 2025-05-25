package com.pm.carslim.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.carslim.data.models.Client
import com.pm.carslim.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

open class ClientViewModel : ViewModel() {

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    open val clients: StateFlow<List<Client>> = _clients  // Liste des clients

    // Client sélectionné
    private val _selectedClient = MutableStateFlow<Client?>(null)

    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message // Message


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

    // Méthode pour sélectionner un client
    fun selectClient(client: Client?) {
        _selectedClient.value = client
    }

    //Methode pour ajout un client
    fun addClient(client: Client) {
        viewModelScope.launch {
            try {
                addMessage(RetrofitInstance.api.addClient(client))
                Log.d("API", "ajout d'un client")
                fetchClients() // Rafraîchir la liste après ajout

            } catch (e: Exception) {
                addMessage()
                Log.e("API_ERROR", "Erreur lors de l'ajout d'un client: ${e.message}").toString()
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

