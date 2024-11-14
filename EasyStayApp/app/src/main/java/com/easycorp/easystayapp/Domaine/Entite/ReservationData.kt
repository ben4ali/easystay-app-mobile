package com.easycorp.easystayapp.Domaine.Entite

data class DonneesReservation(
    val id : Int?,
    var typeChambre: String,
    var description: String,
    var note: Float,
    var nombreAvis: Int,
    var commodites: List<String>,
    var dates: String,
    var invites: String,
    var prixParNuit: Double,
    var nuits: Int,
    var taxes: Double
) {
    fun prixTotal(): Double {
        return (prixParNuit * nuits) + taxes
    }
}
