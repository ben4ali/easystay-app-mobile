package com.easycorp.easystayapp

class Presentateur(private val view: MyView) {

    fun loadData() {
        val data = "Données simulées"
        view.showData(data)
    }

}