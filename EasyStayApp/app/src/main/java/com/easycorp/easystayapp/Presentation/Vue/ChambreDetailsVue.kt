package com.easycorp.easystayapp.Presentation.Vue

import com.easycorp.easystayapp.Utilitaire.ImageSliderAdapter
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.R.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layout.fragment_chambre_details, container, false)

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

        val images = intArrayOf(drawable.chambre_exemple1, drawable.chambre_exemple2, drawable.chambre_exemple3)
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
            showDateRangePicker()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typeChambre = arguments?.getString("typeChambre")
        val description = arguments?.getString("description")
        val note = arguments?.getFloat("note")
        val nombreAvis = arguments?.getInt("nombreAvis")
        val prixParNuit = arguments?.getDouble("prixParNuit")

        roomTitleTextView.text = typeChambre
        typeChambreTextView.text = typeChambre
        descriptionTextView.text = description
        noteTextView.text = "Note: $note (${nombreAvis ?: 0} avis)"
        prixTextView.text = "${prixParNuit ?: 0.0}$ / nuit"

        réserverBtn.setOnClickListener {
            if (startDate != null && endDate != null) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA_FRENCH)
                val startDateString = startDate?.let { dateFormat.format(it.time) }
                val endDateString = endDate?.let { dateFormat.format(it.time) }

                val bundle = Bundle().apply {
                    putString("typeChambre", typeChambre)
                    putString("description", description)
                    putFloat("note", note ?: 0.0f)
                    putInt("nombreAvis", nombreAvis ?: 0)
                    putDouble("prixParNuit", prixParNuit ?: 5.0)
                    putString("startDate", startDateString)
                    putString("endDate", endDateString)
                }
                try {
                    findNavController().navigate(R.id.action_chambreDetailsFragment_to_reserverFragment, bundle)
                } catch (e: IllegalArgumentException) {
                    Toast.makeText(requireContext(), "Erreur de naviguation: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), "Il faut d'abord choisir une date", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createDotIndicators(count: Int) {
        dotIndicators = List(count) {
            val dot = LayoutInflater.from(requireContext()).inflate(layout.dot_indicator, dotIndicatorLayout, false)
            dot.setBackgroundResource(drawable.dot_background)
            dotIndicatorLayout.addView(dot)
            dot
        }
        updateDotIndicators(0)
    }

    private fun updateDotIndicators(selectedPosition: Int) {
        for (i in dotIndicators.indices) {
            val dot = dotIndicators[i]
            dot.setBackgroundColor(if (i == selectedPosition) Color.WHITE else Color.DKGRAY)
        }
    }

    private fun showDateRangePicker() {
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
            val startText = startDate?.let { dateFormat.format(it.time) }
            val endText = endDate?.let { dateFormat.format(it.time) }

            dateShown.text = "$startText - $endText"
        }

        dateRangePicker.show(parentFragmentManager, "date_range_picker")
    }


}
