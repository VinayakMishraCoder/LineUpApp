package com.example.testdatabase

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "De_TodoListFragment"

class TodoListFragment : Fragment() ,RVListener, SearchView.OnQueryTextListener {

    lateinit var navcontroller: NavController
    lateinit var recyclerView: RecyclerView
    lateinit var allNotes : LiveData<List<Note>>
    lateinit var adapter: RVAdapter
    lateinit var db: NoteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO : (high order scale)
        (requireActivity() as MainActivity).supportActionBar!!.title = "LineUp"
        return inflater.inflate(R.layout.fragment_todo_list_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: in")
        super.onViewCreated(view, savedInstanceState)

        navcontroller = Navigation.findNavController(view)
        recyclerView = view.findViewById(R.id.recyclerview_todo)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        adapter = RVAdapter(activity as AppCompatActivity,this);

        recyclerView.adapter = adapter
        db = NoteDatabase.getDatabase(requireActivity())
        allNotes = db.noteDao().getNotes()
        allNotes.observe(activity as AppCompatActivity ,{
            adapter.updateList(it)
        })

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener{
            navcontroller.navigate(R.id.action_todo_list_fragment_to_input_todos)
        }
    }


    // search menu ---------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search,menu)

        val search = menu.findItem(R.id.app_bar_search)
        val searchView = search!!.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d(TAG, "onQueryTextChange: in")
        if(newText != null) {
            searchDataBase(newText)
        }
        return true
    }

    private fun searchDataBase(query: String) {
        val searchQuery = "%$query%"

        allNotes = db.noteDao().getNotes(searchQuery)
        allNotes.observe(activity as AppCompatActivity ,{
            adapter.updateList(it)
        })
    }
    // ----------------------------------------------------------------------------

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navcontroller) || super.onOptionsItemSelected(item)
    }

    override fun onEditClicked(note: Note) {
        val bundle = Bundle().apply {
            putSerializable("todo", note)
        }
        navcontroller.navigate(R.id.action_todo_list_fragment_to_input_todos, bundle)
    }

    override fun onDeleteClicked(note: Note) {
        GlobalScope.launch {
            db.noteDao().delete(note)
        }
    }

    override fun onLongClicked(note: Note) {
        val bundle = Bundle().apply {
            putSerializable("todo", note)
        }
        navcontroller.navigate(R.id.action_todo_list_fragment_to_detail2, bundle)
    }

    override fun timerStart(note: Note) {
        val timer = getTime(note.time)
        if(timer[0]!=-1) {
            val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
                putExtra(AlarmClock.EXTRA_LENGTH, timer[0] * 60 * 60 + timer[1] * 60 + timer[2])
                putExtra(AlarmClock.EXTRA_MESSAGE, note.description)
            }
            startActivity(intent)
        } else {
            Toast.makeText(activity as AppCompatActivity, "Please set valid time", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTime(timer: String): Array<Int> {
        val str = timer.split(":")
        if(str.size != 3) {
            return arrayOf(-1)
        }
        return arrayOf(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }
}


















//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment todo_list_fragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            todo_list_fragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
