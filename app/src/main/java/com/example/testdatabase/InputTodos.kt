package com.example.testdatabase

import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Serializable
import kotlin.concurrent.thread

@DelicateCoroutinesApi
class InputTodos : Fragment() {

    private lateinit var database: NoteDatabase
    lateinit var inputdescription : EditText
    lateinit var inputtitle : EditText
    lateinit var numberpicker: NumberPicker
    lateinit var timer: EditText
    private var note: Note? = null
    lateinit var db: NoteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        note = arguments?.getSerializable("todo") as Note?
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar!!.title = "Input"
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_input_todos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        database = NoteDatabase.getDatabase(activity as AppCompatActivity)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navcontroller = Navigation.findNavController(view)
        db = NoteDatabase.getDatabase(requireActivity())
        inputdescription = view.findViewById(R.id.get_description)
        inputtitle = view.findViewById(R.id.get_title)
        numberpicker = view.findViewById(R.id.number_picker)
        timer = view.findViewById(R.id.get_time)
        numberpicker.maxValue = 100
        numberpicker.minValue = 1
        val note = note
        if(note != null) {
            inputdescription.setText(note.description)
            inputtitle.setText(note.title)
            numberpicker.value = note.priority
            timer.setText(note.time)
        }

        view.findViewById<ImageButton>(R.id.save_btn).setOnClickListener {
            if(note == null) {
                GlobalScope.launch {
                    val description = inputdescription.text.toString()
                    val title = inputtitle.text.toString()
                    val priority = numberpicker.value
                    val timer = timer.text.toString()
                    database.noteDao().insert(Note(title,description,priority,timer))
                }
            } else {
                GlobalScope.launch {
                    note.description = inputdescription.text.toString()
                    note.title = inputtitle.text.toString()
                    note.priority = numberpicker.value
                    note.time = timer.text.toString()
                    database.noteDao().update(note)
                }
            }
            navcontroller.navigate(R.id.action_input_todos_to_todo_list_fragment)
        }
    }
}