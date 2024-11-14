package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.R
import java.util.Locale


class PreferenceVue : Fragment() {

    lateinit var switch: Switch
    lateinit var radioFrancais: RadioButton
    lateinit var radioAnglais: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preference, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var modele = Modèle()
        val client = modele.obtenirClientParId(1)


        val prénomTextView = view.findViewById<TextView>(R.id.prenomTextView)
        val nomTextView = view.findViewById<TextView>(R.id.nomTextView)
        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
        val photoProfilImageView = view.findViewById<ImageView>(R.id.photoProfilImageView)


        prénomTextView.text = client.prénom
        nomTextView.text = client.nom
        emailTextView.text = client.email

        if (client.photo != null){
            photoProfilImageView.setImageResource(client.photo)
        } else{
            photoProfilImageView.setImageResource(R.drawable.profil_icon)
        }



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
