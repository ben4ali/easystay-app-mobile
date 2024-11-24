package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.easycorp.easystayapp.Presentation.Presentateur.ChambreDétail.ChambreDétailPresentateur
import com.easycorp.easystayapp.Presentation.Presentateur.ChambreDétail.ChambreDétailPresentateurInterface
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Utilitaire.ImageSliderAdapter
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class ChambreDetailsVue : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var dotIndicatorLayout: LinearLayout
    private lateinit var dateSelectionAffichage: LinearLayout
    private lateinit var dateAfficher: TextView
    private lateinit var typeChambreTexte: TextView
    private lateinit var descriptionChambreTexte: TextView
    private lateinit var noteChambreTexte: TextView
    private lateinit var prixChambreTexte: TextView
    private lateinit var TitreChambreTexte: TextView
    private lateinit var boutonRetour: ImageView
    private lateinit var boutonRéserver: Button
    private lateinit var wormDotsIndicator: WormDotsIndicator

    private lateinit var presentateur: ChambreDétailPresentateurInterface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vue = inflater.inflate(R.layout.fragment_chambre_details, container, false)

        viewPager = vue.findViewById(R.id.viewPagerImages)
        wormDotsIndicator = vue.findViewById(R.id.worm_dots_indicator)
        dateSelectionAffichage = vue.findViewById(R.id.datePickerLayout)
        dateAfficher = vue.findViewById(R.id.dateTextView)
        typeChambreTexte = vue.findViewById(R.id.bedTypeTextView)
        TitreChambreTexte = vue.findViewById(R.id.roomTitleTextView)
        descriptionChambreTexte = vue.findViewById(R.id.roomDescriptionTitle)
        noteChambreTexte = vue.findViewById(R.id.reviewCountTextView)
        prixChambreTexte = vue.findViewById(R.id.priceTextView)
        boutonRetour = vue.findViewById(R.id.backButton)
        boutonRéserver = vue.findViewById(R.id.bookButton)

        presentateur = ChambreDétailPresentateur(this, requireContext(), wormDotsIndicator)

        presentateur.importerDetailsChambre()

        val images = intArrayOf(R.drawable.chambre_exemple1, R.drawable.chambre_exemple2, R.drawable.chambre_exemple3)
        val adapteur = ImageSliderAdapter(requireContext(), images)
        viewPager.adapter = adapteur
        wormDotsIndicator.attachTo(viewPager)

        dateSelectionAffichage.setOnClickListener {
            presentateur.afficherSelectionneurDates()
        }

        boutonRéserver.setOnClickListener {
            presentateur.gererBoutonReservationCliquer()
        }

        boutonRetour.setOnClickListener {
            presentateur.gererBoutonRetourCliquer()
        }

        return vue
    }

    fun modifierDetailsChambre(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double) {
        TitreChambreTexte.text = typeChambre
        typeChambreTexte.text = typeChambre
        descriptionChambreTexte.text = description
        noteChambreTexte.text = "Note: $note (${nombreAvis} avis)"
        prixChambreTexte.text = "${prixParNuit}$ / nuit"
    }

    fun modifierDateAfficher(dateDebut: String, dateFin: String) {
        dateAfficher.text = "$dateDebut - $dateFin"
    }
}
