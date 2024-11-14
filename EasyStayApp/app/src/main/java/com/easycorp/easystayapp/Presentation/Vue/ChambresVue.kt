package com.easycorp.easystayapp.Presentation.Vue

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter

class ChambresVue : Fragment() {

    private lateinit var rechercher: EditText
    private lateinit var listViewChambres: ListView
    private lateinit var filterIcon: ImageView
    private var isFilterApplied = false

    private val chambres = listOf(
        ChambreData(1,"Chambre Deluxe", "Vue sur la mer", 4.5f, 120, listOf("Wi-Fi", "TV"), 250.0, 20.0),
        ChambreData(2,"Suite Junior", "Balcon privé", 4.8f, 85, listOf("Wi-Fi", "Climatisation"), 320.0, 25.0),
        ChambreData(3,"Chambre Standard", "Lit queen-size", 4.2f, 200, listOf("Wi-Fi", "Bureau"), 180.0, 15.0)
    )

    private var maxPrice = 500
    private var selectedType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chambres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rechercher = view.findViewById(R.id.rechercher)
        listViewChambres = view.findViewById(R.id.listView_chambres)
        filterIcon = view.findViewById(R.id.filter)

        val adapter = ChambreAdapter(requireContext(), chambres.toMutableList()) { chambre ->
            openDetailsFragment(chambre)
        }
        listViewChambres.adapter = adapter

        rechercher.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                applyFiltersAndSearch(adapter, s.toString().trim().lowercase())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        filterIcon.setOnClickListener {
            if (isFilterApplied) {
                maxPrice = 500
                selectedType = null
                applyFiltersAndSearch(adapter, rechercher.text.toString().trim().lowercase())
                isFilterApplied = false
            } else {
                showFilterDialog(adapter)
                isFilterApplied = true
            }
        }
    }

    private fun openDetailsFragment(chambre: ChambreData) {
        val bundle = Bundle().apply {
            putString("typeChambre", chambre.typeChambre)
            putString("description", chambre.description)
            putFloat("note", chambre.note)
            putInt("nombreAvis", chambre.nombreAvis)
            putDouble("prixParNuit", chambre.prixParNuit)
        }
        try {
            if (findNavController().currentDestination?.id == R.id.fragment_chambres) {
                findNavController().navigate(R.id.action_fragment_chambres_to_chambreDetailsFragment, bundle)
            } else {
                Toast.makeText(requireContext(), "Invalid navigation destination", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun applyFiltersAndSearch(adapter: ChambreAdapter, searchText: String) {
        val filteredChambres = chambres.filter { it.matchesFilter(searchText, maxPrice, selectedType) }
        adapter.clear()
        adapter.addAll(filteredChambres)
        adapter.notifyDataSetChanged()
    }

    private fun ChambreData.matchesFilter(searchText: String, maxPrice: Int, selectedType: String?): Boolean {
        val matchesPrice = prixParNuit <= maxPrice
        val matchesType = selectedType == "Toutes les chambres" || selectedType == null || typeChambre == selectedType
        val matchesSearch = searchText.isEmpty() || typeChambre.lowercase().contains(searchText)
        return matchesPrice && matchesType && matchesSearch
    }
    private fun showFilterDialog(adapter: ChambreAdapter) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_filter, null)
        val priceSeekBar = dialogView.findViewById<SeekBar>(R.id.priceSeekBar)
        val priceTextView = dialogView.findViewById<TextView>(R.id.priceTextView)
        val typeSpinner = dialogView.findViewById<Spinner>(R.id.typeSpinner)
        val applyButton = dialogView.findViewById<Button>(R.id.applyFilterButton)

        val types = listOf("Toutes les chambres") + chambres.map { it.typeChambre }.distinct()
        val adapterSpinner = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = adapterSpinner

        priceSeekBar.progress = maxPrice
        priceTextView.text = "Selected Price: ${maxPrice}$"
        selectedType?.let {
            val index = types.indexOf(it)
            if (index >= 0) {
                typeSpinner.setSelection(index)
            }
        }

        priceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                priceTextView.text = "Selected Price: ${progress}$"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        applyButton.setOnClickListener {
            maxPrice = priceSeekBar.progress
            selectedType = if (typeSpinner.selectedItem.toString() == "Toutes les chambres") null else typeSpinner.selectedItem.toString()
            applyFiltersAndSearch(adapter, rechercher.text.toString().trim().lowercase())
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}
