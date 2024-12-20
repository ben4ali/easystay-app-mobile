package com.easycorp.easystayapp.Domaine.Entite

data class ClientData(
    var id: Int,
    var courriel: String,
    var prénom: String,
    var nom: String,
    var photo: String?
)