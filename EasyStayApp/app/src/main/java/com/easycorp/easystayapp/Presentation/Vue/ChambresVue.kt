package com.easycorp.easystayapp.Presentation.Vue

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChambresVue : Fragment() {

    private lateinit var rechercher: EditText
    private lateinit var carte1: CardView
    private lateinit var carte2: CardView
    private lateinit var carte3: CardView
    private lateinit var filterIcon: ImageView
    private lateinit var imageButton1: ImageButton
    private lateinit var imageButton2: ImageButton
    private lateinit var imageButton3: ImageButton
    private var isFilterApplied = false


    private val chambres = listOf(
        ChambreData("Chambre Deluxe", "Vue sur la mer", 4.5f, 120, listOf("Wi-Fi", "TV"), 250.0, 20.0),
        ChambreData("Suite Junior", "Balcon privé", 4.8f, 85, listOf("Wi-Fi", "Climatisation"), 320.0, 25.0),
        ChambreData("Chambre Standard", "Lit queen-size", 4.2f, 200, listOf("Wi-Fi", "Bureau"), 180.0, 15.0)
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
        carte1 = view.findViewById(R.id.carte1)
        carte2 = view.findViewById(R.id.carte2)
        carte3 = view.findViewById(R.id.carte3)
        imageButton1 = view.findViewById(R.id.imageButton)
        imageButton2 = view.findViewById(R.id.imageButton1)
        imageButton3 = view.findViewById(R.id.imageButton6)
        filterIcon = view.findViewById(R.id.filter)

        setupImageClick(imageButton1, chambres[0])
        setupImageClick(imageButton2, chambres[1])
        setupImageClick(imageButton3, chambres[2])

        setupCardView(carte1, chambres[0],
            R.id.textView5,
            R.id.textView3,
            R.id.textView6,
            R.id.textView7
        )
        setupCardView(carte2, chambres[1],
            R.id.textView8,
            R.id.textView4,
            R.id.textView9,
            R.id.textView10
        )
        setupCardView(carte3, chambres[2],
            R.id.textView13,
            R.id.textView11,
            R.id.textView14,
            R.id.textView15
        )

        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val searchText = rechercher.text.toString().trim().lowercase()
                CoroutineScope(Dispatchers.Main).launch {
                    applyFiltersAndSearch(searchText)
                }
                delay(100)
            }
        }

        filterIcon.setOnClickListener {
            if (isFilterApplied) {
                maxPrice = 500
                selectedType = null
                applyFiltersAndSearch(rechercher.text.toString().trim().lowercase())
                isFilterApplied = false
            } else {
                showFilterDialog()
                isFilterApplied = true
            }
        }
    }

    private fun setupImageClick(imageButton: ImageButton, chambre: ChambreData) {
        imageButton.setOnClickListener {
            openDetailsFragment(chambre)
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
            findNavController().navigate(R.id.action_fragment_chambres_to_chambreDetailsFragment, bundle)
        } catch (e: IllegalArgumentException) {
            Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showFilterDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_filter, null)
        val priceSeekBar = dialogView.findViewById<SeekBar>(R.id.priceSeekBar)
        val priceTextView = dialogView.findViewById<TextView>(R.id.priceTextView)
        val typeSpinner = dialogView.findViewById<Spinner>(R.id.typeSpinner)
        val applyButton = dialogView.findViewById<Button>(R.id.applyFilterButton)

        val types = listOf("Toutes les chambres") + chambres.map { it.typeChambre }.distinct()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = adapter

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
            applyFiltersAndSearch(rechercher.text.toString().trim().lowercase())
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun applyFiltersAndSearch(searchText: String) {
        carte1.visibility = if (chambres[0].matchesFilter(searchText, maxPrice, selectedType)) View.VISIBLE else View.GONE
        carte2.visibility = if (chambres[1].matchesFilter(searchText, maxPrice, selectedType)) View.VISIBLE else View.GONE
        carte3.visibility = if (chambres[2].matchesFilter(searchText, maxPrice, selectedType)) View.VISIBLE else View.GONE
    }

    private fun ChambreData.matchesFilter(searchText: String, maxPrice: Int, selectedType: String?): Boolean {
        val matchesPrice = prixParNuit <= maxPrice
        val matchesType = selectedType == "Toutes les chambres" || selectedType == null || typeChambre == selectedType
        val matchesSearch = searchText.isEmpty() || typeChambre.lowercase().contains(searchText)
        return matchesPrice && matchesType && matchesSearch
    }

    private fun setupCardView(
        cardView: CardView,
        chambreData: ChambreData,
        typeChambreId: Int,
        noteId: Int,
        prixParNuitId: Int,
        descriptionId: Int
    ) {
        val typeChambre = cardView.findViewById<TextView>(typeChambreId)
        val note = cardView.findViewById<TextView>(noteId)
        val prixParNuit = cardView.findViewById<TextView>(prixParNuitId)
        val description = cardView.findViewById<TextView>(descriptionId)

        typeChambre.text = chambreData.typeChambre
        note.text = "Note: ${chambreData.note} (${chambreData.nombreAvis} avis)"
        prixParNuit.text = "${chambreData.prixParNuit}$ / nuit"
        description.text = chambreData.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
