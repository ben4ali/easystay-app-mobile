package com.easycorp.easystayapp.Presentation.Presentateur

import com.easycorp.easystayapp.Domaine.Entite.ReservationData

interface ListeRéservationPrésentateurInterface {
    fun chargerReservations(clientId: Int)
    fun supprimerReservation(réservation: ReservationData)
}