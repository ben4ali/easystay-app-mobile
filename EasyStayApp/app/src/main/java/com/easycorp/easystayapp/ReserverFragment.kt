package com.easycorp.easystayapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

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

    private var isNightMode = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_reserver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation des vues avec les ID français
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
        // Écouteur de clic pour basculer entre les thèmes
        boutonReserver.setOnClickListener {
            isNightMode = !isNightMode // Alterne l'état du mode
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}
