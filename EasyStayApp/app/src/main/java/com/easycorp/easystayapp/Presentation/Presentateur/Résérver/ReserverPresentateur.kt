package com.easycorp.easystayapp.Presentation.Presentateur.Résérver

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.ReserverVue
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Domaine.Service.EmailService
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReserverPresentateur(private val vue: ReserverVue, private val context: Context) : ReserverPresentateurInterface {

    val modèle = Modèle.getInstance(context)

    var dateDebut: Calendar? = null
    var dateFin: Calendar? = null
    var chambreId: Int? = null
    var dateDebutInitiale: Calendar? = null
    var dateFinInitiale: Calendar? = null
    lateinit var dateDebutFormatter: String
    lateinit var dateFinFormatter: String

    lateinit var chambre: ChambreData
    lateinit var réservation: ReservationData

    override fun ouvrirDetailsRéservation() {
        CoroutineScope(Dispatchers.IO).launch {
            val id = modèle.getReservationChoisieId()
            Log.d("ID", id.toString())
            if (id != null) {
                val reservation = modèle.obtenirReservationParId(id)
                if (reservation != null) {
                    réservation = reservation
                    val chambreData = modèle.obtenirChambreParId(réservation.chambre.id)
                    if (chambreData != null) {
                        chambre = chambreData
                        CoroutineScope(Dispatchers.Main).launch {
                            val dateFormatageAffichageInitiale = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA_FRENCH)
                            val dateFormatageAffichageFinal = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)

                            val dateDebut = dateFormatageAffichageInitiale.parse(réservation.dateDébut)
                            val dateFin = dateFormatageAffichageInitiale.parse(réservation.dateFin)

                            dateDebutFormatter = dateFormatageAffichageFinal.format(dateDebut!!)
                            dateFinFormatter = dateFormatageAffichageFinal.format(dateFin!!)

                            vue.modifierDetailsChambre(chambre)
                        }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, "Erreur lors du chargement de la chambre", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Erreur lors du chargement des données de la réservation", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Erreur lors du chargement de la réservation", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun calculerPrixTotale() {
        val nuits = réservation.calculerNombreDeNuits()
        val sousTotal = réservation.chambre.prixParNuit * nuits
        val taxes = sousTotal * 0.15
        val total = sousTotal + taxes

        CoroutineScope(Dispatchers.Main).launch {
            vue.modifierPrixTotale(réservation.chambre.prixParNuit, nuits, sousTotal, taxes, total)
        }
    }

    /*override fun gererConfirmationReservation() {
        CoroutineScope(Dispatchers.IO).launch {
            val client = modèle.obtenirClientParId(1)
            val dateDebutReservation = réservation.dateDébut
            val dateFinReservation = réservation.dateFin

            val nouvelleReservation = ReservationData(
                id = 7,
                client = client,
                chambreNumero = chambre.id.toString(),
                dateDébut = dateDebutReservation,
                dateFin = dateFinReservation,
                prixTotal = chambre.prixParNuit * réservation.calculerNombreDeNuits(),
                statut = "Confirmée",
                methodePaiement = "Carte de crédit",
                statusPaiement = true,
                datePaiement = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).format(Date()),
                chambre = chambre
            )

            CoroutineScope(Dispatchers.IO).launch {
                modèle.ajouterReservation(nouvelleReservation, client, chambre)
                CoroutineScope(Dispatchers.Main).launch {
                    vue.modifierDetailsChambre(chambre)
                    Toast.makeText(context, "Réservation confirmée", Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.IO).launch {
                        modèle.getCheminVersReservation()?.let {
                            (vue as Fragment).findNavController().navigate(it)
                        }
                    }
                }
            }


            /*
            val emailService = EmailService()
            val sujet = "Confirmation de réservation"
            val contenu =
                """
                Bonjour ${client.nom},

                Votre réservation pour la chambre ${chambre.typeChambre} a été confirmée.
                Dates : $dateDebutReservation - $dateFinReservation
                Prix total : ${chambre.prixParNuit * réservation.calculerNombreDeNuits()} CAD.

                Merci pour votre confiance.
                L'équipe EasyStay
                """.trimIndent()

            emailService.envoyerEmail(client.courriel, sujet, contenu) {
                vue.requireActivity().runOnUiThread {
                    Toast.makeText(vue.requireContext(), "Réservation confirmée", Toast.LENGTH_SHORT).show()
                }
            }
            */
        }
    }*/

    override fun gererBoutonRetourCliquer() {
        modèle.getCheminVersReservation()?.let { vue.findNavController().navigate(it) }
    }
}