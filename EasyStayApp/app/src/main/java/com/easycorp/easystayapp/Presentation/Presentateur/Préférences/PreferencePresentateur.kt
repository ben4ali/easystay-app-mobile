package com.easycorp.easystayapp.Presentation.Presentateur.Préférences

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import androidx.appcompat.app.AlertDialog
import android.widget.EditText
import android.widget.Toast
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.SourceDeDonnes.SourceBidon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PreferencePresentateur(
    val vue: PreferencePresentateurInterface,
    context: Context
) {
    var modèle = Modèle.getInstance(context)

    fun afficherClient(clientId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val client = withContext(Dispatchers.IO) {
                modèle.obtenirClientParId(clientId)
            }
            vue.afficherClient(
                prénom = client.prénom,
                nom = client.nom,
                email = client.courriel

            )
        }
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
            fonctionDeMiseAJour = { nouveauNom ->
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        modèle.modifierClientNom(clientId = 1, nouveauNom)
                    }
                    afficherClient(clientId = 1)
                }
            }
        )
    }

    fun modifierEmail(fragment: Fragment, email: String) {
        modifierAttribut(
            fragment,
            titre = "Modifier l'email",
            valeurActuelle = email,
            fonctionDeMiseAJour = { nouvelEmail ->
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        modèle.modifierClientCourriel(clientId = 1, nouvelEmail)
                    }
                    afficherClient(clientId = 1)
                }
            }
        )
    }

    fun modifierPrenom(fragment: Fragment, prénom: String) {
        modifierAttribut(
            fragment,
            titre = "Modifier le prénom",
            valeurActuelle = prénom,
            fonctionDeMiseAJour = { nouveauPrenom ->
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        modèle.modifierClientPrenom(clientId = 1, nouveauPrenom)
                    }
                    afficherClient(clientId = 1)
                }
            }
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
                dialog.dismiss()
            }
        }
        constructeur.setNegativeButton("Annuler") { dialog, _ ->
            dialog.cancel()
        }
        constructeur.show()
    }

    fun demanderAutorisationCamera(fragment: Fragment) {
        val permission = android.Manifest.permission.CAMERA

        if (fragment.requireContext().checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ouvrirCamera(fragment)
        } else {
            fragment.requestPermissions(arrayOf(permission), REQUEST_CAMERA_PERMISSION)
        }
    }

    fun traiterResultatPermissionCamera(requestCode: Int, grantResults: IntArray, fragment: Fragment) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                ouvrirCamera(fragment)
            } else {
                Toast.makeText(fragment.requireContext(), "L'autorisation de la caméra est requise pour prendre des photos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun traiterResultatCamera(requestCode: Int, resultCode: Int, data: Intent?, context: Context) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
//            modèle.modifierClientImage(imageBitmap, context)
//            vue.afficherPhotoProfil(imageBitmap)
        }
    }

    fun afficherPhotoProfil(clientId: Int, context: Context) {
        val clientImage = modèle.obtenirClientImage(context)
        if (clientImage != null) {
            vue.afficherPhotoProfil(clientImage)
        } else {
            val client = modèle.obtenirClientParId(clientId)
//            vue.afficherPhoto(client.photo!!)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_CAMERA_PERMISSION = 2
    }
}