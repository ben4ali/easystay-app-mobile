package com.easycorp.easystayapp.Presentation.Presentateur.Préférences

import android.graphics.Bitmap

interface PreferencePresentateurInterface {
    fun afficherClient(prénom: String, nom: String, email: String, photoResId: Int)
    fun afficherPhotoProfil(photo: Bitmap)
}