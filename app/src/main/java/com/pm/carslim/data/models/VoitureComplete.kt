package com.pm.carslim.data.models

data class VoitureComplete(
    val id_voiture : Int,
    val annee : String ,
    val marque : String ,
    val modele : String ,
    val immatriculation : String ,
    val kilometrage : String ,
    val client : Client,
    val factures: List<Facture>,
    val reparations: List<Reparation>
)