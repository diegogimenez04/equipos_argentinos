package com.example.equiposargentinos

import android.graphics.drawable.Drawable
import android.media.Image
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

class DetailFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_detail, container, false)

        val team = args.team

        teamName = rootView.findViewById<TextView>(R.id.lbl_name)
        abrv = rootView.findViewById<TextView>(R.id.lbl_abrv)
        stadiumName = rootView.findViewById<TextView>(R.id.lbl_stadium_name)
        stadiumImg = rootView.findViewById<ImageView>(R.id.img_stadium)
        stadiumLoc = rootView.findViewById<TextView>(R.id.lbl_stadium_location)
        stadiumCap =    rootView.findViewById<TextView>(R.id.lbl_stadium_cap)
        stadiumWebsite = rootView.findViewById<TextView>(R.id.lbl_webpage)
        teamBadge = rootView.findViewById<ImageView>(R.id.img_crest)

        setTeamData(team)

        return rootView
    }

    private fun setTeamData(team: Team) {
        Glide.with(this)
            .load(team.strTeamBadge)
            .listener(object: RequestListener<Drawable> {
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
            .listener(object: RequestListener<Drawable> {
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
}