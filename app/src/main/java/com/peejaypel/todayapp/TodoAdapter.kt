package com.peejaypel.todayapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity


class TodoAdapter(private val todos: MutableList<Todo>) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val tvTitle = itemView.findViewById<TextView>(R.id.todoTitle)
        val tvDescription = itemView.findViewById<TextView>(R.id.todoDescription)
        val tvDateTarget = itemView.findViewById<TextView>(R.id.todoDateTarget)
        val tvTimeTarget = itemView.findViewById<TextView>(R.id.todoTimeTarget)
        val cbWillMessage = itemView.findViewById<CheckBox>(R.id.cbWillMessage)
        val btnDeleteTodo = itemView.findViewById<ImageButton>(R.id.btnDeleteTodo)
        val panelTodoMain = itemView.findViewById<ConstraintLayout>(R.id.panelTodoMain)
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

        val cbWillMessage = holder.cbWillMessage
        cbWillMessage.setOnCheckedChangeListener(null)
        cbWillMessage.isChecked = todo.isMessageOn == 1
        cbWillMessage.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            run {
                isChecked != isChecked
                updateIsMessageOn(todo, isChecked, holder.itemView.context)
            }
        }
        )

        val btnDeleteTodo = holder.btnDeleteTodo
        btnDeleteTodo.setOnClickListener(View.OnClickListener {
            removeTodo(
                holder,
                position,
                holder.itemView.context
            )
        })

        val panelTodoMain = holder.panelTodoMain
        panelTodoMain.setOnClickListener { updateTodo(holder, position, holder.itemView.context) }
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    private fun updateIsMessageOn(todo: Todo, isChecked: Boolean, context: Context) {
        todo.isMessageOn = if (isChecked) 1 else 0
        todo.title = "Yes"
        DBHelper(context).updateTodo(todo)
    }

    private fun updateTodo(holder: TodoAdapter.ViewHolder, position: Int, context: Context) {
        println("updateTodo(): Attempting to update todo...")
        val intent = Intent(context, TodoAddActivity::class.java)
        intent.putExtra("id", todos[position].id)
        intent.putExtra("userId", todos[position].ownerId)
        intent.putExtra("title", todos[position].title)
        intent.putExtra("timeTarget", todos[position].timeTarget)
        intent.putExtra("dateTarget", todos[position].dateTarget)
        intent.putExtra("description", todos[position].description)
        startActivity(context, intent, null)
    }

    private fun removeTodo(holder: TodoAdapter.ViewHolder, position: Int, context: Context) {
        println("removeItem(): Attempting to delete item...")
        val todo: Todo = todos[position]
        DBHelper(context).deleteTodo(todo)
        todos.removeAt(position)
        notifyItemRemoved(position)
    }
}