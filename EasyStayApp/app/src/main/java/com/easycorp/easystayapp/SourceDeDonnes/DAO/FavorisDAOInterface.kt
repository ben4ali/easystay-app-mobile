package com.easycorp.easystayapp.SourceDeDonnes

interface FavorisDAOInterface {
    fun ajouterFavoris(roomId: Int)
    fun retirerFavoris(roomId: Int)
    fun estFavoris(roomId: Int): Boolean
    fun obtenirTousLesFavoris(): List<Int>
}