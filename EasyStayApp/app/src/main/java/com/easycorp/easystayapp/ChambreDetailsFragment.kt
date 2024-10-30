package com.easycorp.easystayapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class ChambreDetailsFragment : Fragment(), MyView {

    private lateinit var presenter: Presentateur

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chambre_details, container, false)

        presenter = Presentateur(this)

        presenter.loadData()

        val btnNaviguer = view.findViewById<Button>(R.id.bouton_naviguer)
        btnNaviguer.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_conteneur, fragment_test())
            fragmentTransaction.commit()
        }

        return view
    }

    override fun showData(data: String) {
        view?.findViewById<TextView>(R.id.textView)?.text = data
    }
}