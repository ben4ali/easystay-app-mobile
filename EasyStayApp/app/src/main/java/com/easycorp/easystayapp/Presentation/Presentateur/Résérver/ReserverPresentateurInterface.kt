package com.easycorp.easystayapp.Presentation.Presentateur.Résérver

import java.util.Calendar

interface ReserverPresentateurInterface {

    fun ouvrirDetailsRéservation()
    fun afficherSelectionneurDates()
    fun calculerPrixTotale(dateDebut: Calendar?, dateFin: Calendar?, prixParNuit: Double?)
    fun dateFormatage(dateTexte: String): String
    fun gererConfirmationReservation()

}