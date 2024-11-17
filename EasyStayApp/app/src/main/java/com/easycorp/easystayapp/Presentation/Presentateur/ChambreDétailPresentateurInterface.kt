package com.easycorp.easystayapp.Presentation.Presentateur

import android.os.Bundle
import java.util.Calendar

interface ChambreDétailPresentateurInterface {

    abstract val startDate: Any
    abstract val endDate: Any

    fun loadRoomDetails(arguments: Bundle?)
    fun onDateSelected(startDate: Calendar?, endDate: Calendar?)
    fun onBookButtonClicked(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double, startDate: String, endDate: String)
    fun showRoomDetails(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double)
    fun showDateError(message: String)
    fun navigateToBooking(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double, startDate: String, endDate: String)
    fun updateDateDisplay(startDate: String, endDate: String)
    fun showDateRangePicker()
    fun areDatesValid(): Boolean
}
