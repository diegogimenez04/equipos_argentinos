package com.example.equiposargentinos

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.equiposargentinos.api.WorkerUtils
import com.example.equiposargentinos.login.LoginActivity
import com.example.equiposargentinos.login.LoginViewModel
import com.example.equiposargentinos.main.MainActivity
import com.example.equiposargentinos.main.MainViewModel
import com.example.equiposargentinos.main.MainViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.ClassCastException

class ListFragment : Fragment() {

    private lateinit var firebaseDatabase: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var user: User
    lateinit var viewModel: MainViewModel
    private lateinit var teamSelectListener: TeamSelectListener
    private lateinit var favSelectListener: FavSelectListener

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

        firebaseDatabase = Firebase.database.reference
        auth = Firebase.auth
        currentUser = auth.currentUser!!

        val fbRecycler = rootView.findViewById<RecyclerView>(R.id.fb_recycler)
        fbRecycler.layoutManager = LinearLayoutManager(requireContext())

        val adapter = FbAdapter()
        fbRecycler.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]

        viewModel.user.observe(requireActivity()) {
            user = it
        }


        /*if (Intent.ACTION_SEARCH == requireActivity().intent.action) {
            requireActivity().intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }
        }*/

        WorkerUtils.scheduleSynchronization(requireActivity())

        adapter.onItemClickListener = { team ->
            teamSelectListener.onTeamSelected(team)
        }

        adapter.onFavClickListener = { team ->
            viewModel.handleFavorite(team)
            favSelectListener.onFavSelected(viewModel)
        }

        viewModel.fbList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            handleEmptyView(it, rootView)
        }

        return rootView
    }

    private fun addFavoriteToDB(team: Team, user: FirebaseUser) {
        firebaseDatabase.child("users").child(user.uid)
            .child("teams").setValue(team)
    }

    private fun handleEmptyView(it: MutableList<Team>, rootView: View) {
        if (it.isEmpty()) rootView.findViewById<ProgressBar>(R.id.pb_list).visibility = View.VISIBLE
        else rootView.findViewById<ProgressBar>(R.id.pb_list).visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
        Log.d("ListFragment", "Options menu created")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.btn_logout) {
            val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
            viewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        } else if (itemId == R.id.btn_fav) {
            if (user.favoritesTeams != null) {
                (activity as MainActivity).onFavItemSelected(user)
            } else {
                Toast.makeText(requireContext(), "No favorites teams", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun doMySearch(query: String) {
        viewModel.reloadTeamsWithName(query)
    }
}