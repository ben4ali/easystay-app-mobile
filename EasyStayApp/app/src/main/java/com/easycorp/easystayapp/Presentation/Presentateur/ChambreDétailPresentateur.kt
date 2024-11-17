package com.easycorp.easystayapp.Presentation.Presentateur

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.easycorp.easystayapp.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ChambreDétailPresentateur(private val view: ChambreDétailPresentateurInterface) : ChambreDétailPresentateurInterface {

    var startDate: Calendar? = null
    var endDate: Calendar? = null

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

    override fun showDateRangePicker() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.from(today))

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Sélectionnez une plage de dates")
            .setCalendarConstraints(constraintsBuilder.build())
            .setTheme(R.style.MaterialCalendarTheme)
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startMillis = selection.first
            val endMillis = selection.second

            startDate = Calendar.getInstance().apply {
                timeInMillis = startMillis
                add(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            endDate = Calendar.getInstance().apply {
                timeInMillis = endMillis
                add(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)
            val startDateString = dateFormat.format(startDate?.time ?: Date())
            val endDateString = dateFormat.format(endDate?.time ?: Date())

            view.updateDateDisplay(startDateString, endDateString)
        }

        (view as Fragment).parentFragmentManager?.let { fragmentManager ->
            dateRangePicker.show(fragmentManager, "date_range_picker")
        }
    }

    override fun areDatesValid(): Boolean {
        return startDate != null && endDate != null
    }

}
