package com.example.equiposargentinos.main

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.equiposargentinos.*
import com.example.equiposargentinos.login.LoginActivity
import com.example.equiposargentinos.login.LoginViewModel

class MainActivity : AppCompatActivity(),
    ListFragment.TeamSelectListener, ListFragment.FavSelectListener,
    FavoritesFragment.TeamSelectListener{

    private lateinit var favList: MutableList<Team>
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onTeamSelected(team: Team) {
        findNavController(R.id.main_navigation_container)
            .navigate(ListFragmentDirections.actionListFragmentToDetailFragment(team))
    }

    override fun onFavSelected(viewModel: MainViewModel) {
        favList = viewModel.favTeams.value ?: favList
    }

    override fun onFavTeamSelected(team: Team) {
        findNavController(R.id.main_navigation_container)
            .navigate(FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(team))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.btn_logout){
            val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
            viewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))
        } else if (itemId == R.id.btn_fav) {
            if (::favList.isInitialized && favList.isNotEmpty()){
                findNavController(R.id.main_navigation_container)
                    .navigate(ListFragmentDirections.actionListFragmentToFavoritesFragment(favList.toTypedArray()))
            } else
                Toast.makeText(this, "No favorites teams", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}