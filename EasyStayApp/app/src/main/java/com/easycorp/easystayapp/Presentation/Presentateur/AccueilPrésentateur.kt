package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import android.widget.ListView
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Utilitaire.RéservationAdapter
import com.easycorp.easystayapp.Utilitaire.RéservationCourtAdapter

class AccueilPrésentateur(private val context: Context, private val listView: ListView) {

    private val modèle: Modèle = Modèle()

    fun chargerReservationsCourte(clientId: Int) {
        val reservations = modèle.obtenirReservationsParClient(modèle.obtenirClientParId(clientId))
        val adapter = RéservationCourtAdapter(context, reservations)
        listView.adapter = adapter
    }
}