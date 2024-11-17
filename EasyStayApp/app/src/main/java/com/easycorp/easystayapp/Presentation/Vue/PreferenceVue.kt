package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.easycorp.easystayapp.Presentation.Presentateur.Préférences.PreferencePresentateur
import com.easycorp.easystayapp.Presentation.Presentateur.Préférences.PreferencePresentateurInterface
import com.easycorp.easystayapp.R

class PreferenceVue : Fragment(), PreferencePresentateurInterface {
    private lateinit var présentateur: PreferencePresentateur
    private lateinit var prénomTextView: TextView
    private lateinit var nomTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var photoProfilImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preference, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        présentateur = PreferencePresentateur(this)
        prénomTextView = view.findViewById(R.id.prenomTextView)
        nomTextView = view.findViewById(R.id.nomTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        photoProfilImageView = view.findViewById(R.id.photoProfilImageView)

        présentateur.afficherClient(clientId = 1)
    }

    override fun afficherClient(prénom: String, nom: String, email: String, photoResId: Int) {
        prénomTextView.text = prénom
        nomTextView.text = nom
        emailTextView.text = email
        photoProfilImageView.setImageResource(photoResId)
    }
}
