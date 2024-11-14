package com.easycorp.easystayapp.Domaine.Entite

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class ReservationData(
        val id : Int?,
        var client: ClientData,
        var chambre: ChambreData,
        var dateDébut: String,
        var dateFin: String,
    )
{
    fun prixTotal(): Double {
        return (chambre.prixParNuit * calculerNombreDeNuits()) * 1.15
    }

    fun calculerNombreDeNuits(): Int {
        val dateDebut = LocalDate.parse(dateDébut)
        val dateFin = LocalDate.parse(dateFin)
        return ChronoUnit.DAYS.between(dateDebut, dateFin).toInt()
    }

    fun obtenirNombreDeJours(): Int {
        val dateDebut = LocalDate.parse(dateDébut)
        val dateAujourdhui = LocalDate.now()
        return ChronoUnit.DAYS.between(dateAujourdhui, dateDebut).toInt()
    }
}
