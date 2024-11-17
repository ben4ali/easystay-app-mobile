package com.easycorp.easystayapp.Domaine.Entite

import com.easycorp.easystayapp.R

data class ClientData(
     val id: Int,
     val prénom : String,
     val nom : String,
     val email: String,
     val photo: Int? = R.drawable.photo_profil_1
 ) {

 }