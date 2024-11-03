package com.easycorp.easystayapp

import ImageSliderAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2

class fragment_chambre_details : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chambre_details, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        val images = intArrayOf(R.drawable.chambre_exemple1, R.drawable.chambre_exemple2, R.drawable.chambre_exemple3)
        val adapter = ImageSliderAdapter(requireContext(), images)
        viewPager.adapter = adapter

        return view
    }
}