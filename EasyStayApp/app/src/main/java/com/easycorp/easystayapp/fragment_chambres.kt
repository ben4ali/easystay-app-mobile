package com.easycorp.easystayapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.cardview.widget.CardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class fragment_chambres : Fragment() {

    lateinit var rechercher : EditText
    lateinit var carte1 : CardView
    lateinit var carte2 : CardView
    lateinit var carte3 : CardView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chambres, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rechercher = view.findViewById(R.id.rechercher)
        carte1 = view.findViewById(R.id.carte1)
        carte2 = view.findViewById(R.id.carte2)
        carte3 = view.findViewById(R.id.carte3)

        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val text = rechercher.text.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    if (text.isNotEmpty()) {
                        carte1.visibility = View.GONE
                        carte2.visibility = View.GONE
                        carte3.visibility = View.GONE
                    } else {
                        carte1.visibility = View.VISIBLE
                        carte2.visibility = View.VISIBLE
                        carte3.visibility = View.VISIBLE
                    }
                }
                delay(100)
            }
        }
    }
}