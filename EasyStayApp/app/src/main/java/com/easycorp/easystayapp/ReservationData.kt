package com.easycorp.easystayapp

data class DonneesReservation(
    val typeChambre: String,
    val description: String,
    val note: Float,
    val nombreAvis: Int,
    val commodites: List<String>,
    val dates: String,
    val invites: String,
    val prixParNuit: Double,
    val nuits: Int,
    val taxes: Double
) {
    fun prixTotal(): Double {
        return (prixParNuit * nuits) + taxes
    }
}
