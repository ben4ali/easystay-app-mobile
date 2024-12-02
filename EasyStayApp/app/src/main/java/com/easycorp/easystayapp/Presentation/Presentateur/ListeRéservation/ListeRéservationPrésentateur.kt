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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListeRéservationPrésentateur(
    private val context: Context,
    private val listView: ListView,
    private val vue: ListeReservationsVue
) : ListeRéservationPrésentateurInterface {

    private val modèle = Modèle.getInstance(context)

    override suspend fun chargerReservations(clientId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val reservations = modèle.obtenirReservationsParClient(modèle.obtenirClientParId(clientId))
            CoroutineScope(Dispatchers.Main).launch {
                val adapter = RéservationAdapter(context, reservations, this@ListeRéservationPrésentateur)
                listView.adapter = adapter
            }

        }

    }

    override suspend fun supprimerReservation(réservation: ReservationData) {
        CoroutineScope(Dispatchers.IO).launch {
            modèle.supprimerRéservation(réservation)
            CoroutineScope(Dispatchers.Main).launch {
                chargerReservations(réservation.client.id)
            }
        }
    }

    fun ouvrirDetailsReservation(reservation: ReservationData, view: View) {
        reservation.id?.let { id ->
            modèle.setReservationChoisieId(id)
            modèle.setDates(reservation.dateDébut, reservation.dateFin)
            modèle.setCheminVersReservation(R.id.action_reserverFragment_to_fragment_listeReservations)
            vue.findNavController().navigate(R.id.action_fragment_listeReservations_to_reserverFragment)
        }
    }
}