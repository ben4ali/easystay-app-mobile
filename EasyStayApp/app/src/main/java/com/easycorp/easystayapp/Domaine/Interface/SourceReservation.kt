package com.easycorp.easystayapp.Domaine.Interface

import com.easycorp.easystayapp.Domaine.Entite.DonneesReservation

interface SourceReservation {
    var listeReservation: ArrayList<DonneesReservation>

    fun getListeReservation(): List<DonneesReservation>

    fun addReservation(reservation: DonneesReservation)

    fun updateReservation(reservation: DonneesReservation)

    fun findRerservationById(id: Int): DonneesReservation?

    fun deleteReservationById(id: Int)

}