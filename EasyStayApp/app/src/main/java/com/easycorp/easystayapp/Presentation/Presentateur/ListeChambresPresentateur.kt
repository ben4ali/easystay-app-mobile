package com.easycorp.easystayapp.Presentation.Presentateur

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.ChambresVue
import com.easycorp.easystayapp.R

class ListeChambresPresentateur(
    private val vue: ChambresVue,
    private val context: Context
) {
    private val modèle = Modèle.getInstance()
    private var maxPrice = 500
    private var selectedType: String? = null
    private var isFilterApplied = false

    fun chargerChambres() {
        val chambres = modèle.obtenirChambres()
        vue.afficherChambres(chambres)
    }

    fun filtrerChambres(searchText: String) {
        val chambres = modèle.obtenirChambres()
        val filteredChambres = chambres.filter { it.matchesFilter(searchText, maxPrice, selectedType) }
        vue.afficherChambres(filteredChambres)
    }

    fun appliquerOuEffacerFiltres() {
        if (isFilterApplied) {
            maxPrice = 500
            selectedType = null
            isFilterApplied = false
            vue.afficherChambres(modèle.obtenirChambres())
        } else {
            afficherFilterDialog()
        }
    }

    fun ouvrirDetailsChambre(chambre: ChambreData) {
        modèle.setChambreChoisieId(chambre.id)
        vue.ouvrirDetailsChambre(actionId = R.id.action_fragment_chambres_to_chambreDetailsFragment)
    }

    fun afficherFilterDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_filter, null)
        val priceSeekBar = dialogView.findViewById<SeekBar>(R.id.priceSeekBar)
        val priceTextView = dialogView.findViewById<TextView>(R.id.priceTextView)
        val typeSpinner = dialogView.findViewById<Spinner>(R.id.typeSpinner)
        val applyButton = dialogView.findViewById<Button>(R.id.applyFilterButton)

        val types = listOf("Toutes les chambres") + modèle.obtenirChambres().map { it.typeChambre }.distinct()
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

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        applyButton.setOnClickListener {
            maxPrice = priceSeekBar.progress
            selectedType = if (typeSpinner.selectedItem.toString() == "Toutes les chambres") null else typeSpinner.selectedItem.toString()
            isFilterApplied = true
            filtrerChambres("")
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun ChambreData.matchesFilter(searchText: String, maxPrice: Int, selectedType: String?): Boolean {
        val matchesPrice = prixParNuit <= maxPrice
        val matchesType = selectedType == "Toutes les chambres" || selectedType == null || typeChambre == selectedType
        val matchesSearch = searchText.isEmpty() || typeChambre.lowercase().contains(searchText)
        return matchesPrice && matchesType && matchesSearch
    }
}