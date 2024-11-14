package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.easycorp.easystayapp.Presentation.Presentateur.AccueilPrésentateur
import com.easycorp.easystayapp.R

class AccueilVue : Fragment() {

    private lateinit var listeReservations: ListView
    private lateinit var présentateur: AccueilPrésentateur

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_accueil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listeReservations = view.findViewById(R.id.RAList)
        présentateur = AccueilPrésentateur(requireContext(), listeReservations)
        présentateur.chargerReservationsCourte(1)
    }

    override fun onResume() {
        super.onResume()
        présentateur.chargerReservationsCourte(1)
    }
}
