package com.pm.carslim.data.models

data class Client(
    val id_client: Int? = null, // Nullable car l'ID est généré par la base
    val nom: String,
    val prenom: String,
    val telephone: String?,
    val mel: String?,
    val adresse: String?,
    val code_postal: String?,
    val ville: String?
)