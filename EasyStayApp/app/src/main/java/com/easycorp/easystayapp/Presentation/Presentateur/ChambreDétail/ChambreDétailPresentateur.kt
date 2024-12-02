package com.easycorp.easystayapp.Presentation.Presentateur.ChambreDétail

import android.content.Context
import android.graphics.Color
import android.util.Log
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
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChambreDétailPresentateur(private val vue: ChambreDetailsVue, private val contexte: Context,
                                wormDotsIndicator: WormDotsIndicator
) :
    ChambreDétailPresentateurInterface {

    private val modèle = Modèle.getInstance(contexte)

    override var dateDebut: Calendar? = null
    override var dateFin: Calendar? = null

    override fun importerDetailsChambre() {
        CoroutineScope(Dispatchers.IO).launch {
            val chambreId = modèle.getChambreChoisieId() ?: return@launch
            val chambre = modèle.obtenirChambreParId(chambreId)

            if (chambre != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    afficherDetailsChambre(chambre.typeChambre, chambre.typeChambre, chambre.note.toFloat(), chambre.nombreAvis, chambre.prixParNuit)
                }
            }
        }
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
            Log.i("Erreur", "Les dates sont nulles")
        }
    }

    override fun gererBoutonRetourCliquer() {
        modèle.getCheminVersChambreDetails()?.let { vue.findNavController().navigate(it) }
    }

    override fun naviguerVersReservation(dateDebut: String, dateFin: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val chambreId = modèle.getChambreChoisieId()
            if (chambreId != null) {
                val dateDebutFormatted = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).format(SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH).parse(dateDebut)!!)
                val dateFinFormatted = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).format(SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH).parse(dateFin)!!)

                val nouvelleRéservation = modèle.obtenirChambreParId(chambreId)?.let {
                    ReservationData(
                        modèle.obtenirToutesLesReservations()!!.size + 1,
                        modèle.obtenirClientParId(1),
                        modèle.obtenirChambreParId(chambreId)!!.id.toString(),
                        dateDebutFormatted,
                        dateFinFormatted,
                        modèle.obtenirChambreParId(chambreId)!!.prixParNuit * (SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).parse(dateFinFormatted).time - SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).parse(dateDebutFormatted).time) / (1000 * 60 * 60 * 24),
                        "Confirmée",
                        "Carte de crédit",
                        true,
                        SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).format(Date()),
                        it
                    )
                }
                if (nouvelleRéservation != null) {
                    modèle.ajouterReservation(nouvelleRéservation, modèle.obtenirClientParId(1), modèle.obtenirChambreParId(chambreId)!!)
                    nouvelleRéservation.id?.let { modèle.setReservationChoisieId(it) }
                }
                CoroutineScope(Dispatchers.Main).launch {
                    modèle.setDates(dateDebutFormatted, dateFinFormatted)
                    vue.findNavController().navigate(R.id.action_chambreDetailsFragment_to_reserverFragment)
                }
            }
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
            Log.i("Erreur", "Les dates sont nulles")
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

}