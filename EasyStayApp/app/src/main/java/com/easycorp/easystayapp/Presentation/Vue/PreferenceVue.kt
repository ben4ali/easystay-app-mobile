package com.easycorp.easystayapp.Presentation.Vue

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
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
    private lateinit var editImageIcon: ImageView
    private lateinit var editNomIcon: ImageView
    private lateinit var editEmailIcon: ImageView
    private lateinit var editPrenomIcon: ImageView

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
        editImageIcon = view.findViewById(R.id.editImageIcon)
        editNomIcon = view.findViewById(R.id.editNomIcon)
        editEmailIcon = view.findViewById(R.id.editEmailIcon)
        editPrenomIcon = view.findViewById(R.id.editPrenomIcon)
        présentateur.afficherClient(clientId = 1)

        editImageIcon.setOnClickListener {
            présentateur.ouvrirCamera(this)
        }

        editNomIcon.setOnClickListener {
            présentateur.modifierNom(this,nomTextView.text.toString())
        }
        editEmailIcon.setOnClickListener {
            présentateur.modifierEmail(this,emailTextView.text.toString())
        }
        editPrenomIcon.setOnClickListener {
            présentateur.modifierPrenom(this,prénomTextView.text.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        présentateur.traiterResultatCamera(requestCode, resultCode, data)
    }

    override fun afficherClient(prénom: String, nom: String, email: String, photoResId: Int) {
        prénomTextView.text = prénom
        nomTextView.text = nom
        emailTextView.text = email
        photoProfilImageView.setImageResource(photoResId)
    }

    override fun afficherPhotoProfil(photo: Bitmap) {
        photoProfilImageView.setImageBitmap(photo)
    }
}