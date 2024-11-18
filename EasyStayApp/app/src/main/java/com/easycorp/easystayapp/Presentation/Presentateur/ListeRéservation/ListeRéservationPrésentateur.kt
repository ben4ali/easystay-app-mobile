package com.easycorp.easystayapp.Presentation.Presentateur.ListeRéservation

import android.content.Context
import android.view.View
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.ListeReservationsVue
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Utilitaire.RéservationAdapter

class ListeRéservationPrésentateur(
    private val context: Context,
    private val listView: ListView,
    private val vue: ListeReservationsVue
) : ListeRéservationPrésentateurInterface {

    private val modèle = Modèle.getInstance()

    override fun chargerReservations(clientId: Int) {
        val reservations = modèle.obtenirReservationsParClient(modèle.obtenirClientParId(clientId))
        val adapter = RéservationAdapter(context, reservations, this)
        listView.adapter = adapter
    }

    override fun supprimerReservation(réservation: ReservationData) {
        modèle.supprimerRéservation(réservation)
        chargerReservations(réservation.client.id)
    }

    fun ouvrirDetailsReservation(reservation: ReservationData, view: View) {
        reservation.id?.let { id ->
            modèle.setReservationChoisieId(id)
            modèle.setDates(reservation.dateDébut, reservation.dateFin)
            vue.findNavController().navigate(R.id.action_fragment_listeReservations_to_reserverFragment)
        }
    }
}