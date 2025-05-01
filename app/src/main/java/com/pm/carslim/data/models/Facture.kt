package com.pm.carslim.data.models


data class Facture(
    val id_facture: Int,
    val date: String,
    val montant: String,
    val client: Int,
    val voiture: Int,
)