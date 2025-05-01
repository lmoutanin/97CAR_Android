package com.pm.carslim.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.carslim.data.models.FactureComplete
import com.pm.carslim.data.remote.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FactureDetailViewModel(private val api: ApiService) : ViewModel() {

    private val _factureDetail = MutableStateFlow<FactureComplete?>(null)
    val factureDetail: StateFlow<FactureComplete?> = _factureDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val TAG = "FactureDetailViewModel"

    // Méthode pour charger une facture spécifique
    fun loadFactureDetail(factureId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val factureDetails = api.getFactureById(factureId)
                _factureDetail.value = factureDetails
                Log.d(TAG, "Détails de la facture chargée : $factureDetails")
            } catch (e: Exception) {
                _factureDetail.value = null
                _error.value = "Impossible de charger la facture : ${e.message}"
                Log.e(TAG, "Erreur de chargement facture", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Méthode pour réinitialiser le détail de la facture
    fun resetFactureDetail() {
        _factureDetail.value = null
        Log.d(TAG, "Détails de la facture réinitialisés")
    }
}