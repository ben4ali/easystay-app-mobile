package com.easycorp.easystayapp.Presentation.Modele

import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.SourceDeDonnes.FavorisDAOImpl
import com.easycorp.easystayapp.SourceDeDonnes.SourceBidon

class Modèle private constructor() {

    @Volatile
    private var chambreChoisieId: Int? = null

    @Volatile
    private var reservationChoisieId: Int? = null

    companion object {
        @Volatile
        private var instance: Modèle? = null

        fun getInstance(): Modèle {
            return instance ?: synchronized(this) {
                instance ?: Modèle().also { instance = it }
            }
        }
    }

    private val sourceDeDonnées: SourceBidon = SourceBidon()

    // chambres
    fun obtenirChambres(): List<ChambreData> {
        return sourceDeDonnées.obtenirChambres()
    }

    fun obtenirChambreParType(type: String): List<ChambreData> {
        return sourceDeDonnées.obtenirChambreParType(type)
    }

    fun obtenirChambreParId(id: Int): ChambreData {
        return sourceDeDonnées.obtenirChambreParId(id)
    }

    fun obtenirChambresDisponibles(): List<ChambreData> {
        return sourceDeDonnées.obtenirChambresDisponibles()
    }

    fun setChambreChoisieId(id: Int) {
        chambreChoisieId = id
    }


    fun getChambreChoisieId(): Int? {
        return chambreChoisieId
    }

    // reservations
    fun setReservationChoisieId(id: Int) {
        reservationChoisieId = id
    }

    fun getReservationChoisieId(): Int? {
        return reservationChoisieId
    }

    fun obtenirClientParId(id: Int): ClientData {
        return sourceDeDonnées.obtenirClientParId(id)
    }

    fun modifierClient(client: ClientData) {
        sourceDeDonnées.modifierClient(client)
    }

    fun ajouterReservation(réservation: ReservationData) {
        sourceDeDonnées.ajouterReservation(réservation)
    }

    fun obtenirReservationsParClient(client: ClientData): List<ReservationData> {
        return sourceDeDonnées.obtenirReservationsParClient(client)
    }

    fun obtenirReservationParId(id: Int): ReservationData {
        return sourceDeDonnées.obtenirReservationParId(id)
    }

    fun obtenirReservationParChambre(chambre: ChambreData): List<ReservationData> {
        return sourceDeDonnées.obtenirReservationParChambre(chambre)
    }

    fun supprimerRéservation(réservation: ReservationData) {
        sourceDeDonnées.suppressionReservation(réservation)
    }

    fun modifierReservation(réservation: ReservationData) {
        sourceDeDonnées.modifierReservation(réservation)
    }
}