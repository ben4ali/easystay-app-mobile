package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.ListeReservationsVue
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Utilitaire.RéservationAdapter

class ListeRéservationPrésentateur(private val context: Context, private val listView: ListView, private val vue:ListeReservationsVue) : ListeRéservationPrésentateurInterface {

    override fun chargerReservations(clientId: Int) {
        val reservations = Modèle.obtenirReservationsParClient(Modèle.obtenirClientParId(clientId))
        val adapter = RéservationAdapter(context, reservations, this)
        listView.adapter = adapter
    }

    override fun supprimerReservation(réservation: ReservationData) {
        Modèle.supprimerRéservation(réservation)
        chargerReservations(réservation.client.id)
    }

    fun ouvrirDetailsReservation(reservation: ReservationData, view: View) {
        val bundle = Bundle().apply {
            putInt("reservationId", reservation.id?:0)
        }
        vue.findNavController().navigate(R.id.action_fragment_listeReservations_to_reserverFragment, bundle)

    }
}