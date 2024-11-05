package com.easycorp.easystayapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
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

        val donneesReservation = DonneesReservation(
            typeChambre = "Chambre Deluxe",
            description = "Cette chambre deluxe offre une vue imprenable sur la ville, un lit king-size confortable et un accès Wi-Fi rapide. Profitez de commodités modernes, d'un mini-bar, et d'un espace de travail. Idéale pour les séjours d'affaires ou de loisirs.",
            note = 4.9f,
            nombreAvis = 200,
            commodites = listOf("Wi-Fi gratuit", "Petit déjeuner inclus", "Accès à la salle de sport", "Service de nettoyage quotidien"),
            dates = "24-29 juillet",
            invites = "2 adultes, 1 enfant",
            prixParNuit = 150.0,
            nuits = 5,
            taxes = 112.5
        )

        typeChambreTextView.text = donneesReservation.typeChambre
        descriptionChambreTextView.text = "Vue sur la ville, lit king-size, Wi-Fi gratuit"
        noteTextView.text = "★ ${donneesReservation.note} (${donneesReservation.nombreAvis} avis)"
        descriptionCompleteTextView.text = donneesReservation.description
        commoditesTextView.text = donneesReservation.commodites.joinToString("\n") { "✓ $it" }
        datesTextView.text = donneesReservation.dates
        invitesTextView.text = donneesReservation.invites
        prixParNuitTextView.text = "$${donneesReservation.prixParNuit} x ${donneesReservation.nuits} nuits"
        sousTotalTextView.text = "$${donneesReservation.prixParNuit * donneesReservation.nuits}"
        taxesTextView.text = "$${donneesReservation.taxes}"
        totalTextView.text = "Total (CAD): $${donneesReservation.prixTotal()}"

        datesTextView.setOnClickListener {
            showStartDatePicker()
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

            val startText = "${startDate?.get(Calendar.DAY_OF_MONTH)}-${startDate?.get(Calendar.MONTH)?.plus(1)}-${startDate?.get(Calendar.YEAR)}"
            val endText = "${endDate?.get(Calendar.DAY_OF_MONTH)}-${endDate?.get(Calendar.MONTH)?.plus(1)}-${endDate?.get(Calendar.YEAR)}"
            datesTextView.text = "Du $startText au $endText"
        }, year, month, day)

        datePickerDialog.datePicker.minDate = startDate?.timeInMillis ?: calendar.timeInMillis
        datePickerDialog.show()
    }
}

