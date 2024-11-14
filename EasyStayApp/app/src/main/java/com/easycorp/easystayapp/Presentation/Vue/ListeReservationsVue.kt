package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.easycorp.easystayapp.Presentation.Modele.ModeleVueReservation
import com.easycorp.easystayapp.Presentation.Presentateur.ListeReservationsPresentateur
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.SourceDeDonnes.SourceReservationBidon


class ListeReservationsVue : Fragment() {

    private var presentateur : ListeReservationsPresentateur? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_liste_reservations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val modele = ModeleVueReservation(SourceReservationBidon())
        presentateur = ListeReservationsPresentateur(this, modele)


    }
}