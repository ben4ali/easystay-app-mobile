package com.easycorp.easystayapp.Presentation.Modele

import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.SourceDeDonnes.SourceBidon

class Modèle {

    private val sourceDeDonnées: SourceBidon = SourceBidon()

    //chambres
    fun obtenirChambres() : List<ChambreData>{
        return sourceDeDonnées.obtenirChambres()
    }
    fun obtenirChambreParType(type: String) : List<ChambreData>{
        return sourceDeDonnées.obtenirChambreParType(type)
    }
    fun obtenirChambreParId(id: Int) : ChambreData{
        return sourceDeDonnées.obtenirChambreParId(id)
    }
    fun obtenirChambresDisponibles() : List<ChambreData>{
        return sourceDeDonnées.obtenirChambresDisponibles()
    }

    //clients
    fun ajouterClient(client: ClientData){
        sourceDeDonnées.ajouterClient(client)
    }
    fun obtenirClientParId(id: Int) : ClientData{
        return sourceDeDonnées.obtenirClientParId(id)
    }

    //réservations
    fun ajouterReservation(réservation: ReservationData) {
        sourceDeDonnées.ajouterReservation(réservation)
    }
    fun obtenirReservationsParClient(client: ClientData) : List<ReservationData> {
        return sourceDeDonnées.obtenirReservationsParClient(client)
    }
    fun obtenirReservationParId(id: Int) : ReservationData{
        return sourceDeDonnées.obtenirReservationParId(id)
    }
    fun obtenirReservationParChambre(chambre: ChambreData) : List<ReservationData> {
        return sourceDeDonnées.obtenirReservationParChambre(chambre)
    }
    fun suppressionReservation(réservation: ReservationData) {
        sourceDeDonnées.suppressionReservation(réservation)
    }
    fun modifierReservation(réservation: ReservationData) {
        sourceDeDonnées.modifierReservation(réservation)
    }


}