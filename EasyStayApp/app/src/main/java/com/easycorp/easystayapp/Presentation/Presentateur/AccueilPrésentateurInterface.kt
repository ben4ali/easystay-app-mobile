package com.easycorp.easystayapp.Presentation.Presentateur

import com.easycorp.easystayapp.Domaine.Entite.ChambreData

interface AccueilPrésentateurInterface {
    fun chargerReservationsCourte(clientId: Int)
    fun chargerChambres()
    fun ouvrirDetailsChambre(chambre: ChambreData)
    fun chargerChambresFavoris()
}