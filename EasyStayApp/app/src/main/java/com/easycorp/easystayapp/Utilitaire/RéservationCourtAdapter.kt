package com.easycorp.easystayapp.Utilitaire

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.R
import java.text.SimpleDateFormat
import java.util.Locale

class RéservationCourtAdapter(
    context: Context,
    private val réservations: List<ReservationData>,
) : ArrayAdapter<ReservationData>(context, 0, réservations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val réservation = getItem(position) ?: return convertView ?: View(context)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_reservation_court, parent, false)
        val textType = view.findViewById<TextView>(R.id.RSIDTypeChambre)
        val textDates = view.findViewById<TextView>(R.id.RSDateDebut)
        val indicateur = view.findViewById<View>(R.id.RSIndicateur)

        when (réservation.obtenirNombreDeJours()) {
            in 0..7 -> indicateur.setBackgroundColor(Color.parseColor("#9cef95"))
            in 8..13 -> indicateur.setBackgroundColor(Color.parseColor("#f9efa8"))
            in 14..19 -> indicateur.setBackgroundColor(Color.parseColor("#fabb53"))
        }

        val dateFormatageInitiale = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH)
        val dateFormatageAffichageFinal = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)

        val dateDebutFormattageInitiale = dateFormatageInitiale.parse(réservation.dateDébut)!!
        val dateFinFormattageInitiale = dateFormatageInitiale.parse(réservation.dateFin)!!

        val dateDebutFormatter = dateFormatageAffichageFinal.format(dateDebutFormattageInitiale)
        val dateFinFormatter = dateFormatageAffichageFinal.format(dateFinFormattageInitiale)

        textType.text = "${réservation.chambre.id} - ${réservation.chambre.typeChambre}"
        textDates.text = "${dateDebutFormatter} - ${dateFinFormatter}"

        return view
    }
}