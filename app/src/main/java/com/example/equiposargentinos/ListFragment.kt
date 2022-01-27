package com.example.equiposargentinos

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.equiposargentinos.api.WorkerUtils
import com.example.equiposargentinos.login.LoginActivity
import com.example.equiposargentinos.login.LoginViewModel
import com.example.equiposargentinos.main.MainActivity
import com.example.equiposargentinos.main.MainViewModel
import com.example.equiposargentinos.main.MainViewModelFactory
import com.google.firebase.database.DatabaseReference

class ListFragment : Fragment() {

    private lateinit var firebaseDatabase: DatabaseReference
    lateinit var viewModel: MainViewModel
    private lateinit var teamSelectListener: TeamSelectListener
    private lateinit var favSelectListener: FavSelectListener
    lateinit var favTeams: ArrayList<Team>

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
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        val fbRecycler = rootView.findViewById<RecyclerView>(R.id.fb_recycler)
        fbRecycler.layoutManager = LinearLayoutManager(requireContext())

        val adapter = FbAdapter()
        fbRecycler.adapter = adapter

        viewModel = ViewModelProvider(this,
            MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]

        if (Intent.ACTION_SEARCH == requireActivity().intent.action) {
            requireActivity().intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }
        }

        WorkerUtils.scheduleSynchronization(requireActivity())

        adapter.onItemClickListener = {team ->
            teamSelectListener.onTeamSelected(team)
        }

        adapter.onFavClickListener = { team ->
            handleFavorite(team)
            favSelectListener.onFavSelected(viewModel)
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.btn_logout){
            val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
            viewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        } else if (itemId == R.id.btn_fav) {
            (activity as MainActivity).onGoToFavoriteSelected()
        } else if (itemId == R.id.btn_search) {
            //doMySearch("Aldosi")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleFavorite(team: Team) {
        if(::favTeams.isInitialized){
            // If initialized I check that it is not duplicated
            if (!duplicated(team)){
                favTeams.add(team)
                (activity as MainActivity).saveFavorites(favTeams)
            }
        } else {
            // If its not initialized, I check a previous initialization and if it is duplicated
            if ((activity as MainActivity).loadFavorite() != null) {
                favTeams = (activity as MainActivity).loadFavorite()!!
                if (!duplicated(team)){
                    favTeams.add(team)
                    (activity as MainActivity).saveFavorites(favTeams)
                }
            }
            // If it was not initialized ever then I initialize it
            else{
                favTeams = arrayListOf()
                favTeams.add(team)
                (activity as MainActivity).saveFavorites(favTeams)
            }
        }
    }

    private fun duplicated(team: Team): Boolean {
        for (i in favTeams) {
            if (team == i) {
                return true
            }
        }
        return false
    }

    private fun handleEmptyView(it: MutableList<Team>, rootView: View) {
        if (it.isEmpty()) rootView.findViewById<ProgressBar>(R.id.pb_list).visibility = View.VISIBLE
        else rootView.findViewById<ProgressBar>(R.id.pb_list).visibility = View.GONE
    }

    private fun doMySearch(query: String) {
        viewModel.reloadTeamsWithName(query)
    }
}