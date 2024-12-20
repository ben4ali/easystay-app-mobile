package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.easycorp.easystayapp.Presentation.Presentateur.ListeChambres.ListeChambresPresentateur
import com.easycorp.easystayapp.R

class ChambresVue : Fragment() {

    private lateinit var présentateur: ListeChambresPresentateur
    private lateinit var rechercher: EditText
    lateinit var listViewChambres: ListView
    private lateinit var filterIcon: ImageView

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

        rechercher.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                présentateur.rechercherChambres(s.toString().trim().lowercase())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        filterIcon.setOnClickListener {
            présentateur.appliquerOuEffacerFiltres()
        }

        présentateur = ListeChambresPresentateur(this, requireContext())
        présentateur.chargerChambres()
    }
}