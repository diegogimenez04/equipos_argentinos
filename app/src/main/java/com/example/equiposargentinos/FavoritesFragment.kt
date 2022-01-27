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
import com.example.equiposargentinos.main.MainViewModel
import com.example.equiposargentinos.main.MainViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.ClassCastException

class FavoritesFragment : Fragment() {
    private val args: FavoritesFragmentArgs by navArgs()
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

        val favList = args.favList

        val fbRecycler = rootView.findViewById<RecyclerView>(R.id.fb_recycler)
        fbRecycler.layoutManager = LinearLayoutManager(requireContext())

        val adapter = FbAdapter()
        fbRecycler.adapter = adapter

        //adapter.submitList(favList.toMutableList())

        adapter.onItemClickListener = {team ->
            teamSelectListener.onFavTeamSelected(team)
        }

        /*val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.value
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })*/

        return rootView
    }

    override fun onPrepareOptionsMenu(menu: Menu){
        menu.getItem(1).isEnabled = false
    }
}