package com.easycorp.easystayapp.Presentation.Presentateur.Résérver

import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.ReserverVue
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Utilitaire.EmailService
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReserverPresentateur(private val vue: ReserverVue) : ReserverPresentateurInterface {

    val modèle = Modèle.getInstance()

    var dateDebut: Calendar? = null
    var dateFin: Calendar? = null
    var chambreId: Int? = null
    var dateDebutInitiale: Calendar? = null
    var dateFinInitiale: Calendar? = null
    lateinit var dateDebutFormatter: String
    lateinit var dateFinFormatter: String

    lateinit var chambre : ChambreData
    lateinit var réservation: ReservationData

    override fun ouvrirDetailsRéservation() {
        réservation = modèle.getReservationChoisieId()
            ?.let { modèle.obtenirReservationParId(it) }!!
        chambre = modèle.obtenirChambreParId(réservation.chambre.id)

        val dateFormatageInitiale = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH)
        val dateFormatageAffichageFinal = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)

        val dateDebutFormattageInitiale = dateFormatageInitiale.parse(réservation.dateDébut)!!
        val dateFinFormattageInitiale = dateFormatageInitiale.parse(réservation.dateFin)!!

        dateDebutFormatter = dateFormatageAffichageFinal.format(dateDebutFormattageInitiale)
        dateFinFormatter = dateFormatageAffichageFinal.format(dateFinFormattageInitiale)

        vue.modifierDetailsChambre(chambre)
    }

    override fun afficherSelectionneurDates() {
        val dateAujourdhui = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.from(dateAujourdhui))

        val formattageDateInitiale = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH)
        val formatageDateAffichage = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)

        val dateDebutCalendar = Calendar.getInstance().apply {
            time = formattageDateInitiale.parse(réservation.dateDébut)!!
        }

        val dateFinCalendar = Calendar.getInstance().apply {
            time = formattageDateInitiale.parse(réservation.dateFin)!!
        }

        val plageDateSelectionneur = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Sélectionnez une plage de dates")
            .setCalendarConstraints(constraintsBuilder.build())
            .setSelection(Pair(
                dateDebutCalendar.timeInMillis,
                dateFinCalendar.timeInMillis
            ))
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

            val dateFormatage = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH)
            val dateDebutTexte = dateFormatage.format(dateDebut?.time ?: Date())
            val dateFinTexte = dateFormatage.format(dateFin?.time ?: Date())

            dateDebutInitiale = Calendar.getInstance().apply {
                time = dateFormatage.parse(dateDebutTexte)!!
            }

            dateFinInitiale = Calendar.getInstance().apply {
                time = dateFormatage.parse(dateFinTexte)!!
            }

            dateDebutFormatter = formatageDateAffichage.format(dateDebut?.time ?: Date())
            dateFinFormatter = formatageDateAffichage.format(dateFin?.time ?: Date())
            réservation.dateDébut = dateDebutTexte
            réservation.dateFin = dateFinTexte

            vue.modifierDetailsChambre(chambre)
        }

        (vue as Fragment).parentFragmentManager.let { fragmentManager ->
            plageDateSelectionneur.show(fragmentManager, "plage_date_selectionneur")
        }
    }

    override fun calculerPrixTotale() {
        val nuits = réservation.calculerNombreDeNuits()
        val sousTotal = réservation.chambre.prixParNuit * nuits
        val taxes = sousTotal * 0.15
        val total = sousTotal + taxes

        vue.modifierPrixTotale(réservation.chambre.prixParNuit, nuits, sousTotal, taxes, total)
    }

    override fun gererConfirmationReservation() {

        val client = modèle.obtenirClientParId(1)
        val dateDebutReservation = réservation.dateDébut
        val dateFinReservation = réservation.dateFin

        val nouvelleReservation = ReservationData(
            id = 7,
            client = client,
            chambre = chambre!!,
            dateDébut = dateDebutReservation,
            dateFin = dateFinReservation
        )

        modèle.ajouterReservation(nouvelleReservation)

        val emailService = EmailService()
        val sujet = "Confirmation de réservation"
        val contenu = """
        Bonjour ${client.nom},

        Votre réservation pour la chambre ${chambre!!.typeChambre} a été confirmée.
        Dates : $dateDebutReservation - $dateFinReservation
        Prix total : ${chambre!!.prixParNuit * nouvelleReservation.calculerNombreDeNuits()} CAD.

        Merci pour votre confiance.
        L'équipe EasyStay
    """.trimIndent()

        emailService.envoyerEmail(client.email, sujet, contenu) {
            vue.requireActivity().runOnUiThread {
                Toast.makeText(vue.requireContext(), "Réservation confirmée", Toast.LENGTH_SHORT).show()
            }
        }

        vue.requireActivity().runOnUiThread {
            (vue as Fragment).findNavController().navigate(R.id.action_reserverFragment_to_fragment_listeReservations)
        }
    }

    override fun gererBoutonRetourCliquer() {
        modèle.getCheminVersReservation()?.let { vue.findNavController().navigate(it) }
    }


}