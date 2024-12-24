package com.example.studentmanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "student_manager.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_STUDENTS = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_STUDENT_ID = "student_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_STUDENTS ( " +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_STUDENT_ID INTEGER )")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    fun addStudent(student: StudentModel): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.studentName)
            put(COLUMN_STUDENT_ID, student.studentId)
        }
        return writableDatabase.insert(TABLE_STUDENTS, null, values)
    }

    fun getAllStudents(): List<StudentModel> {
        val students = mutableListOf<StudentModel>()
        val db = readableDatabase
        val cursor = db.query(TABLE_STUDENTS, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val studentId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID))
            students.add(StudentModel(name, studentId))
        }
        cursor.close()
        return students
    }

    fun updateStudent(student: StudentModel, id: Int): Int {
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.studentName)
            put(COLUMN_STUDENT_ID, student.studentId)
        }
        return writableDatabase.update(TABLE_STUDENTS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteStudent(studentId: Int): Int {
        return writableDatabase.delete(TABLE_STUDENTS, "$COLUMN_STUDENT_ID = ?", arrayOf(studentId.toString()))
    }
}