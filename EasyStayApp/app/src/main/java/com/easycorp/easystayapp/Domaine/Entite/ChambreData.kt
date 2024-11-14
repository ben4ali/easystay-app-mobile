package com.easycorp.easystayapp.Domaine.Entite

data class ChambreData(
    val id: Int,
    val typeChambre: String,
    val description: String,
    val note: Float,
    val nombreAvis: Int,
    val commodites: List<String>,
    val prixParNuit: Double,
    val favoris: Boolean=false,
)