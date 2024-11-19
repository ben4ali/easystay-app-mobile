package com.easycorp.easystayapp.Domaine.Entite

import com.easycorp.easystayapp.R

data class ClientData(
    val id: Int,
    var prénom : String,
    var nom : String,
    var email: String,
    var photo: Int? = R.drawable.photo_profil_1
 ) {

 }