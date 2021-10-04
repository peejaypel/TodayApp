package com.peejaypel.todayapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.LocalTime

class TodoAddActivity() : AppCompatActivity() {
    private lateinit var username:String
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
            username = bundle.getString("username").toString()
        }

        val btnAddConfirm = findViewById<FloatingActionButton>(R.id.btnAddConfirm)
        val inputTitle = findViewById<EditText>(R.id.inputTitle)
        val inputDescription = findViewById<EditText>(R.id.inputDescription)
        val inputTime = findViewById<TimePicker>(R.id.inputTime)
        val inputDate = findViewById<CalendarView>(R.id.inputDate)

        val tvDateTime = findViewById<TextView>(R.id.tvDateTime)



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
                    DBHelper(this).addTodo(DBHelper(this).getUserIdFromUsername(username), inputTitle.text.toString(), inputDescription.text.toString(), localDate.toString(), localTime.toString())
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