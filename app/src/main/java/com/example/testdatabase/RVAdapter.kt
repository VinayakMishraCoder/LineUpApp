package com.example.testdatabase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RVAdapter(val context : Context, val listener : RVListener) : RecyclerView.Adapter<RVAdapter.NoteViewHolder>() {
    val allNotes  = ArrayList<Note>()

    inner class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title: TextView =itemView.findViewById<TextView>(R.id.title_todo)
        val description: TextView = itemView.findViewById<TextView>(R.id.description)
        val timer: TextView = itemView.findViewById(R.id.timer_text_view)

        fun bind(note: Note) {
            this.title.text = note.title
            this.description.text = note.description
            this.timer.text = note.time
            itemView.findViewById<ImageButton>(R.id.delete_btn).setOnClickListener{
                listener.onDeleteClicked(note)
            }
            itemView.findViewById<ImageButton>(R.id.editButton).setOnClickListener{
                listener.onEditClicked(note)
            }
            itemView.setOnLongClickListener {
                listener.onLongClicked(note)
                true
            }
            itemView.findViewById<ImageButton>(R.id.timer_btn).setOnClickListener{
                listener.timerStart(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder = NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_todos,parent,false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currNote = allNotes[position]
        holder.bind(currNote)
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun updateList(newList : List<Note>){
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }
}

interface RVListener {
    fun onEditClicked(note: Note)
    fun onDeleteClicked(note: Note)
    fun onLongClicked(note: Note)
    fun timerStart(note: Note)
}
