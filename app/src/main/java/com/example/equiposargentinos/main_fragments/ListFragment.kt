package com.example.equiposargentinos.main_fragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Adapter
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.equiposargentinos.FbAdapter
import com.example.equiposargentinos.R
import com.example.equiposargentinos.Team
import com.example.equiposargentinos.api.ApiResponseStatus
import com.example.equiposargentinos.api.WorkerUtils
import com.example.equiposargentinos.login.LoginActivity
import com.example.equiposargentinos.login.LoginViewModel
import com.example.equiposargentinos.main.MainActivity
import com.example.equiposargentinos.main.MainViewModel
import com.example.equiposargentinos.main.MainViewModelFactory
import com.google.firebase.database.DatabaseReference

class ListFragment : Fragment() {

    private lateinit var searchList: MutableList<Team>
    lateinit var viewModel: MainViewModel
    private lateinit var teamSelectListener: TeamSelectListener
    private lateinit var favSelectListener: FavSelectListener
    lateinit var adapter: FbAdapter
    private lateinit var fbRecycler: RecyclerView

    interface TeamSelectListener {
        fun onTeamSelected(team: Team)
    }

    interface FavSelectListener {
        fun onFavSelected(viewModel: MainViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        teamSelectListener = try {
            context as TeamSelectListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement TeamSelectListener")
        }

        favSelectListener = try {
            context as FavSelectListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement FavSelectListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchList = mutableListOf()
        WorkerUtils.scheduleSynchronization(requireActivity())

        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        fbRecycler = rootView.findViewById(R.id.fb_recycler)
        fbRecycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = FbAdapter(requireContext())
        fbRecycler.adapter = adapter

        adapter.onItemClickListener = {team ->
            teamSelectListener.onTeamSelected(team)
        }

        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]

        adapter.onFavClickListener = { team, btnFav ->
            viewModel.handleFavorite(team, btnFav)
            favSelectListener.onFavSelected(viewModel)
        }

        viewModel.searchList.observe(requireActivity()) {
            searchList = it
            adapter.submitList(searchList)
        }

        viewModel.status.observe(requireActivity()) {
            val progressBar = rootView.findViewById<ProgressBar>(R.id.pb_list)
            if (it == ApiResponseStatus.DONE){
                progressBar.visibility = View.GONE
            } else if (it == ApiResponseStatus.LOADING) {
                progressBar.visibility = View.VISIBLE
            }
        }

        viewModel.fbList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            handleEmptyView(it, rootView)
        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
        handleSearch(menu)
    }

    private fun handleSearch(menu: Menu){
        val searchItem = menu.findItem(R.id.btn_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                doMySearch(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                doMySearch(query)
                return false
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.btn_logout){
            val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
            viewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        } else if (itemId == R.id.btn_fav) {
            (activity as MainActivity).onGoToFavoriteSelected()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleEmptyView(it: MutableList<Team>, rootView: View) {
        if (it.isEmpty()) rootView.findViewById<ProgressBar>(R.id.pb_list).visibility = View.VISIBLE
        else rootView.findViewById<ProgressBar>(R.id.pb_list).visibility = View.GONE
    }

    private fun doMySearch(query: String) {
        viewModel.reloadTeamsWithName(query)
    }
}