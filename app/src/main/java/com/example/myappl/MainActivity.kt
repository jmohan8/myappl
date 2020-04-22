package com.example.myappl

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class Name {
    var id: Int = 0
    var userName: String? = null
    constructor(id: Int, userName: String) {
        this.id = id
        this.userName = userName
    }
    constructor(userName: String) {
        this.userName = userName
    }
}

class MindOrksDBOpenHelper(context: Context,
                           factory: SQLiteDatabase.CursorFactory?) :
        SQLiteOpenHelper(context, DATABASE_NAME,
                factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME
                + " TEXT" + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    fun addName(name: Name) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name.userName)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getAllName(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "mindorksName.db"
        val TABLE_NAME = "name"
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "username"
    }
}
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAddToDb.setOnClickListener {
            val dbHandler = MindOrksDBOpenHelper(this, null)
            val user = Name(etName.text.toString())
            dbHandler.addName(user)
            Toast.makeText(this, etName.text.toString() + " Added to database", Toast.LENGTH_LONG).show()
        }
        btnShowDatafromDb.setOnClickListener {
            tvDisplayName.text = ""
            val dbHandler = MindOrksDBOpenHelper(this, null)
            val cursor = dbHandler.getAllName()
            cursor!!.moveToFirst()
            tvDisplayName.append((cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_NAME))))
            while (cursor.moveToNext()) {
                tvDisplayName.append((cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_NAME))))
                tvDisplayName.append("\n")

            }
            cursor.close()

        }
    }
}
