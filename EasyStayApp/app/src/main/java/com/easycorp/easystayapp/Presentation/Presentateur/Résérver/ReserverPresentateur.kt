package com.easycorp.easystayapp.Presentation.Presentateur.Résérver

import android.util.Log
import androidx.fragment.app.Fragment
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.ReserverVue
import com.easycorp.easystayapp.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReserverPresentateur(private val vue: ReserverVue) : ReserverPresentateurInterface {

    private val modèle = Modèle.getInstance()

    override var dateDebut: Calendar? = null
    override var dateFin: Calendar? = null
    private val dateFormatage = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)
    override var dateDebutInitiale: Calendar? = null
    override var dateFinInitiale: Calendar? = null
    val chambreId = modèle.getReservationChoisieId()
    val chambre = chambreId?.let { modèle.obtenirChambreParId(it) }

    override fun ouvrirDetailsChambre() {
        val dateDebutTexte = (vue as Fragment).arguments?.getString("dateDebut") ?: ""
        val dateFinTexte = (vue as Fragment).arguments?.getString("dateFin") ?: ""

        dateDebutInitiale = Calendar.getInstance().apply {
            time = dateFormatage.parse(dateDebutTexte)!!
        }

        dateFinInitiale = Calendar.getInstance().apply {
            time = dateFormatage.parse(dateFinTexte)!!
        }

        if (chambre != null) {
            vue.modifierDetailsChambre(chambre, dateDebutTexte, dateFinTexte)
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

            val dateFormatage = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)
            val dateDebutTexte = dateFormatage.format(dateDebut?.time ?: Date())
            val dateFinTexte = dateFormatage.format(dateFin?.time ?: Date())

            dateDebutInitiale = Calendar.getInstance().apply {
                time = dateFormatage.parse(dateDebutTexte)!!
            }

            dateFinInitiale = Calendar.getInstance().apply {
                time = dateFormatage.parse(dateFinTexte)!!
            }

            if (chambre != null) {
                vue.modifierDetailsChambre(chambre, dateDebutTexte, dateFinTexte)
            }
        }

        (vue as Fragment).parentFragmentManager.let { fragmentManager ->
            plageDateSelectionneur.show(fragmentManager, "plage_date_selectionneur")
        }
    }

    override fun dateFormatage(dateTexte: String): String {
        val date = Calendar.getInstance().apply {
            time = dateFormatage.parse(dateTexte)!!
        }
        return dateFormatage.format(date.time)
    }

    override fun calculerPrixTotale(dateDebut: Calendar?, dateFin: Calendar?, prixParNuit: Double?) {
        if (dateDebut != null && dateFin != null && prixParNuit != null) {
            val diffInMillis = dateFin.timeInMillis - dateDebut.timeInMillis
            val nuits = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()

            val sousTotal = prixParNuit * nuits
            val taxes = sousTotal * 0.15
            val total = sousTotal + taxes

            vue.modifierPrixTotale(prixParNuit, nuits, sousTotal, taxes, total)
        }
    }

}