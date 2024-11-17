package com.easycorp.easystayapp.Presentation.Vue

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.easycorp.easystayapp.Presentation.Presentateur.ChambreDétailPresentateur
import com.easycorp.easystayapp.Presentation.Presentateur.ChambreDétailPresentateurInterface
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Utilitaire.ImageSliderAdapter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ChambreDetailsVue : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var dotIndicatorLayout: LinearLayout
    private lateinit var dotIndicators: List<View>
    private lateinit var datePickerBtn: LinearLayout
    private lateinit var dateShown: TextView
    private lateinit var typeChambreTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var noteTextView: TextView
    private lateinit var prixTextView: TextView
    private lateinit var roomTitleTextView: TextView
    private lateinit var backBtn: ImageView
    private lateinit var réserverBtn: Button

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private lateinit var presenter: ChambreDétailPresentateurInterface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chambre_details, container, false)

        viewPager = view.findViewById(R.id.viewPagerImages)
        dotIndicatorLayout = view.findViewById(R.id.dotIndicatorLayout)
        datePickerBtn = view.findViewById(R.id.datePickerLayout)
        dateShown = view.findViewById(R.id.dateTextView)
        typeChambreTextView = view.findViewById(R.id.bedTypeTextView)
        roomTitleTextView = view.findViewById(R.id.roomTitleTextView)
        descriptionTextView = view.findViewById(R.id.roomDescriptionTitle)
        noteTextView = view.findViewById(R.id.reviewCountTextView)
        prixTextView = view.findViewById(R.id.priceTextView)
        backBtn = view.findViewById(R.id.backButton)
        réserverBtn = view.findViewById(R.id.bookButton)

        presenter = ChambreDétailPresentateur(this)

        presenter.loadRoomDetails()

        val images = intArrayOf(R.drawable.chambre_exemple1, R.drawable.chambre_exemple2, R.drawable.chambre_exemple3)
        val adapter = ImageSliderAdapter(requireContext(), images)
        viewPager.adapter = adapter
        createDotIndicators(images.size)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDotIndicators(position)
            }
        })

        datePickerBtn.setOnClickListener {
            presenter.showDateRangePicker()
        }

        backBtn.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_chambreDetailsFragment_to_fragment_chambres)
            } catch (e: IllegalArgumentException) {
                Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    fun updateRoomDetails(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double) {
        roomTitleTextView.text = typeChambre
        typeChambreTextView.text = typeChambre
        descriptionTextView.text = description
        noteTextView.text = "Note: $note (${nombreAvis} avis)"
        prixTextView.text = "${prixParNuit}$ / nuit"
    }

    fun setReservationButtonListener(listener: () -> Unit) {
        réserverBtn.setOnClickListener { listener() }
    }

    fun showToastMessage(message: String) {
        Toast.makeText(requireContext(), "Il faut d'abord choisir une date", Toast.LENGTH_LONG).show()
    }


    fun updateDateDisplay(startDate: String, endDate: String) {
        dateShown.text = "$startDate - $endDate"
    }

    fun showDateError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun navigateToBooking(actionId: Int, startDate: String, endDate: String) {
        val bundle = Bundle().apply {
            putString("startDate", startDate)
            putString("endDate", endDate)
        }
        findNavController().navigate(actionId, bundle)
    }

    private fun createDotIndicators(count: Int) {
        presenter.createDotIndicators(requireContext(), dotIndicatorLayout, count)
    }

    private fun updateDotIndicators(selectedPosition: Int) {
        presenter.updateDotIndicators(selectedPosition)
    }
}
