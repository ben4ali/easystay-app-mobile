package com.easycorp.easystayapp.Presentation.Presentateur.ListeChambres

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.ChambresVue
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListeChambresPresentateur(
    private val vue: ChambresVue,
    private val context: Context
) {
    private val modèle = Modèle.getInstance(context)
    private var maxPrice = 500
    private var selectedType: String? = null
    private var isFilterApplied = false

    fun chargerChambres() {
        CoroutineScope(Dispatchers.Main).launch {
            val chambres = withContext(Dispatchers.IO) { modèle.obtenirChambres() }
            afficherChambresOuMessage(chambres)
        }
    }

    fun rechercherChambres(keyword: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val chambres = withContext(Dispatchers.IO) { modèle.rechercherChambresParMotCle(keyword) }
            afficherChambresOuMessage(chambres)
        }
    }

    fun filtrerChambres(type: String?, maxPrice: Int?) {
        CoroutineScope(Dispatchers.Main).launch {
            val chambres = withContext(Dispatchers.IO) { modèle.filtrerChambres(type, maxPrice) }
            afficherChambresOuMessage(chambres)
        }
    }

    fun appliquerOuEffacerFiltres() {
        if (isFilterApplied) {
            resetFiltres()
        } else {
            afficherFilterDialog()
        }
    }

    private fun resetFiltres() {
        maxPrice = 500
        selectedType = null
        isFilterApplied = false
        chargerChambres()
    }

    private fun afficherFilterDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_filter, null)
        val priceSeekBar = dialogView.findViewById<SeekBar>(R.id.priceSeekBar)
        val priceTextView = dialogView.findViewById<TextView>(R.id.priceTextView)
        val typeSpinner = dialogView.findViewById<Spinner>(R.id.typeSpinner)
        val applyButton = dialogView.findViewById<Button>(R.id.applyFilterButton)

        CoroutineScope(Dispatchers.Main).launch {
            setupFilterDialog(priceSeekBar, priceTextView, typeSpinner)

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            applyButton.setOnClickListener {
                maxPrice = priceSeekBar.progress
                selectedType = if (typeSpinner.selectedItem.toString() == "Toutes les chambres") null else typeSpinner.selectedItem.toString()
                isFilterApplied = true
                filtrerChambres(selectedType, maxPrice)
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }

    private suspend fun setupFilterDialog(
        priceSeekBar: SeekBar,
        priceTextView: TextView,
        typeSpinner: Spinner
    ) {
        val types = withContext(Dispatchers.IO) {
            listOf("Toutes les chambres") + (modèle.obtenirChambres()?.map { it.typeChambre }?.distinct() ?: emptyList())
        }

        val adapterSpinner = ArrayAdapter(context, android.R.layout.simple_spinner_item, types)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = adapterSpinner

        priceSeekBar.progress = maxPrice
        priceTextView.text = "Prix maximum sélectionné : ${maxPrice}$"

        priceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                priceTextView.text = "Prix maximum sélectionné : ${progress}$"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun afficherChambres(chambres: List<ChambreData>) {
        val adapter = ChambreAdapter(context, chambres.toMutableList()) { chambre ->
            ouvrirDetailsChambre(chambre)
        }
        vue.listViewChambres.adapter = adapter
    }

    private fun afficherChambresOuMessage(chambres: List<ChambreData>?) {
        if (chambres.isNullOrEmpty()) {
            afficherMessageErreur("Aucune chambre disponible.")
        } else {
            afficherChambres(chambres)
        }
    }

    fun ouvrirDetailsChambre(chambre: ChambreData) {
        modèle.setChambreChoisieId(chambre.id)
        modèle.setCheminVersChambreDetails(R.id.action_chambreDetailsFragment_to_fragment_chambres)
        modèle.setCheminVersReservation(R.id.action_reserverFragment_to_chambreDetailsFragment)
        vue.findNavController().navigate(R.id.action_fragment_chambres_to_chambreDetailsFragment)
    }

    private fun afficherMessageErreur(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}