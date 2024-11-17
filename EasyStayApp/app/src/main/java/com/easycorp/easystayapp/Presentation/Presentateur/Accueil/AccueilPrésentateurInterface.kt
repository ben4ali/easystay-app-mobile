package com.easycorp.easystayapp.Presentation.Presentateur.Accueil

import com.easycorp.easystayapp.Domaine.Entite.ChambreData

interface AccueilPrésentateurInterface {
    fun chargerReservationsCourte(clientId: Int)
    fun chargerChambres()
    fun ouvrirDetailsChambre(chambre: ChambreData)
    fun chargerChambresFavoris()
}