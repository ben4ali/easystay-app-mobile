package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import android.widget.ListView
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Utilitaire.RéservationCourtAdapter
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter

class AccueilPrésentateur(private val context: Context, private val listViewReservations: ListView, private val listViewChambres: ListView) {

    private val modèle: Modèle = Modèle()

    fun chargerReservationsCourte(clientId: Int) {
        val reservations = modèle.obtenirReservationsParClient(modèle.obtenirClientParId(clientId))
        val adapter = RéservationCourtAdapter(context, reservations)
        listViewReservations.adapter = adapter
    }

    fun chargerChambres() {
        val chambres = modèle.obtenirChambres()
        val adapter = ChambreAdapter(context, chambres) { chambre ->

        }
        listViewChambres.adapter = adapter
    }
}