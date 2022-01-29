package com.example.equiposargentinos.main_fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.Intent
import android.location.Geocoder
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.FragmentContainerView
import com.example.equiposargentinos.R
import com.example.equiposargentinos.Team
import com.example.equiposargentinos.main.MainActivity
import com.google.android.material.card.MaterialCardView
import java.lang.Exception
import java.util.*


class DetailFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private val args: DetailFragmentArgs by navArgs()

    private lateinit var teamName: TextView
    private lateinit var abrv: TextView
    private lateinit var stadiumName: TextView
    private lateinit var stadiumImg: ImageView
    private lateinit var stadiumLoc: TextView
    private lateinit var stadiumCap: TextView
    private lateinit var stadiumWebsite: TextView
    private lateinit var teamBadge: ImageView
    private lateinit var mapView: FragmentContainerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_detail, container, false)

        val btnReturn = rootView.findViewById<MaterialCardView>(R.id.return_button)

        val team = args.team

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        geocoder = Geocoder(requireActivity(), Locale.getDefault())

        teamName = rootView.findViewById(R.id.lbl_name)
        abrv = rootView.findViewById(R.id.lbl_abrv)
        stadiumName = rootView.findViewById(R.id.lbl_stadium_name)
        stadiumImg = rootView.findViewById(R.id.img_stadium)
        stadiumLoc = rootView.findViewById(R.id.lbl_stadium_loc)
        stadiumCap = rootView.findViewById(R.id.lbl_stadium_cap)
        stadiumWebsite = rootView.findViewById(R.id.lbl_webpage)
        teamBadge = rootView.findViewById(R.id.img_crest)
        mapView = rootView.findViewById(R.id.map)

        setTeamData(team)

        btnReturn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        stadiumWebsite.setOnClickListener {
            var url = team.strWebsite

            if(!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://$url"

            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            requireActivity().startActivity(intent)
        }

        return rootView
    }

    private fun setTeamData(team: Team) {
        Glide.with(this)
            .load(team.strTeamBadge)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .error(R.drawable.ic_baseline_image_not_supported_24)
            .into(teamBadge)

        Glide.with(this)
            .load(team.strStadiumThumb)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .error(R.drawable.ic_baseline_image_not_supported_24)
            .into(stadiumImg)

        teamName.text = team.strTeam
        stadiumName.text = getString(R.string.nombre_del_estadio, team.strStadium)
        stadiumLoc.text = getString(R.string.stadium_location, team.strStadiumLocation)
        stadiumCap.text = getString(R.string.stadium_cap, team.intStadiumCapacity.toString())
        abrv.text = team.strTeamShort
        stadiumWebsite.text = team.strWebsite
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        val location = stadiumLoc.text.toString()
        try {
            if (location.isNotEmpty()) {
                val address = geocoder.getFromLocationName(location, 1).firstOrNull()
                val stadium: LatLng
                if (address != null){
                    stadium = LatLng(address.latitude, address.longitude)
                } else {
                    stadiumName.visibility = View.GONE
                    stadiumLoc.visibility = View.GONE
                    stadiumCap.visibility = View.GONE
                    mapView.visibility = View.GONE
                    stadium = LatLng(0.0, 0.0)
                    Toast.makeText(requireContext(), "Club has no stadium", Toast.LENGTH_LONG)
                        .show()
                }
                mMap.addMarker(MarkerOptions().position(stadium).title(stadiumName.text.toString()))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium, 10.0f))
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
            mapView.visibility = View.GONE
        }

    }
}
