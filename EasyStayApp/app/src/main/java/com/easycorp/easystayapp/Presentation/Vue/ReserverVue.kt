package com.easycorp.easystayapp.Presentation.Vue

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.easycorp.easystayapp.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import androidx.core.util.Pair

class ReserverVue : Fragment() {

    private lateinit var typeChambreTextView: TextView
    private lateinit var imageChambreImageView: ImageView
    private lateinit var descriptionChambreTextView: TextView
    private lateinit var noteTextView: TextView
    private lateinit var descriptionCompleteTextView: TextView
    private lateinit var commoditesTextView: TextView
    private lateinit var datesTextView: TextView
    private lateinit var invitesTextView: TextView
    private lateinit var prixParNuitTextView: TextView
    private lateinit var sousTotalTextView: TextView
    private lateinit var taxesTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var boutonReserver: Button

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var initialStartDate: Calendar? = null
    private var initialEndDate: Calendar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reserver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        typeChambreTextView = view.findViewById(R.id.roomTypeTextView)
        imageChambreImageView = view.findViewById(R.id.roomImageView)
        descriptionChambreTextView = view.findViewById(R.id.roomDescriptionTextView)
        noteTextView = view.findViewById(R.id.ratingTextView)
        descriptionCompleteTextView = view.findViewById(R.id.roomFullDescriptionTextView)
        commoditesTextView = view.findViewById(R.id.amenitiesTextView)
        datesTextView = view.findViewById(R.id.datesTextView)
        invitesTextView = view.findViewById(R.id.guestsTextView)
        prixParNuitTextView = view.findViewById(R.id.pricePerNightTextView)
        sousTotalTextView = view.findViewById(R.id.subtotalTextView)
        taxesTextView = view.findViewById(R.id.taxesAmountTextView)
        totalTextView = view.findViewById(R.id.totalAmountTextView)
        boutonReserver = view.findViewById(R.id.reserveButton)

        val typeChambre = arguments?.getString("typeChambre") ?: "Type de chambre non spécifié"
        val description = arguments?.getString("description") ?: "Description non spécifiée"
        val note = arguments?.getFloat("note", 0.0f)
        val nombreAvis = arguments?.getInt("nombreAvis", 0)
        val prixParNuit = arguments?.getDouble("prixParNuit", 0.0)
        val startDateString = arguments?.getString("startDate")
        val endDateString = arguments?.getString("endDate")

        typeChambreTextView.text = typeChambre
        descriptionChambreTextView.text = description
        noteTextView.text = "★ $note ($nombreAvis avis)"
        descriptionCompleteTextView.text = description
        commoditesTextView.text = "Wi-Fi gratuit, Petit déjeuner inclus"

        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)

        initialStartDate = startDateString?.let {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA_FRENCH).parse(it)
                val calendar = Calendar.getInstance().apply { time = date }
                calendar
            } catch (e: Exception) {
                null
            }
        }

        initialEndDate = endDateString?.let {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA_FRENCH).parse(it)
                val calendar = Calendar.getInstance().apply { time = date }
                calendar
            } catch (e: Exception) {
                null
            }
        }

        if (initialStartDate != null && initialEndDate != null) {
            val startText = dateFormat.format(initialStartDate!!.time)
            val endText = dateFormat.format(initialEndDate!!.time)
            datesTextView.text = "$startText - $endText"
            calculateTotal(initialStartDate, initialEndDate, prixParNuit)
        }


        datesTextView.setOnClickListener {
            showDateRangePicker()
        }
    }

    private fun calculateTotal(startDate: Calendar?, endDate: Calendar?, prixParNuit: Double?) {
        if (startDate != null && endDate != null && prixParNuit != null) {
            val diffInMillis = endDate.timeInMillis - startDate.timeInMillis
            val nuits = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()

            prixParNuitTextView.text = "$${String.format("%.2f", prixParNuit)} x $nuits nuits"

            val sousTotal = prixParNuit * nuits
            sousTotalTextView.text = "$${String.format("%.2f", sousTotal)}"

            val taxes = sousTotal * 0.15
            taxesTextView.text = "$${String.format("%.2f", taxes)}"

            val total = sousTotal * 1.15
            totalTextView.text = "$${String.format("%.2f", total)}"
        }
    }



    private fun showDateRangePicker() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.from(today))

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Sélectionnez une plage de dates")
            .setCalendarConstraints(constraintsBuilder.build())
            .apply {
                initialStartDate?.let {
                    setSelection(
                        Pair(
                            it.timeInMillis,
                            initialEndDate?.timeInMillis ?: it.timeInMillis
                        )
                    )
                }
            }
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

            datesTextView.text = "$startText - $endText"
            val prixParNuit = arguments?.getDouble("prixParNuit", 0.0)
            calculateTotal(startDate, endDate, prixParNuit)
        }

        dateRangePicker.show(parentFragmentManager, "date_range_picker")
    }
}
