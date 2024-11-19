package com.easycorp.easystayapp.Presentation.Presentateur.Résérver

import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.R
import java.util.Calendar

interface ReserverPresentateurInterface {

    fun ouvrirDetailsRéservation()
    fun afficherSelectionneurDates()
    fun calculerPrixTotale()
    fun dateFormatage(dateTexte: String): String
    fun gererConfirmationReservation()
    fun gererBoutonRetourCliquer()

}