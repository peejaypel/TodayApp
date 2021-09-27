package com.peejaypel.todayapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter (private val todos: List<Todo>): RecyclerView.Adapter<TodoAdapter.ViewHolder>()  {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val tvTitle = itemView.findViewById<TextView>(R.id.todoTitle)
        val tvDescription = itemView.findViewById<TextView>(R.id.todoDescription)
        val tvDateTarget = itemView.findViewById<TextView>(R.id.todoDateTarget)
        val tvTimeTarget = itemView.findViewById<TextView>(R.id.todoTimeTarget)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val todoView = inflater.inflate(R.layout.item_todo, parent, false)
        // Return a new holder instance
        return ViewHolder(todoView)
    }

    override fun onBindViewHolder(holder: TodoAdapter.ViewHolder, position: Int) {
        val todo: Todo = todos.get(position)
        val title = holder.tvTitle
        title.setText(todo.title)
        val description = holder.tvDescription
        description.setText(todo.description)
        val dateTarget = holder.tvDateTarget
        dateTarget.setText(todo.dateTarget)
        val timeTarget = holder.tvTimeTarget
        timeTarget.setText(todo.timeTarget)
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}