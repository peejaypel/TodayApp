package com.peejaypel.todayapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class TodoAddActivity() : AppCompatActivity() {
    private var userId = 0
    @SuppressLint("NewApi")

    var localTime: LocalTime = LocalTime.now()
    @SuppressLint("NewApi")
    var localDate: LocalDate = LocalDate.now()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_add)

        val bundle = intent.extras
        if (bundle != null) {
            userId = bundle.getInt("userId")
        }

        val btnAddConfirm = findViewById<FloatingActionButton>(R.id.btnAddConfirm)
        val inputTitle = findViewById<EditText>(R.id.inputTitle)
        val inputDescription = findViewById<EditText>(R.id.inputDescription)
        val inputTime = findViewById<TimePicker>(R.id.inputTime)
        val inputDate = findViewById<CalendarView>(R.id.inputDate)

        val tvDateTime = findViewById<TextView>(R.id.tvDateTime)

        if (bundle!!.containsKey("title")) inputTitle.setText(bundle.getString("title"))
        if (bundle!!.containsKey("timeTarget")) {
            var localTime = bundle.getString("timeTarget")
            inputTime.hour = LocalTime.parse(localTime).hour
            inputTime.minute = LocalTime.parse(localTime).minute
        }
        if (bundle!!.containsKey("dateTarget")) {
            var localDate = bundle.getString("dateTarget")
            var calendar =  Calendar.getInstance()
            calendar.set(Calendar.YEAR, LocalDate.parse(localDate).year)
            calendar.set(Calendar.MONTH, LocalDate.parse(localDate).monthValue)
            calendar.set(Calendar.DAY_OF_MONTH, LocalDate.parse(localDate).dayOfMonth)

            inputDate.date = calendar.timeInMillis
        }
        if (bundle!!.containsKey("description")) inputDescription.setText(bundle.getString("description"))



        inputTime.setOnTimeChangedListener { _, hour, minute ->
            var hour = hour
            var am_pm = " "

            when {
                hour == 0 -> {
                    hour += 12
                    am_pm = "AM"
                }
                hour == 12 -> am_pm = "PM"
                hour > 12 -> {
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
            if (tvDateTime != null) {
                val hour:String = if (hour < 10) "0" + hour else hour.toString()
                val min:String = if (minute < 10) "0" + minute else minute.toString()
                // display format of time
                val msg = "Time is: $hour : $min $am_pm"
                tvDateTime.text = msg
                localTime = LocalTime.of(hour.toInt(), min.toInt())
            }
        }

        inputDate.setOnDateChangeListener { _, year, month, date ->
            localDate = LocalDate.of(year, month, date)
        }


        btnAddConfirm.setOnClickListener {
            if (inputTitle.text.toString() != "") {
                if (inputDescription.text.toString() != ""){
                    if (bundle!!.containsKey("id")){
                        var todo = Todo()
                        todo.id = bundle.getInt("id")
                        todo.ownerId = userId
                        todo.title = inputTitle.text.toString()
                        todo.description = inputDescription.text.toString()
                        todo.timeTarget = localTime.toString()
                        todo.dateTarget = localDate.toString()
                        DBHelper(this).updateTodo(todo)
                    } else {
                        DBHelper(this).addTodo(userId, inputTitle.text.toString(), inputDescription.text.toString(), localDate.toString(), localTime.toString())
                    }
                    finish()
                } else {
                    Toast.makeText(this, "Please set a description.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please set a title.", Toast.LENGTH_LONG).show()
            }
        }
    }
}