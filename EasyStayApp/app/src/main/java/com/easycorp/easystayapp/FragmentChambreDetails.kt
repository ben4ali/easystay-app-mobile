package com.easycorp.easystayapp

import ImageSliderAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2

class FragmentChambreDetails : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chambre_details, container, false)
        viewPager = view.findViewById(R.id.viewPager)

        val images = intArrayOf(R.drawable.chambre_exemple1, R.drawable.chambre_exemple2, R.drawable.chambre_exemple3)
        val adapter = ImageSliderAdapter(requireContext(), images)
        viewPager.adapter = adapter

        Log.d("ChambreDetailsFragment", "ViewPager2 ID: ${view.findViewById<View>(R.id.viewPager)}")

        return view
    }
}