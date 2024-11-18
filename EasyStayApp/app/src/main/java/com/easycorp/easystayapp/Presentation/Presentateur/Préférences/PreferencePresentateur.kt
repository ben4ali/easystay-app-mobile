package com.easycorp.easystayapp.Presentation.Presentateur.Préférences

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.R
import androidx.appcompat.app.AlertDialog
import android.widget.EditText
import android.widget.Toast


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

    fun modifierNom(fragment: Fragment, nom: String) {
        modifierAttribut(
            fragment,
            titre = "Modifier le nom",
            valeurActuelle = nom,
            fonctionDeMiseAJour = { nouveauNom -> modèle.updateClientName(nouveauNom) }
        )
    }

    fun modifierEmail(fragment: Fragment, email: String) {
        modifierAttribut(
            fragment,
            titre = "Modifier l'email",
            valeurActuelle = email,
            fonctionDeMiseAJour = { nouvelEmail -> modèle.updateClientEmail(nouvelEmail) }
        )
    }

    fun modifierPrenom(fragment: Fragment, prénom: String) {
        modifierAttribut(
            fragment,
            titre = "Modifier le prénom",
            valeurActuelle = prénom,
            fonctionDeMiseAJour = { nouveauPrenom -> modèle.updateClientSurname(nouveauPrenom) }
        )
    }

    fun modifierAttribut(
        fragment: Fragment,
        titre: String,
        valeurActuelle: String,
        fonctionDeMiseAJour: (String) -> Unit
    ) {
        val constructeur = AlertDialog.Builder(fragment.requireContext())
        val saisieTexte = EditText(fragment.requireContext()).apply {
            setText(valeurActuelle)
            setSelection(valeurActuelle.length)
        }
        constructeur.setTitle(titre)
        constructeur.setView(saisieTexte)
        constructeur.setPositiveButton("OK") { dialog, _ ->
            val nouvelleValeur = saisieTexte.text.toString().trim()
            if (nouvelleValeur.isEmpty()) {
                Toast.makeText(fragment.requireContext(), "Ce champ ne peut pas être vide", Toast.LENGTH_SHORT).show()
            } else {
                fonctionDeMiseAJour(nouvelleValeur)
                afficherClient(clientId = 1)
                dialog.dismiss()
            }
        }
        constructeur.setNegativeButton("Annuler") { dialog, _ ->
            dialog.cancel()
        }
        constructeur.show()
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