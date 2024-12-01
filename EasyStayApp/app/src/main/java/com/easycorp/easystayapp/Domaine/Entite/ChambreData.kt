package com.easycorp.easystayapp.Domaine.Entite

data class ChambreData(
    val id: Int,
    val typeChambre: String,
    val prixParNuit: Double,
    val statutDisponibilite: String,
    val statutNettoyage: String,
    val note: Int,
    val nombreAvis: Int,
    val caracteristique: List<String>,
    val equipement: List<String>,
    val photos: List<String> = listOf()
)