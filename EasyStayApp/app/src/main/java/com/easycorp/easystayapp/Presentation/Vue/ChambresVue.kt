package com.easycorp.easystayapp.Presentation.Vue

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
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Presentateur.ListeChambresPresentateur
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter

class ChambresVue : Fragment(), ChambresVueInterface {

    private lateinit var rechercher: EditText
    private lateinit var listViewChambres: ListView
    private lateinit var filterIcon: ImageView
    private lateinit var presenter: ListeChambresPresentateur

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chambres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = ListeChambresPresentateur(this, requireContext())
        rechercher = view.findViewById(R.id.rechercher)
        listViewChambres = view.findViewById(R.id.listView_chambres)
        filterIcon = view.findViewById(R.id.filter)

        rechercher.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.filtrerChambres(s.toString().trim().lowercase())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        filterIcon.setOnClickListener {
            presenter.appliquerOuEffacerFiltres()
        }


        presenter.chargerChambres()
    }

    override fun afficherChambres(chambres: List<ChambreData>) {
        val adapter = ChambreAdapter(requireContext(), chambres.toMutableList()) { chambre ->
            presenter.ouvrirDetailsChambre(chambre)
        }
        listViewChambres.adapter = adapter
    }

    override fun ouvrirDetailsChambre(actionId: Int, bundle: Bundle) {
                findNavController().navigate(actionId, bundle)
    }

    override fun afficherMessageErreur(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun afficherFiltreDialog() {
        presenter.afficherFilterDialog()
    }
}
