package com.easycorp.easystayapp

import ImageSliderAdapter
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layout.fragment_chambre_details, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        dotIndicatorLayout = view.findViewById(R.id.dotIndicatorLayout)
        datePickerBtn = view.findViewById(R.id.datePicker)
        dateShown = view.findViewById(R.id.dateShown)

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

        datePickerBtn.setOnClickListener{
            showStartDatePicker()
        }

        return view
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
            // Enregistre la date de début
            startDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            // Affiche le sélecteur de date de fin
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
            // Enregistre la date de fin
            endDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }

            // Met à jour le champ de texte avec la plage de dates sélectionnée
            val startText = "${startDate?.get(Calendar.DAY_OF_MONTH)}"
            val endText = "${endDate?.get(Calendar.DAY_OF_MONTH)}"
            val monthTextNumber = "${endDate?.get(Calendar.MONTH)}"
            val dateFormat = SimpleDateFormat("MMMM", Locale.FRENCH)
            val monthTextLetter = dateFormat.format(endDate?.time)
            if(startDate?.get(Calendar.MONTH) != endDate?.get(Calendar.MONTH)){
                val firstMonthTextLetter = dateFormat.format(startDate?.time)
                dateShown.text = "$startText $firstMonthTextLetter - $endText $monthTextLetter"
            }else {
                dateShown.text = "$startText - $endText $monthTextLetter"
            }
        }, year, month, day)

        // Assurez-vous que la date de fin ne peut pas être antérieure à la date de début
        datePickerDialog.datePicker.minDate = startDate?.timeInMillis ?: calendar.timeInMillis
        datePickerDialog.show()
    }
}