package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2
import com.easycorp.easystayapp.Presentation.Presentateur.Accueil.AccueilPrésentateur
import com.easycorp.easystayapp.R
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class AccueilVue : Fragment() {

     lateinit var listeReservations: ViewPager2
     lateinit var listeChambres: ListView
     lateinit var textFavoris : TextView
     lateinit var présentateur: AccueilPrésentateur
     lateinit var dotsIndicator: DotsIndicator
     lateinit var heroImage : CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_accueil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listeChambres = view.findViewById(R.id.RAListChambre)
        listeReservations = view.findViewById(R.id.RAList)
        textFavoris = view.findViewById(R.id.textFavoris)
        dotsIndicator = view.findViewById(R.id.dots_indicator)
        heroImage = view.findViewById(R.id.HeroCard)
        présentateur = AccueilPrésentateur(requireContext(), listeReservations, listeChambres, this, dotsIndicator)
        présentateur.chargerReservationsCourte(1, listeReservations)
        présentateur.chargerChambres()
        présentateur.chargerChambresFavoris()
    }

    override fun onResume() {
        super.onResume()
        présentateur.chargerReservationsCourte(1, listeReservations)
        présentateur.chargerChambres()
        présentateur.chargerChambresFavoris()
    }
}