package com.easycorp.easystayapp.SourceDeDonnes

import android.graphics.Bitmap
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData

class SourceDeDonnéesException( message:String ) : Exception( message) {}

interface SourceDeDonnées {

    fun obtenirChambres(): List<ChambreData>?
    fun obtenirChambreParType(typeChambre: String): List<ChambreData>?
    fun obtenirChambreParId(id: Int): ChambreData?
    fun obtenirChambresDisponibles(): List<ChambreData>?

    fun ajouterClient(client: ClientData)
    fun obtenirClientParId(id: Int): ClientData
    fun modifierClient(client: ClientData)

    suspend fun obtenirToutesLesReservations(): List<ReservationData>?
    suspend fun ajouterReservation(reservationData: ReservationData, clientData: ClientData, chambre: ChambreData)
    suspend fun obtenirReservationsParClient(client: ClientData): List<ReservationData>
    suspend fun obtenirReservationParId(id: Int): ReservationData?
    suspend fun obtenirReservationParChambre(chambre: ChambreData): List<ReservationData>
    fun suppressionReservation(reservation: ReservationData)
    fun modifierReservation(reservation: ReservationData)
    fun modifierClientName(newName: String)
    fun modifierClientSurname(newSurname: String)
    fun modifierClientEmail(newEmail: String)
    fun modifierClientImage(newImage: String)

}