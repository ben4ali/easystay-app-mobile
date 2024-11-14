package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.easycorp.easystayapp.Presentation.Presentateur.ListeRéservationPrésentateur
import com.easycorp.easystayapp.R

class ListeReservationsVue : Fragment() {

    private lateinit var listeReservations: ListView
    private lateinit var présentateur: ListeRéservationPrésentateur

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_liste_reservations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listeReservations = view.findViewById(R.id.RList)
        présentateur = ListeRéservationPrésentateur(requireContext(), listeReservations)
        présentateur.chargerReservations(1)
    }

    override fun onResume() {
        super.onResume()
        présentateur.chargerReservations(1)
    }
}