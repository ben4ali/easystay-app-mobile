package com.easycorp.easystayapp

import ImageSliderAdapter
import android.app.DatePickerDialog
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
import com.easycorp.easystayapp.R.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentChambreDetails : Fragment() {

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
            showStartDatePicker()
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
        val prixTotal = arguments?.getDouble("prixTotal")

        roomTitleTextView.text = typeChambre
        typeChambreTextView.text = typeChambre
        descriptionTextView.text = description
        noteTextView.text = "Note: $note (${nombreAvis ?: 0} avis)"
        prixTextView.text = "${prixTotal ?: 0.0}$ / nuit"
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

    private fun showStartDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            startDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            showEndDatePicker()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showEndDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            endDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }

            val startText = "${startDate?.get(Calendar.DAY_OF_MONTH)}"
            val endText = "${endDate?.get(Calendar.DAY_OF_MONTH)}"
            val dateFormat = SimpleDateFormat("MMMM", Locale.FRENCH)
            val startMonthText = dateFormat.format(startDate?.time)
            val endMonthText = dateFormat.format(endDate?.time)
            dateShown.text = if (startMonthText != endMonthText) {
                "$startText $startMonthText - $endText $endMonthText"
            } else {
                "$startText - $endText $endMonthText"
            }
        }, year, month, day)

        datePickerDialog.datePicker.minDate = startDate?.timeInMillis ?: calendar.timeInMillis
        datePickerDialog.show()
    }
}
