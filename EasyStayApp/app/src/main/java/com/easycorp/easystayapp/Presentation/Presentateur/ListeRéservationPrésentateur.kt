package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import android.widget.ListView
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Utilitaire.RéservationAdapter

class ListeRéservationPrésentateur(private val context: Context, private val listView: ListView) {

    private val modèle: Modèle = Modèle()

    fun chargerReservations(clientId: Int) {
        val reservations = modèle.obtenirReservationsParClient(modèle.obtenirClientParId(clientId))
        val adapter = RéservationAdapter(context, reservations, this)
        listView.adapter = adapter
    }

    fun supprimerReservation(réservation: ReservationData) {
        modèle.supprimerRéservation(réservation)
        chargerReservations(réservation.client.id)
    }
}