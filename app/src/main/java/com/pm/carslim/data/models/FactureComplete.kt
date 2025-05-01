package com.pm.carslim.data.models

data class FactureComplete(
    val id_facture: Int,
    val date_facture: String,
    val montant: String,
    val client: Client,
    val voiture: Voiture,
    val reparations: List<Reparation>
)