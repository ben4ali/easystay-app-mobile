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

import com.easycorp.easystayapp.R

class ReserverVue : Fragment() {

    lateinit var typeChambreTextView: TextView
    lateinit var imageChambreImageView: ImageView
    lateinit var descriptionChambreTextView: TextView
    lateinit var noteTextView: TextView
    lateinit var descriptionCompleteTextView: TextView
    lateinit var commoditesTextView: TextView
    lateinit var datesTextView: TextView
    lateinit var invitesTextView: TextView
    lateinit var prixParNuitTextView: TextView
    lateinit var sousTotalTextView: TextView
    lateinit var taxesTextView: TextView
    lateinit var totalTextView: TextView
    lateinit var boutonRetourRéserver: ImageView

    lateinit var presentateur : ReserverPresentateur

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reserver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presentateur = ReserverPresentateur(this, requireContext())

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
        boutonRetourRéserver = view.findViewById(R.id.backButton2)

        presentateur.ouvrirDetailsRéservation()

        boutonRetourRéserver.setOnClickListener {
            presentateur.gererBoutonRetourCliquer()
        }
    }

    fun modifierDetailsChambre(chambre: ChambreData) {
        typeChambreTextView.text = chambre.typeChambre
        descriptionChambreTextView.text = chambre.typeChambre
        noteTextView.text = "★ ${chambre.note} (${chambre.nombreAvis} avis)"
        descriptionCompleteTextView.text = chambre.typeChambre
        commoditesTextView.text = "Wi-Fi gratuit, Petit déjeuner inclus"
        prixParNuitTextView.text = "${chambre.prixParNuit}$ / nuit"
        datesTextView.text = "${presentateur.dateDebutFormatter} - ${presentateur.dateFinFormatter}"
        presentateur.calculerPrixTotale()
    }

    fun modifierPrixTotale(prixParNuit: Double, nuits: Int, sousTotal: Double, taxes: Double, total: Double) {
        prixParNuitTextView.text = "$${String.format("%.2f", prixParNuit)} x $nuits nuits"
        sousTotalTextView.text = "$${String.format("%.2f", sousTotal)}"
        taxesTextView.text = "$${String.format("%.2f", taxes)}"
        totalTextView.text = "$${String.format("%.2f", total)}"
    }
}