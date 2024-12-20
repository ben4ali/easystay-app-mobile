package com.easycorp.easystayapp.Utilitaire

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Presentateur.ListeRéservation.ListeRéservationPrésentateur
import com.easycorp.easystayapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RéservationAdapter(
    context: Context,
    private val réservations: List<ReservationData>,
    private val présentateur: ListeRéservationPrésentateur
) : ArrayAdapter<ReservationData>(context, 0, réservations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false)
        val réservation = getItem(position) ?: return view

        val imageChambre = view.findViewById<ImageView>(R.id.RChambreImage)
        val textType = view.findViewById<TextView>(R.id.RTypeChambre)
        val textDesc = view.findViewById<TextView>(R.id.RDescription)
        val textAvis = view.findViewById<TextView>(R.id.RNote)
        val textPrix = view.findViewById<TextView>(R.id.RPrix)
        val textDates = view.findViewById<TextView>(R.id.RDates)
        val btnVoir = view.findViewById<CardView>(R.id.btnVoirRes)
        val btnSupprimer = view.findViewById<TextView>(R.id.btnSupprimer)

        textType.text = réservation.chambre.typeChambre
        textDesc.text = réservation.chambre.typeChambre
        textAvis.text = "${réservation.chambre.note} (${réservation.chambre.nombreAvis} avis)"
        textPrix.text = "${réservation.chambre.prixParNuit}$/nuit"
        textDates.text = "${réservation.dateDébut} - ${réservation.dateFin}"
        imageChambre.setImageResource(R.drawable.chambre_exemple1)

        btnVoir.setOnClickListener {
            présentateur.ouvrirDetailsReservation(réservation, view)
        }
        btnSupprimer.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                présentateur.supprimerReservation(réservation)
            }
        }


        return view
    }
}