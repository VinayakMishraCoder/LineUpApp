package com.example.testdatabase

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation


class detail : Fragment() {
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getSerializable("todo") as Note?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar!!.title = "Back"
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val title = note?.title
        val description = note?.description
        if(note != null) {
            view.findViewById<TextView>(R.id.detail_title).text = title
            view.findViewById<TextView>(R.id.detail_description).text = description
        }

        view.findViewById<ImageButton>(R.id.detail_button).setOnClickListener {
            val intent = Intent(AlarmClock.ACTION_SHOW_TIMERS)
            startActivity(intent)
        }
    }
}