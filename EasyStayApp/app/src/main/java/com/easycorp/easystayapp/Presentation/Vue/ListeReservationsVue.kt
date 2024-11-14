package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter
import com.easycorp.easystayapp.Utilitaire.RéservationAdapter


class ListeReservationsVue : Fragment() {

    private lateinit var listeReservations: ListView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_liste_reservations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listeReservations = view.findViewById(R.id.RList)
        var modele : Modèle = Modèle()
        val adapter = RéservationAdapter(requireContext(),modele.obtenirReservationsParClient(modele.obtenirClientParId(1)))
        listeReservations.adapter = adapter
    }
}