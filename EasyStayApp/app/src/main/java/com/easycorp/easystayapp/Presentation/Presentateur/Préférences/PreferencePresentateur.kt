package com.easycorp.easystayapp.Presentation.Presentateur.Préférences

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.R

class PreferencePresentateur(
    val vue: PreferencePresentateurInterface
) {
    var modèle = Modèle.getInstance()

    fun afficherClient(clientId: Int) {
        val client = modèle.obtenirClientParId(clientId)
        vue.afficherClient(
            prénom = client.prénom,
            nom = client.nom,
            email = client.email,
            photoResId = client.photo ?: R.drawable.profil_icon
        )
    }

    fun ouvrirCamera(fragment: Fragment) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fragment.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    fun traiterResultatCamera(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            vue.afficherPhotoProfil(imageBitmap)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }
}