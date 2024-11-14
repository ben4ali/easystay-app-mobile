package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import android.widget.ListView
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Utilitaire.RéservationAdapter

class ListeRéservationPrésentateur(private val context: Context, private val listView: ListView) : ListeRéservationPrésentateurInterface {

    override fun chargerReservations(clientId: Int) {
        val reservations = Modèle.obtenirReservationsParClient(Modèle.obtenirClientParId(clientId))
        val adapter = RéservationAdapter(context, reservations, this)
        listView.adapter = adapter
    }

    override fun supprimerReservation(réservation: ReservationData) {
        Modèle.supprimerRéservation(réservation)
        chargerReservations(réservation.client.id)
    }
}