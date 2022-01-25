package com.example.equiposargentinos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.equiposargentinos.main.MainViewModel
import java.lang.ClassCastException

class ListFragment : Fragment() {

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

        adapter.onItemClickListener = {
            team ->  teamSelectListener.onTeamSelected(team)
        }

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.fbList.observe(this) {
            adapter.submitList(it)
            //handleEmptyView(it, rootView)
        }

        return rootView
    }
}