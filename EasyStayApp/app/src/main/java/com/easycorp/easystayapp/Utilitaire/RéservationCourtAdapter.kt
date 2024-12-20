package com.easycorp.easystayapp.Utilitaire

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.R
import java.text.SimpleDateFormat
import java.util.Locale


class RéservationCourtAdapter(
    private val context: Context,
    private val réservations: List<ReservationData>,
) : RecyclerView.Adapter<RéservationCourtAdapter.ReservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reservation_court, parent, false)
        view.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val réservation = réservations[position]
        holder.bind(réservation)
    }

    override fun getItemCount(): Int = réservations.size

    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textType: TextView = itemView.findViewById(R.id.RSIDTypeChambre)
        private val textDates: TextView = itemView.findViewById(R.id.RSDateDebut)
        private val indicateur: View = itemView.findViewById(R.id.RSIndicateur)

        fun bind(réservation: ReservationData) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.OVAL
            shape.setSize(20, 20)

            when (réservation.obtenirNombreDeJours()) {
                in 0..7 -> shape.setColor(Color.parseColor("#9cef95"))
                in 8..13 -> shape.setColor(Color.parseColor("#f9efa8"))
                in 14..19 -> shape.setColor(Color.parseColor("#fabb53"))
            }

            indicateur.background = shape

            val dateFormatageInitiale = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH)
            val dateFormatageAffichageFinal = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)

            val dateDebutFormattageInitiale = dateFormatageInitiale.parse(réservation.dateDébut)!!
            val dateFinFormattageInitiale = dateFormatageInitiale.parse(réservation.dateFin)!!

            val dateDebutFormatter = dateFormatageAffichageFinal.format(dateDebutFormattageInitiale)
            val dateFinFormatter = dateFormatageAffichageFinal.format(dateFinFormattageInitiale)

            textType.text = "${réservation.chambre.typeChambre}"
            textDates.text = "$dateDebutFormatter - $dateFinFormatter"
        }
    }
}
