package com.easycorp.easystayapp.Presentation.Presentateur.ChambreDétail

import android.content.Context
import android.view.ViewGroup
import java.util.Calendar

interface ChambreDétailPresentateurInterface {

    val dateDebut: Calendar?
    val dateFin: Calendar?

    fun importerDetailsChambre()
    fun nouvelleDateChoisie(dateDebut: Calendar?, dateFin: Calendar?)
    fun afficherDetailsChambre(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double)
    fun naviguerVersReservation(dateDebut: String, dateFin: String)
    fun modifierDateAfficher(dateDebut: String, dateFin: String)
    fun afficherSelectionneurDates()
    fun gererBoutonReservationCliquer()
    fun gererBoutonRetourCliquer()
    fun verifierDatesValide(): Boolean
    fun créerIndicateurImages(contexte: Context, dotIndicatorLayout: ViewGroup, compte: Int)
    fun modifierIndicateurImages(positionSelectionner: Int)
}
