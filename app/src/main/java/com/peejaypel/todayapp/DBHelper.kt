package com.peejaypel.todayapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// here we have defined variables for our database

// below is variable for database name
private val DATABASE_NAME = "TODAYAPPDB"

// below is the variable for database version
private val DATABASE_VERSION = 1


val TABLE_USERS = "users"
val COL_USERS_ID = "userID"
val COL_USERS_USERNAME = "username"
val COL_USERS_PASSWORD = "password"
val COL_USERS_PHONE_NUMBER = "phoneNumber"

val TABLE_TODOS = "todos"
val COL_TODOS_ID = "todoID"
val COL_TODOS_TITLE = "title"
val COL_TODOS_DESCRIPTION = "description"
val COL_TODOS_DATE_TARGET = "dateTarget"
val COL_TODOS_TIME_TARGET = "timeTarget"
val COL_TODOS_OWNERID = "ownerID"
val COL_TODOS_ISMESSAGEON = "isMessageOn"
val COL_TODOS_ISMESSAGEON_DEFAULT = false

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        //Initiating Users Table
        val query = ("CREATE TABLE " +
                TABLE_USERS + " (" +
                COL_USERS_ID + " INTEGER PRIMARY KEY, " +
                COL_USERS_USERNAME + " TEXT NOT NULL, " +
                COL_USERS_PASSWORD + " TEXT NOT NULL, " +
                COL_USERS_PHONE_NUMBER + " TEXT NOT NULL" + ")")
        db.execSQL(query)

        //Initiating Todos Table
        val query2 =
            "CREATE TABLE $TABLE_TODOS (" +
                    "$COL_TODOS_ID INTEGER PRIMARY KEY, " +
                    "$COL_TODOS_TITLE TEXT NOT NULL," +
                    "$COL_TODOS_DESCRIPTION TEXT NOT NULL, " +
                    "$COL_TODOS_TIME_TARGET TEXT NOT NULL, " +
                    "$COL_TODOS_DATE_TARGET TEXT NOT NULL, " +
                    "$COL_TODOS_OWNERID INTEGER NOT NULL, " +
                    "$COL_TODOS_ISMESSAGEON INTEGER NOT NULL, " +
                    "FOREIGN KEY ($COL_TODOS_OWNERID) REFERENCES $TABLE_USERS($COL_USERS_ID))"
        db.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TODOS")
        onCreate(db)
    }

    public fun isRegistered(username: String): Boolean {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_USERS_USERNAME = '$username'"
        val result = db.rawQuery(query, null)

        if (result.count <= 0) return false

        return true
    }

    @SuppressLint("Range")
    fun isValidCredentials(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_USERS_USERNAME = '$username'"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        if (result.getString(result.getColumnIndex(COL_USERS_PASSWORD))
                .toString() == password
        ) return true
        return false
    }

    fun register(username: String, password: String, phoneNumber: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_USERS_USERNAME, username)
        contentValues.put(COL_USERS_PASSWORD, password)
        contentValues.put(COL_USERS_PHONE_NUMBER, phoneNumber)
        db.insert(TABLE_USERS, null, contentValues)
    }

    @SuppressLint("Range")
    fun getUserTable(): MutableList<User> {
        val list: MutableList<User> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val user = User()
                user.userID = result.getString(result.getColumnIndex(COL_USERS_ID)).toInt()
                user.username =
                    result.getString(result.getColumnIndex(COL_USERS_USERNAME)).toString()
                user.password =
                    result.getString(result.getColumnIndex(COL_USERS_PASSWORD)).toString()
                user.phoneNumber = result.getString(result.getColumnIndex(COL_USERS_PHONE_NUMBER))
                list.add(user)
            } while (result.moveToNext())
        }
        return list
    }

    fun dropUsersTable() {
        val db = this.readableDatabase
        val query = "DROP TABLE $TABLE_USERS"
        db.execSQL(query)
    }

    @SuppressLint("Range")
    fun getTodosByUserName(username: String): ArrayList<Todo> {
        val list: MutableList<User> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_USERS_USERNAME = '$username'"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        return getTodosByUserId(result.getInt(result.getColumnIndex(COL_USERS_ID)).toString())
    }

    @SuppressLint("Range")
    fun getTodosByUserId(userId: String): ArrayList<Todo> {
        val list = ArrayList<Todo>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_TODOS WHERE $COL_TODOS_OWNERID = '$userId'"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val todo = Todo()
                todo.id = result.getInt(result.getColumnIndex(COL_TODOS_ID))
                todo.title = result.getString(result.getColumnIndex(COL_TODOS_TITLE)).toString()
                todo.description =
                    result.getString(result.getColumnIndex(COL_TODOS_DESCRIPTION)).toString()
                todo.dateTarget =
                    result.getString(result.getColumnIndex(COL_TODOS_DATE_TARGET)).toString()
                todo.timeTarget =
                    result.getString(result.getColumnIndex(COL_TODOS_TIME_TARGET)).toString()
                todo.isMessageOn = result.getInt(result.getColumnIndex(COL_TODOS_ISMESSAGEON))
                println("GETTING TODO ${todo.id} WITH MESSAGE ON: ${todo.isMessageOn}")
                list.add(todo)
            } while (result.moveToNext())
        }
        db.close()
        return list
    }

    @SuppressLint("Range")
    fun getPhoneNumberByUserName(username: String): String{
        val list: MutableList<User> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_USERS_USERNAME = '$username'"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        return result.getString(result.getColumnIndex(COL_USERS_PHONE_NUMBER))
    }

    @SuppressLint("NewApi")
    fun addTodo(
        userId: Int,
        title: String,
        description: String,
        dateTarget: String,
        timeTarget: String
    ) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_TODOS_OWNERID, userId)
        contentValues.put(COL_TODOS_TITLE, title)
        contentValues.put(COL_TODOS_DESCRIPTION, description)
        contentValues.put(COL_TODOS_DATE_TARGET, dateTarget)
        contentValues.put(COL_TODOS_TIME_TARGET, timeTarget)
        contentValues.put(COL_TODOS_ISMESSAGEON, COL_TODOS_ISMESSAGEON_DEFAULT)
        db.insert(TABLE_TODOS, null, contentValues)
        println("ADD TODO HAS BEEN CALLED")
        db.close()
    }

    fun updateTodo(todo: Todo) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_TODOS_TITLE, todo.title)
        contentValues.put(COL_TODOS_DESCRIPTION, todo.description)
        contentValues.put(COL_TODOS_DATE_TARGET, todo.dateTarget)
        contentValues.put(COL_TODOS_TIME_TARGET, todo.timeTarget)
        contentValues.put(COL_TODOS_ISMESSAGEON, todo.isMessageOn)
        db.update(TABLE_TODOS, contentValues, "$COL_TODOS_ID = ?", arrayOf(todo.id.toString()))
        println("updated todo. ${todo.title} is now message: ${todo.isMessageOn}")
        db.close()
    }

    fun deleteTodo(todo: Todo){
        val db = this.writableDatabase
        db.delete(TABLE_TODOS, "$COL_TODOS_ID = ?", arrayOf(todo.id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun getUserIdFromUsername(username: String): Int {
        val list = ArrayList<Todo>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_USERS_USERNAME = '$username'"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        return (result.getInt(result.getColumnIndex(COL_USERS_ID)))
    }


    //------------------------------------------------------------------------
}

class User() {
    var userID: Int = 0
    var username: String = ""
    var password: String = ""
    var phoneNumber: String = ""
}
