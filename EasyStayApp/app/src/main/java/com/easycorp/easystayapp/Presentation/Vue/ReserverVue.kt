package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Presentation.Presentateur.Résérver.ReserverPresentateur
import com.easycorp.easystayapp.Presentation.Presentateur.Résérver.ReserverPresentateurInterface
import com.easycorp.easystayapp.R

class ReserverVue : Fragment() {

    private lateinit var typeChambreTextView: TextView
    private lateinit var imageChambreImageView: ImageView
    private lateinit var descriptionChambreTextView: TextView
    private lateinit var noteTextView: TextView
    private lateinit var descriptionCompleteTextView: TextView
    private lateinit var commoditesTextView: TextView
    private lateinit var datesTextView: TextView
    private lateinit var invitesTextView: TextView
    private lateinit var prixParNuitTextView: TextView
    private lateinit var sousTotalTextView: TextView
    private lateinit var taxesTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var boutonReserver: Button

    private lateinit var presentateur: ReserverPresentateurInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reserver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presentateur = ReserverPresentateur(this)

        typeChambreTextView = view.findViewById(R.id.roomTypeTextView)
        imageChambreImageView = view.findViewById(R.id.roomImageView)
        descriptionChambreTextView = view.findViewById(R.id.roomDescriptionTextView)
        noteTextView = view.findViewById(R.id.ratingTextView)
        descriptionCompleteTextView = view.findViewById(R.id.roomFullDescriptionTextView)
        commoditesTextView = view.findViewById(R.id.amenitiesTextView)
        datesTextView = view.findViewById(R.id.RDates)
        invitesTextView = view.findViewById(R.id.guestsTextView)
        prixParNuitTextView = view.findViewById(R.id.pricePerNightTextView)
        sousTotalTextView = view.findViewById(R.id.subtotalTextView)
        taxesTextView = view.findViewById(R.id.taxesAmountTextView)
        totalTextView = view.findViewById(R.id.totalAmountTextView)
        boutonReserver = view.findViewById(R.id.reserveButton)

        presentateur.ouvrirDetailsChambre()

        datesTextView.setOnClickListener {
            presentateur.afficherSelectionneurDates()
        }
    }

    fun modifierDetailsChambre(chambre: ChambreData, startDate: String, endDate: String) {
        typeChambreTextView.text = chambre.typeChambre
        descriptionChambreTextView.text = chambre.description
        noteTextView.text = "★ ${chambre.note} (${chambre.nombreAvis} avis)"
        descriptionCompleteTextView.text = chambre.description
        commoditesTextView.text = "Wi-Fi gratuit, Petit déjeuner inclus"
        prixParNuitTextView.text = "${chambre.prixParNuit}$ / nuit"

        datesTextView.text = "${presentateur.dateFormatage(startDate)} - ${presentateur.dateFormatage(endDate)}"

        presentateur.calculerPrixTotale(presentateur.dateDebutInitiale, presentateur.dateFinInitiale, chambre.prixParNuit)
    }

    fun modifierPrixTotale(prixParNuit: Double, nuits: Int, sousTotal: Double, taxes: Double, total: Double) {
        prixParNuitTextView.text = "$${String.format("%.2f", prixParNuit)} x $nuits nuits"
        sousTotalTextView.text = "$${String.format("%.2f", sousTotal)}"
        taxesTextView.text = "$${String.format("%.2f", taxes)}"
        totalTextView.text = "$${String.format("%.2f", total)}"
    }
}