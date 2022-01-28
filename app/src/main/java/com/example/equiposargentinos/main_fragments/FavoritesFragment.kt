package com.example.equiposargentinos.main_fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.equiposargentinos.FbAdapter
import com.example.equiposargentinos.R
import com.example.equiposargentinos.Team
import com.example.equiposargentinos.main.MainActivity
import java.lang.ClassCastException

class FavoritesFragment : Fragment() {
    private lateinit var teamSelectListener: TeamSelectListener

    interface TeamSelectListener {
        fun onFavTeamSelected(team: Team)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        teamSelectListener = try {
            context as TeamSelectListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement TeamSelectListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favorites, container, false)

        val fbRecycler = rootView.findViewById<RecyclerView>(R.id.fb_recycler)
        fbRecycler.layoutManager = LinearLayoutManager(requireContext())

        val adapter = FbAdapter()
        fbRecycler.adapter = adapter

        var favList = (activity as MainActivity).loadFavorite()
        adapter.submitList(favList)

        adapter.onItemClickListener = {team ->
            teamSelectListener.onFavTeamSelected(team)
        }

        adapter.onFavClickListener = {team ->
            favList = handleFavorite(team, favList)
        }

        return rootView
    }

    private fun handleFavorite(team: Team, favTeams: ArrayList<Team>?): ArrayList<Team> {
        var favReturn = arrayListOf<Team>()
        if(favTeams != null){
            // If initialized I check that it is not duplicated
            if (!duplicated(team, favTeams)){
                favTeams.add(team)
                (activity as MainActivity).saveFavorites(favTeams)
            } else {
                favTeams.remove(team)
                (activity as MainActivity).saveFavorites(favTeams)
            }
            return favTeams
        }
        else {
            // If its not initialized, I check a previous initialization and if it is duplicated
            if ((activity as MainActivity).loadFavorite() != null) {
                favReturn = (activity as MainActivity).loadFavorite()!!
                if (!duplicated(team, favReturn)){
                    favReturn.add(team)

                    (activity as MainActivity).saveFavorites(favReturn)
                } else {
                    favReturn.remove(team)
                    (activity as MainActivity).saveFavorites(favReturn)
                }
            }
            // If it was not initialized ever then I initialize it
            else{
                favReturn.add(team)
                (activity as MainActivity).saveFavorites(favReturn)
            }
            return favReturn
        }
    }

    private fun duplicated(team: Team, favTeams: ArrayList<Team>): Boolean {
        for (i in favTeams) {
            if (team == i) {
                return true
            }
        }
        return false
    }

}