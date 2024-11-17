package com.easycorp.easystayapp.Presentation.Presentateur.Résérver

import java.util.Calendar

interface ReserverPresentateurInterface {

    val dateDebut: Calendar?
    val dateFin: Calendar?
    val dateDebutInitiale: Calendar?
    val dateFinInitiale: Calendar?

    fun ouvrirDetailsChambre()
    fun afficherSelectionneurDates()
    fun calculerPrixTotale(dateDebut: Calendar?, dateFin: Calendar?, prixParNuit: Double?)
    fun dateFormatage(dateTexte: String): String

}