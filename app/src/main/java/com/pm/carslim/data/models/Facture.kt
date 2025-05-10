package com.pm.carslim.data.models


data class Facture(
    val id_facture: Int? = null, // Nullable car l'ID est généré par la base
    val date: String,
    val client_id: String,
    val voiture_id: String,
    val montant: String,
)