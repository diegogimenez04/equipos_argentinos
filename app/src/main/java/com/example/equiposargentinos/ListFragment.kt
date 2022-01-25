package com.example.equiposargentinos

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.equiposargentinos.api.WorkerUtils
import com.example.equiposargentinos.main.MainViewModel
import com.example.equiposargentinos.main.MainViewModelFactory
import java.lang.ClassCastException

class ListFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    interface TeamSelectListener {
        fun onTeamSelected(team: Team)
    }

    private lateinit var teamSelectListener: TeamSelectListener

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
        // Inflate the layout for this fragment
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

        adapter.onItemClickListener = {
            team ->  teamSelectListener.onTeamSelected(team)
        }

        viewModel.fbList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            //handleEmptyView(it, rootView)
        }

        return rootView
    }

    private fun doMySearch(query: String) {
        viewModel.reloadTeamsWithName(query)
    }
}