package com.easycorp.easystayapp.SourceDeDonnes

import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData

class SourceDeDonnéesException( message:String ) : Exception( message) {}

interface SourceDeDonnées {

    fun obtenirChambres(): List<ChambreData>
    fun obtenirChambreParType(typeChambre: String): List<ChambreData>
    fun obtenirChambreParId(id: Int): ChambreData
    fun obtenirChambresDisponibles(): List<ChambreData>

    fun ajouterClient(client: ClientData)
    fun obtenirClientParId(id: Int): ClientData
    fun modifierClient(client: ClientData)

    fun ajouterReservation(reservation: ReservationData)
    fun obtenirReservationsParClient(client: ClientData): List<ReservationData>
    fun obtenirReservationParId(id: Int): ReservationData
    fun obtenirReservationParChambre(chambre: ChambreData): List<ReservationData>
    fun suppressionReservation(reservation: ReservationData)
    fun modifierReservation(reservation: ReservationData)

}