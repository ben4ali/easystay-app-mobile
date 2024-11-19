package com.easycorp.easystayapp.Domaine.Entite

import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
        val dateDebut = LocalDate.parse(dateDébut, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val dateFin = LocalDate.parse(dateFin, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        return ChronoUnit.DAYS.between(dateDebut, dateFin).toInt()
    }

    fun obtenirNombreDeJours(): Int {
        val dateDebut = LocalDate.parse(dateDébut, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val dateAujourdhui = LocalDate.now()
        return ChronoUnit.DAYS.between(dateAujourdhui, dateDebut).toInt()
    }
}
