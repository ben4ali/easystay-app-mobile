package com.easycorp.easystayapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

class ReserverFragment : Fragment() {

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
    private var initialStartDate: LocalDate? = null
    private var initialEndDate: LocalDate? = null

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

        val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        initialStartDate = startDateString?.let {
            try { LocalDate.parse(it, formatter) } catch (e: Exception) { null }
        }

        initialEndDate = endDateString?.let {
            try { LocalDate.parse(it, formatter) } catch (e: Exception) { null }
        }

        if (initialStartDate != null && initialEndDate != null) {
            datesTextView.text = "$startDateString - $endDateString"
            calculateTotal(initialStartDate, initialEndDate, prixParNuit)
        }

        datesTextView.setOnClickListener {
            showStartDatePicker()
        }
    }

    private fun calculateTotal(startDate: LocalDate?, endDate: LocalDate?, prixParNuit: Double?) {
        if (startDate != null && endDate != null && prixParNuit != null) {
            val nuits = ChronoUnit.DAYS.between(startDate, endDate).toInt()
            prixParNuitTextView.text = "$$prixParNuit x $nuits nuits"
            val sousTotal = prixParNuit * nuits
            sousTotalTextView.text = "$$sousTotal"
            val taxes = sousTotal * 0.15
            taxesTextView.text = "$$taxes"
            val total = sousTotal * 1.15
            totalTextView.text = "$$total"
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

            val startLocalDate = LocalDate.of(
                startDate!!.get(Calendar.YEAR),
                startDate!!.get(Calendar.MONTH) + 1,
                startDate!!.get(Calendar.DAY_OF_MONTH)
            )

            val endLocalDate = LocalDate.of(
                endDate!!.get(Calendar.YEAR),
                endDate!!.get(Calendar.MONTH) + 1,
                endDate!!.get(Calendar.DAY_OF_MONTH)
            )

            datesTextView.text = "Du ${startLocalDate.dayOfMonth} ${startLocalDate.month} - ${endLocalDate.dayOfMonth} ${endLocalDate.month}"
            val prixParNuit = arguments?.getDouble("prixParNuit", 0.0)
            calculateTotal(startLocalDate, endLocalDate, prixParNuit)
        }, year, month, day)

        datePickerDialog.datePicker.minDate = startDate?.timeInMillis ?: calendar.timeInMillis
        datePickerDialog.show()
    }
}
