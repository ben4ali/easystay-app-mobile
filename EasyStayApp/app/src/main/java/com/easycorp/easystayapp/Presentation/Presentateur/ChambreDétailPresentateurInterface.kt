package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import java.util.Calendar

interface ChambreDétailPresentateurInterface {

    abstract val startDate: Calendar?
    abstract val endDate: Calendar?

    fun loadRoomDetails()
    fun onDateSelected(startDate: Calendar?, endDate: Calendar?)
    fun onBookButtonClicked(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double, startDate: String, endDate: String)
    fun showRoomDetails(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double)
    fun showDateError(message: String)
    fun navigateToBooking(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double, startDate: String, endDate: String)
    fun updateDateDisplay(startDate: String, endDate: String)
    fun showDateRangePicker()
    fun areDatesValid(): Boolean
    fun createDotIndicators(context: Context, dotIndicatorLayout: ViewGroup, count: Int)
    fun updateDotIndicators(selectedPosition: Int)
    fun updateRoomDetails(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double)
    fun setReservationButtonListener(listener: () -> Unit)
    fun showToastMessage(message: String)
}
