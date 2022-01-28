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

        val favList = (activity as MainActivity).loadFavorite()

        adapter.submitList(favList)

        adapter.onItemClickListener = {team ->
            teamSelectListener.onFavTeamSelected(team)
        }

        adapter.onFavClickListener = {team ->
            Toast.makeText(requireContext(), "Borrar", Toast.LENGTH_SHORT).show()
        }

        return rootView
    }
}