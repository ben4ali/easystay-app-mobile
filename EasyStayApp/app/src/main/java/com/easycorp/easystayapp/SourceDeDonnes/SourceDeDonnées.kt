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
    fun rechercherChambresParMotCle(keyword: String): List<ChambreData>?
    fun filtrerChambres(type: String?, prix: Int?): List<ChambreData>?
    fun suppressionReservation(reservation: ReservationData)
    fun modifierReservation(reservation: ReservationData)
    suspend fun modifierClientImage(newImage: String, clientId: Int)
    fun modifierClientPrenom(clientId: Int,newPrenom: String)
    fun modifierClientCourriel(clientId: Int, newCourriel: String)
    fun modifierClientNom(clientId: Int, newNom: String)


}