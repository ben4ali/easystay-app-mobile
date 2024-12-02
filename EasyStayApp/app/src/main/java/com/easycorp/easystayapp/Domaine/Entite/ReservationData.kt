package com.easycorp.easystayapp.Domaine.Entite

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class ReservationData(
    val id: Int,
    var client: ClientData,
    var chambreNumero: String,
    var dateDébut: String,
    var dateFin: String,
    var prixTotal: Double,
    var statut: String,
    var methodePaiement: String,
    var statusPaiement: Boolean,
    var datePaiement: String,
    var chambre: ChambreData
) {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun calculerNombreDeNuits(): Int {
        val dateDebut = LocalDate.parse(dateDébut, formatter)
        val dateFin = LocalDate.parse(dateFin, formatter)
        return ChronoUnit.DAYS.between(dateDebut, dateFin).toInt()
    }

    fun obtenirNombreDeJours(): Int {
        val dateDebut = LocalDate.parse(dateDébut, formatter)
        val dateAujourdhui = LocalDate.now()
        return ChronoUnit.DAYS.between(dateAujourdhui, dateDebut).toInt()
    }
}