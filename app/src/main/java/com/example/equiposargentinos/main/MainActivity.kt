package com.example.equiposargentinos.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.equiposargentinos.*
import com.example.equiposargentinos.main_fragments.FavoritesFragment
import com.example.equiposargentinos.main_fragments.FavoritesFragmentDirections
import com.example.equiposargentinos.main_fragments.ListFragment
import com.example.equiposargentinos.main_fragments.ListFragmentDirections
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SHARED_PREFS = "sharedPrefs"
const val PREF_LIST = "prefList"

class MainActivity : AppCompatActivity(),
    ListFragment.TeamSelectListener, ListFragment.FavSelectListener,
    FavoritesFragment.TeamSelectListener{

    lateinit var viewModel: MainViewModel
    private lateinit var prefTeam: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.favTeams.observe(this) {
            saveFavorites(it)
        }
    }

    override fun onTeamSelected(team: Team) {
        findNavController(R.id.main_navigation_container)
            .navigate(ListFragmentDirections.actionListFragmentToDetailFragment(team))
    }

    override fun onFavSelected(viewModel: MainViewModel) {
    }

    fun onGoToFavoriteSelected() {
        findNavController(R.id.main_navigation_container)
            .navigate(ListFragmentDirections.actionListFragmentToFavoritesFragment())
    }

    override fun onFavTeamSelected(team: Team) {
        findNavController(R.id.main_navigation_container)
            .navigate(FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(team))
    }

    private fun saveFavorites(teams: MutableList<Team>) {
        val gson = Gson().toJson(teams)
        val sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(PREF_LIST, gson)
        editor.apply()
    }

    fun loadFavorite(): ArrayList<Team>? {
        val sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        prefTeam = sharedPref.getString(PREF_LIST, "") ?: ""
        val listType = object:TypeToken<ArrayList<Team>>(){}.type
        return Gson().fromJson(prefTeam, listType)
    }

    fun wipeFavorites() {
        val sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}