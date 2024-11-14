package com.easycorp.easystayapp.Presentation.Modele

import com.easycorp.easystayapp.Domaine.Entite.DonneesReservation
import com.easycorp.easystayapp.SourceDeDonnes.SourceReservationBidon

class ModeleVueReservation (val source: SourceReservationBidon) {
    var listeReservation : ArrayList<DonneesReservation> = ArrayList()

    fun changerListeReservation() : List<DonneesReservation> {
        listeReservation = source.getListeReservation() as ArrayList<DonneesReservation>
        return listeReservation
    }

    fun addReservation(reservation: DonneesReservation) : DonneesReservation {
        source.addReservation(reservation)
        return reservation
    }

    fun getListeReservation() : List<DonneesReservation> {
        return source.getListeReservation()
    }

    fun findReservationById(id: Int) : DonneesReservation? {
        return source.findRerservationById(id)
    }

    fun changerReservation(reservation: DonneesReservation) : DonneesReservation {
        source.updateReservation(reservation)
        return reservation
    }

    fun updateReservation(reservation: DonneesReservation) : DonneesReservation {
        source.updateReservation(reservation)
        return reservation
    }
}