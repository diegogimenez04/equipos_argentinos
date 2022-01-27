package com.example.equiposargentinos

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.equiposargentinos.main.MainActivity
import com.example.equiposargentinos.main.MainViewModel
import com.example.equiposargentinos.main.MainViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

        return rootView
    }
}