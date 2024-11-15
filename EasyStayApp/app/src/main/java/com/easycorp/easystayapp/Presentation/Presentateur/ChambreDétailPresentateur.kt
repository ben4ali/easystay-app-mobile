package com.easycorp.easystayapp.Presentation.Presentateur

import android.os.Bundle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChambreDétailPresentateur(private val view: ChambreDétailPresentateurInterface) : ChambreDétailPresentateurInterface {

    override fun loadRoomDetails(arguments: Bundle?) {
        val typeChambre = arguments?.getString("typeChambre")
        val description = arguments?.getString("description")
        val note = arguments?.getFloat("note") ?: 0.0f
        val nombreAvis = arguments?.getInt("nombreAvis") ?: 0
        val prixParNuit = arguments?.getDouble("prixParNuit") ?: 0.0

        if (typeChambre != null && description != null) {
            showRoomDetails(typeChambre, description, note, nombreAvis, prixParNuit)
        }
    }

    override fun showRoomDetails(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double) {
        view.showRoomDetails(typeChambre, description, note, nombreAvis, prixParNuit)
    }

    override fun showDateError(message: String) {
        view.showDateError(message)
    }

    override fun navigateToBooking(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double, startDate: String, endDate: String) {
        view.navigateToBooking(typeChambre, description, note, nombreAvis, prixParNuit, startDate, endDate)
    }

    override fun updateDateDisplay(startDate: String, endDate: String) {
        view.updateDateDisplay(startDate, endDate)
    }

    override fun onDateSelected(startDate: Calendar?, endDate: Calendar?) {
        if (startDate != null && endDate != null) {
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)
            val startDateString = dateFormat.format(startDate.time)
            val endDateString = dateFormat.format(endDate.time)

            view.updateDateDisplay(startDateString, endDateString)
        } else {
            showDateError("Il faut d'abord choisir une date")
        }
    }


    override fun onBookButtonClicked(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double, startDate: String, endDate: String) {
        navigateToBooking(typeChambre, description, note, nombreAvis, prixParNuit, startDate, endDate)
    }

}
