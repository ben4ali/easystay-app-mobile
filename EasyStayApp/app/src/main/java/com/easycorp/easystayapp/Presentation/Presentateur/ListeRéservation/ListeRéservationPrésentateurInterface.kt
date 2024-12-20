package com.easycorp.easystayapp.Presentation.Presentateur.ListeRéservation

import com.easycorp.easystayapp.Domaine.Entite.ReservationData

interface ListeRéservationPrésentateurInterface {
    suspend fun chargerReservations(clientId: Int)
    suspend fun supprimerReservation(réservation: ReservationData)
}