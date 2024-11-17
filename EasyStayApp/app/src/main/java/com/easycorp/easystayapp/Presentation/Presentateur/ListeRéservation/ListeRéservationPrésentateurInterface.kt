package com.easycorp.easystayapp.Presentation.Presentateur.ListeRéservation

import com.easycorp.easystayapp.Domaine.Entite.ReservationData

interface ListeRéservationPrésentateurInterface {
    fun chargerReservations(clientId: Int)
    fun supprimerReservation(réservation: ReservationData)
}