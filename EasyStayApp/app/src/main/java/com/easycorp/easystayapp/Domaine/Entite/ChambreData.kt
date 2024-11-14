package com.easycorp.easystayapp.Domaine.Entite

data class ChambreData(
    val typeChambre: String,
    val description: String,
    val note: Float,
    val nombreAvis: Int,
    val commodites: List<String>,
    val prixParNuit: Double,
    val taxes: Double
) {
    fun prixTotal(): Double {
        return prixParNuit + taxes
    }
}