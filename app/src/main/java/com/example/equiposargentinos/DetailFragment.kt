package com.example.equiposargentinos

import android.app.SearchManager
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
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import java.util.*


class DetailFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var geocoder: Geocoder
    private val args: DetailFragmentArgs by navArgs()

    lateinit var teamName: TextView
    lateinit var abrv: TextView
    lateinit var stadiumName: TextView
    lateinit var stadiumImg: ImageView
    lateinit var stadiumLoc: TextView
    lateinit var stadiumCap: TextView
    lateinit var stadiumWebsite: TextView
    lateinit var teamBadge: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_detail, container, false)

        val team = args.team

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        geocoder = Geocoder(requireActivity(), Locale.getDefault())

        teamName = rootView.findViewById<TextView>(R.id.lbl_name)
        abrv = rootView.findViewById<TextView>(R.id.lbl_abrv)
        stadiumName = rootView.findViewById<TextView>(R.id.lbl_stadium_name)
        stadiumImg = rootView.findViewById<ImageView>(R.id.img_stadium)
        stadiumLoc = rootView.findViewById<TextView>(R.id.lbl_stadium_loc)
        stadiumCap = rootView.findViewById<TextView>(R.id.lbl_stadium_cap)
        stadiumWebsite = rootView.findViewById<TextView>(R.id.lbl_webpage)
        teamBadge = rootView.findViewById<ImageView>(R.id.img_crest)

        setTeamData(team)

        stadiumWebsite.setOnClickListener {
            var url = team.strWebsite

            if(!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url

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
        stadiumName.text = team.strStadium
        stadiumLoc.text = getString(R.string.stadium_location, team.strStadiumLocation)
        stadiumCap.text = getString(R.string.stadium_cap, team.intStadiumCapacity.toString())
        abrv.text = team.strTeamShort
        stadiumWebsite.text = team.strWebsite
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        val location = stadiumLoc.text.toString()
        if (location.isNotEmpty()) {
            val address = geocoder.getFromLocationName(location, 1).firstOrNull()
            val stadium: LatLng
            if (address != null){
                stadium = LatLng(address.latitude, address.longitude)
            } else {
                stadium = LatLng(0.0, 0.0)
                Toast.makeText(requireContext(), "Club has no stadium", Toast.LENGTH_LONG)
                    .show()
            }
            mMap.addMarker(MarkerOptions().position(stadium).title(stadiumName.text.toString()))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium, 10.0f))
        }
    }
}