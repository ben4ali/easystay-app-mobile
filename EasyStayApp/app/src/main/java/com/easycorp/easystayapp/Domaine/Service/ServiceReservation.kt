package com.easycorp.easystayapp.Domaine.Service

import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.SourceDeDonnes.SourceDeDonnées

class ServiceReservation(private val sourceDeDonnées: SourceDeDonnées) {

    fun obtenirToutesLesReservations(): List<ReservationData>? {
        return sourceDeDonnées.obtenirToutesLesReservations()
    }

    fun ajouterReservation(réservation: ReservationData) {
        sourceDeDonnées.ajouterReservation(réservation)
    }

    suspend fun obtenirReservationsParClient(client: ClientData): List<ReservationData> {
        return sourceDeDonnées.obtenirReservationsParClient(client)
    }

    fun obtenirReservationParId(id: Int): ReservationData {
        return sourceDeDonnées.obtenirReservationParId(id)
    }

    suspend fun obtenirReservationParChambre(chambre: ChambreData): List<ReservationData> {
        return sourceDeDonnées.obtenirReservationParChambre(chambre)
    }

    fun supprimerRéservation(réservation: ReservationData) {
        sourceDeDonnées.suppressionReservation(réservation)
    }

    fun modifierReservation(réservation: ReservationData) {
        sourceDeDonnées.modifierReservation(réservation)
    }
}