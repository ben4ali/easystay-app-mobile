package com.easycorp.easystayapp.Presentation.Presentateur.Accueil

import androidx.viewpager2.widget.ViewPager2
import com.easycorp.easystayapp.Domaine.Entite.ChambreData

interface AccueilPrésentateurInterface {
    suspend fun chargerReservationsCourte(clientId: Int, viewPager: ViewPager2)
    fun chargerChambres()
    fun ouvrirDetailsChambre(chambre: ChambreData)
    fun chargerChambresFavoris()
}