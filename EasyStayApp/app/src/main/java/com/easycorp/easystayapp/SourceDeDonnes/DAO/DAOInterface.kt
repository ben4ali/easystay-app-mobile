package com.easycorp.easystayapp.SourceDeDonnes.DAO

interface DAOInterface<T> {
    fun ajouter(id: Int)
    fun retirer(id: Int)
    fun obtenirTous(): List<T>
    fun modifier(id: Int)
}