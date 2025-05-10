package com.pm.carslim.data.models

data class Voiture(
    val id_voiture: Int? = null,
    val annee: String,
    val marque:  String,
    val modele:  String,
    val immatriculation:  String,
    val kilometrage: String,
    val client_id: String,

    )