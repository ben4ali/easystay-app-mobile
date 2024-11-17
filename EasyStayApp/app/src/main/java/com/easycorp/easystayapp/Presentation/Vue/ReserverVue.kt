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
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var initialStartDate: Calendar? = null
    private var initialEndDate: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reserver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modèle = Modèle.getInstance()

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

        val startDate = arguments?.getString("startDate")
        val endDate = arguments?.getString("endDate")

        val reservationId = modèle.getReservationChoisieId()
        if (reservationId != null) {
            val reservation = modèle.obtenirReservationParId(reservationId)
            val chambre = reservation.chambre

            typeChambreTextView.text = chambre.typeChambre
            descriptionChambreTextView.text = chambre.description
            noteTextView.text = "★ ${chambre.note} (${chambre.nombreAvis} avis)"
            descriptionCompleteTextView.text = chambre.description
            commoditesTextView.text = "Wi-Fi gratuit, Petit déjeuner inclus"

            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)

            initialStartDate = Calendar.getInstance().apply {
                time = dateFormat.parse(reservation.dateDébut)
            }

            initialEndDate = Calendar.getInstance().apply {
                time = dateFormat.parse(reservation.dateFin)
            }

            if (initialStartDate != null && initialEndDate != null) {
                val startText = dateFormat.format(initialStartDate!!.time)
                val endText = dateFormat.format(initialEndDate!!.time)
                datesTextView.text = "$startText - $endText"
                calculateTotal(initialStartDate, initialEndDate, chambre.prixParNuit)
            }
        } else {
            val typeChambre = arguments?.getString("typeChambre")
            val description = arguments?.getString("description")
            val note = arguments?.getFloat("note")
            val nombreAvis = arguments?.getInt("nombreAvis")
            val prixParNuit = arguments?.getDouble("prixParNuit")
            val startDateString = arguments?.getString("startDate")
            val endDateString = arguments?.getString("endDate")

            if (typeChambre != null && description != null && note != null && nombreAvis != null && prixParNuit != null) {
                typeChambreTextView.text = typeChambre
                descriptionChambreTextView.text = description
                noteTextView.text = "★ $note ($nombreAvis avis)"
                descriptionCompleteTextView.text = description
                commoditesTextView.text = "Wi-Fi gratuit, Petit déjeuner inclus"
                prixParNuitTextView.text = "$prixParNuit$ / nuit"
            }

            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)

            initialStartDate = Calendar.getInstance().apply {
                time = dateFormat.parse(startDateString)
            }

            initialEndDate = Calendar.getInstance().apply {
                time = dateFormat.parse(endDateString)
            }

            if (initialStartDate != null && initialEndDate != null) {
                val startText = dateFormat.format(initialStartDate!!.time)
                val endText = dateFormat.format(initialEndDate!!.time)
                datesTextView.text = "$startText - $endText"
                calculateTotal(initialStartDate, initialEndDate, prixParNuit)
            }
        }

        datesTextView.setOnClickListener {
            showDateRangePicker()
        }
    }

    private fun calculateTotal(startDate: Calendar?, endDate: Calendar?, prixParNuit: Double?) {
        if (startDate != null && endDate != null && prixParNuit != null) {
            val diffInMillis = endDate.timeInMillis - startDate.timeInMillis
            val nuits = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()

            prixParNuitTextView.text = "$${String.format("%.2f", prixParNuit)} x $nuits nuits"

            val sousTotal = prixParNuit * nuits
            sousTotalTextView.text = "$${String.format("%.2f", sousTotal)}"

            val taxes = sousTotal * 0.15
            taxesTextView.text = "$${String.format("%.2f", taxes)}"

            val total = sousTotal * 1.15
            totalTextView.text = "$${String.format("%.2f", total)}"
        }
    }

    fun showDateRangePicker() {

    }
}