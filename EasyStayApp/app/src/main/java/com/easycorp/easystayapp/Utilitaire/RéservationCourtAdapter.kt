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

class RéservationCourtAdapter(

    context: Context,
    private val réservations: List<ReservationData>,

) : ArrayAdapter<ReservationData>(context, 0, réservations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_reservation_court, parent, false)
        val réservation = getItem(position) ?: return view

        val textType = view.findViewById<TextView>(R.id.RSIDTypeChambre)
        val textDates = view.findViewById<TextView>(R.id.RSDateDebut)
        val indicateur = view.findViewById<View>(R.id.RSIndicateur)
        when (réservation.obtenirNombreDeJours()) {
            in 0..10 -> indicateur.setBackgroundColor(Color.parseColor("#5AE44F"))
            in 11..19 -> indicateur.setBackgroundColor(Color.parseColor("#F7E24A"))
            in 20..30 -> indicateur.setBackgroundColor(Color.parseColor("#F7A24A"))
            else -> return view
        }

        textType.text = réservation.chambre.id.toString() + " - " + réservation.chambre.typeChambre
        textDates.text = "${réservation.dateDébut} - ${réservation.dateFin}"

        return view
    }
}