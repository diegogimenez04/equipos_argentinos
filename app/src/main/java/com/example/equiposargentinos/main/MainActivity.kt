package com.example.equiposargentinos.main

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.equiposargentinos.ListFragment
import com.example.equiposargentinos.ListFragmentDirections
import com.example.equiposargentinos.R
import com.example.equiposargentinos.Team
import com.example.equiposargentinos.login.LoginActivity
import com.example.equiposargentinos.login.LoginViewModel

class MainActivity : AppCompatActivity(), ListFragment.TeamSelectListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onTeamSelected(team: Team) {
        findNavController(R.id.main_navigation_container)
            .navigate(ListFragmentDirections.actionListFragmentToDetailFragment(team))
    }

    /*
    fun clickBack(@Suppress("UNUSED_PARAMETER") view: View?) {
        findViewById<TextView>(R.id.toolbar_title).text = getString(R.string.equipos_argentinos)
        findViewById<ImageView>(R.id.iv_go_back).visibility = View.GONE
        onBackPressed()
    }
    */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        return super.onOptionsItemSelected(item)
    }
}