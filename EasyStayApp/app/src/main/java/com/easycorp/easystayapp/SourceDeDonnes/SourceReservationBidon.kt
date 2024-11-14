package com.easycorp.easystayapp.SourceDeDonnes

import com.easycorp.easystayapp.Domaine.Entite.DonneesReservation
import com.easycorp.easystayapp.Domaine.Interface.SourceReservation

class SourceReservationBidon : SourceReservation {
    override var listeReservation: ArrayList<DonneesReservation> = ArrayList()

    init {
        listeReservation.add(
            DonneesReservation(
                1,
                "Chambre simple",
                "Chambre simple avec lit double",
                4.5f,
                10,
                listOf("Wifi", "TV", "Climatisation"),
                "2021-10-01",
                "2",
                100.0,
                3,
                10.0
            )
        )
        listeReservation.add(
            DonneesReservation(
                2,
                "Chambre double",
                "Chambre double avec lit double",
                4.8f,
                5,
                listOf("Wifi", "TV", "Climatisation"),
                "2021-10-01",
                "2",
                150.0,
                3,
                10.0
            )
        )
        listeReservation.add(
            DonneesReservation(
                3,
                "Chambre suite",
                "Chambre suite avec lit double",
                4.2f,
                15,
                listOf("Wifi", "TV", "Climatisation"),
                "2021-10-01",
                "2",
                200.0,
                3,
                10.0
            )
        )
        listeReservation.add(
            DonneesReservation(
                4,
                "Chambre suite",
                "Chambre suite avec lit double",
                4.2f,
                15,
                listOf("Wifi", "TV", "Climatisation"),
                "2021-10-01",
                "2",
                200.0,
                3,
                10.0
            )
        )
        listeReservation.add(
            DonneesReservation(
                5,
                "Chambre suite",
                "Chambre suite avec lit double",
                4.2f,
                15,
                listOf("Wifi", "TV", "Climatisation"),
                "2021-10-01",
                "2",
                200.0,
                3,
                10.0
            )
        )
    }

    override fun getListeReservation(): List<DonneesReservation> {
        return listeReservation
    }

    override fun addReservation(reservation: DonneesReservation) {
        listeReservation.add(reservation)
    }

    override fun updateReservation(reservation: DonneesReservation) {
        var reservationTrouvée = listeReservation.find { it == reservation }
        reservationTrouvée?.typeChambre = reservation.typeChambre
        reservationTrouvée?.description = reservation.description
        reservationTrouvée?.note = reservation.note
        reservationTrouvée?.nombreAvis = reservation.nombreAvis
        reservationTrouvée?.commodites = reservation.commodites
        reservationTrouvée?.dates = reservation.dates
        reservationTrouvée?.invites = reservation.invites
        reservationTrouvée?.prixParNuit = reservation.prixParNuit
        reservationTrouvée?.nuits = reservation.nuits
        reservationTrouvée?.taxes = reservation.taxes

    }

    override fun findRerservationById(id: Int): DonneesReservation? {
        return listeReservation.find { it.id == id }
    }

    override fun deleteReservationById(id: Int) {
        listeReservation.removeIf { it.id == id }
    }

}