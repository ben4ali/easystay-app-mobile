package com.easycorp.easystayapp.Presentation.Presentateur.ChambreDétail

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.easycorp.easystayapp.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.ChambreDetailsVue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.Domaine.Entite.ReservationData

class ChambreDétailPresentateur(private val vue: ChambreDetailsVue, private val contexte: Context) :
    ChambreDétailPresentateurInterface {

    private val modèle = Modèle.getInstance()

    override var dateDebut: Calendar? = null
    override var dateFin: Calendar? = null
    private var indicateursImages: List<View> = emptyList()

    override fun importerDetailsChambre() {
        val chambreId = modèle.getChambreChoisieId() ?: return
        val chambre = modèle.obtenirChambreParId(chambreId)

        afficherDetailsChambre(chambre.typeChambre, chambre.description, chambre.note, chambre.nombreAvis, chambre.prixParNuit)
    }

    override fun afficherDetailsChambre(typeChambre: String, description: String, note: Float, nombreAvis: Int, prixParNuit: Double) {
        vue.modifierDetailsChambre(typeChambre, description, note, nombreAvis, prixParNuit)
    }

    override fun gererBoutonReservationCliquer() {
        if (verifierDatesValide()) {
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH).apply {
                timeZone = TimeZone.getTimeZone("EST")
            }
            val dateDebutTexte = dateFormat.format(dateDebut?.time ?: Date())
            val dateFinTexte = dateFormat.format(dateFin?.time ?: Date())

            naviguerVersReservation(dateDebutTexte, dateFinTexte)
        } else {
            Toast.makeText(contexte, "Il faut d'abord choisir une date", Toast.LENGTH_LONG).show()
        }
    }

    override fun gererBoutonRetourCliquer() {
        modèle.getCheminVersFragmentRéserver()?.let { vue.findNavController().navigate(it) }
    }

    override fun naviguerVersReservation(dateDebut: String, dateFin: String) {
        val chambreId = modèle.getChambreChoisieId()
        if (chambreId != null) {
            val dateDebutFormatted = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).format(SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH).parse(dateDebut)!!)
            val dateFinFormatted = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).format(SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH).parse(dateFin)!!)
            val nouvelleRéservation = ReservationData(
                modèle.obtenirToutesLesReservations().size + 1,
                modèle.obtenirClientParId(1),
                modèle.obtenirChambreParId(chambreId),
                dateDebutFormatted,
                dateFinFormatted
            )
            modèle.ajouterReservation(nouvelleRéservation)
            nouvelleRéservation.id?.let { modèle.setReservationChoisieId(it) }
            modèle.setDates(dateDebutFormatted, dateFinFormatted)
            modèle.setCheminVersFragment(R.id.action_reserverFragment_to_chambreDetailsFragment)
            vue.findNavController().navigate(R.id.action_chambreDetailsFragment_to_reserverFragment)
        }
    }

    override fun modifierDateAfficher(dateDebut: String, dateFin: String) {
        vue.modifierDateAfficher(dateDebut, dateFin)
    }

    override fun nouvelleDateChoisie(dateDebut: Calendar?, dateFin: Calendar?) {
        if (dateDebut != null && dateFin != null) {
            val formatageDate = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)
            val dateDebutTexte = formatageDate.format(dateDebut.time)
            val dateFinTexte = formatageDate.format(dateFin.time)

            vue.modifierDateAfficher(dateDebutTexte, dateFinTexte)
        } else {
            Toast.makeText(contexte, "Il faut d'abord choisir une date", Toast.LENGTH_LONG).show()
        }
    }

    override fun afficherSelectionneurDates() {
        val dateAujourdhui = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.from(dateAujourdhui))

        val plageDateSelectionneur = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Sélectionnez une plage de dates")
            .setCalendarConstraints(constraintsBuilder.build())
            .setTheme(R.style.MaterialCalendarTheme)
            .build()

        plageDateSelectionneur.addOnPositiveButtonClickListener { selection ->
            val debutMillis = selection.first
            val finMillis = selection.second

            dateDebut = Calendar.getInstance().apply {
                timeInMillis = debutMillis
                add(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            dateFin = Calendar.getInstance().apply {
                timeInMillis = finMillis
                add(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val formatageDate = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)
            val dateDebutTexte = formatageDate.format(dateDebut?.time ?: Date())
            val dateFinTexte = formatageDate.format(dateFin?.time ?: Date())

            vue.modifierDateAfficher(dateDebutTexte, dateFinTexte)
        }

        (vue as Fragment).parentFragmentManager.let { fragmentManager ->
            plageDateSelectionneur.show(fragmentManager, "plage_date_selectionneur")
        }
    }

    override fun verifierDatesValide(): Boolean {
        return dateDebut != null && dateFin != null
    }

    override fun créerIndicateurImages(contexte: Context, dotIndicatorLayout: ViewGroup, compte: Int) {
        indicateursImages = List(compte) {
            val point = LayoutInflater.from(contexte).inflate(R.layout.dot_indicator, dotIndicatorLayout, false)
            point.setBackgroundResource(R.drawable.dot_background)
            dotIndicatorLayout.addView(point)
            point
        }
        modifierIndicateurImages(0)
    }

    override fun modifierIndicateurImages(positionSelectionner: Int) {
        for (i in indicateursImages.indices) {
            val point = indicateursImages[i]
            point.setBackgroundColor(if (i == positionSelectionner) Color.WHITE else Color.DKGRAY)
        }
    }

}