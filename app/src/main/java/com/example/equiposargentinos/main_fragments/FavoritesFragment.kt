package com.example.equiposargentinos.main_fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.equiposargentinos.FbAdapter
import com.example.equiposargentinos.R
import com.example.equiposargentinos.Team
import com.example.equiposargentinos.main.MainActivity
import com.example.equiposargentinos.main.MainViewModel
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

        val adapter = FbAdapter(requireContext())
        fbRecycler.adapter = adapter

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        val favList = viewModel.favTeams.value
        adapter.submitList(favList)

        adapter.onItemClickListener = { team ->
            teamSelectListener.onFavTeamSelected(team)
        }

        adapter.onFavClickListener = { team, btnFav ->
            viewModel.handleFavorite(team, btnFav)
        }

        return rootView
    }

}