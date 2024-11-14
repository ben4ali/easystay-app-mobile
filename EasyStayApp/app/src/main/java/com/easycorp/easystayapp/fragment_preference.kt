package com.easycorp.easystayapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

class fragment_preference : Fragment() {

    lateinit var switch: Switch
    lateinit var radioFrancais: RadioButton
    lateinit var radioAnglais: RadioButton

    val clientList = listOf(
        ClientData(prénom = "Patrick", nom = "Lafrance", email = "plafrance@gmail.com")
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preference, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = clientList[0]


        val prénomTextView = view.findViewById<TextView>(R.id.prenomTextView)
        val nomTextView = view.findViewById<TextView>(R.id.nomTextView)
        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)

        prénomTextView.text = client.prénom
        nomTextView.text = client.nom
        emailTextView.text = client.email

        switch = view.findViewById(R.id.modeSombre)
        radioFrancais = view.findViewById(R.id.radioFrancais)
        radioAnglais = view.findViewById(R.id.radioAnglais)

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switch.isChecked = true
        }

        val currentLocale = Locale.getDefault().language
        radioFrancais.isChecked = currentLocale == "fr"
        radioAnglais.isChecked = currentLocale == "en"

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        radioFrancais.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setLocale(Locale("fr"))
                radioAnglais.isChecked = false
            }
        }

        radioAnglais.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setLocale(Locale("en"))
                radioFrancais.isChecked = false
            }
        }
    }

    private fun setLocale(locale: Locale) {
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
        requireActivity().recreate()
    }
}
