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
import com.easycorp.easystayapp.Presentation.Presentateur.PreferencePresentateur
import com.easycorp.easystayapp.R
import java.util.Locale


class PreferenceVue : Fragment(), PreferencePresentateurInterface {

    lateinit var présentateur: PreferencePresentateur

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preference, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        présentateur = PreferencePresentateur(this)
        présentateur.afficherClient(clientId = 1)
    }

    override fun afficherClient(prénom: String, nom: String, email: String, photoResId: Int) {
        val prénomTextView = view?.findViewById<TextView>(R.id.prenomTextView)
        val nomTextView = view?.findViewById<TextView>(R.id.nomTextView)
        val emailTextView = view?.findViewById<TextView>(R.id.emailTextView)
        val photoProfilImageView = view?.findViewById<ImageView>(R.id.photoProfilImageView)

        prénomTextView?.text = prénom
        nomTextView?.text = nom
        emailTextView?.text = email
        photoProfilImageView?.setImageResource(photoResId)
    }
}
