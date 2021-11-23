package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.modals.NotesModal
import kotlinx.android.synthetic.main.activity_add_edit_note_acitivity.*
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteActivity : AppCompatActivity() {

    lateinit var viewModal: NoteViewModal
    var noteId = -1

    lateinit var myCalendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    var finalDate = 0L
    var finalTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note_acitivity)

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)

        ETDate.setOnClickListener{
            setListener()
        }

        ETTime.setOnClickListener {
            setTimeListener()
        }


        val noteType = intent.getStringExtra("type")
        if (noteType.equals("Edit")) {
            btnSave.text = "Update Todo"
            ETTitle.setText(intent.getStringExtra("title"))
            ETDescription.setText(intent.getStringExtra("description"))
            ETDate.setText(intent.getStringExtra("date"))
            ETTime.setText(intent.getStringExtra("time"))
            ETCategory.setText(intent.getStringExtra("category"))
            noteId = intent.getIntExtra("noteId", -1)
        } else {
            btnSave.text = "Create Todo"
        }

        btnSave.setOnClickListener {
            val noteTitle = ETTitle.text.toString()
            val noteDescription = ETDescription.text.toString()
            val noteDate = ETDate.text.toString()
            val noteTime = ETTime.text.toString()
            val noteCategory = ETCategory.text.toString()
            if (noteType.equals("Edit")) {
                val updateNote = NotesModal(
                    noteTitle,
                    noteDescription,
                    noteDate,
                    noteTime,
                    noteCategory
                )
                updateNote.id = noteId
                viewModal.updateNote(updateNote)
                Toast.makeText(this, "$noteTitle Updated", Toast.LENGTH_SHORT).show()
            } else {
                viewModal.addNote(
                    NotesModal(
                        noteTitle,
                        noteDescription,
                        noteDate,
                        noteTime,
                        noteCategory
                    )
                )
                Toast.makeText(this, "$noteTitle successfully added to the list", Toast.LENGTH_LONG)
                    .show()
            }

            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }

    }

    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()

        timeSetListener =
            TimePickerDialog.OnTimeSetListener() { _: TimePicker, hourOfDay: Int, min: Int ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, min)
                updateTime()
            }

        val timePickerDialog = TimePickerDialog(
            this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()
    }

    private fun updateTime() {
        //Mon, 5 Jan 2020
        val myformat = "h:mm a"
        val sdf = SimpleDateFormat(myformat)
        finalTime = myCalendar.time.time
        ETTime.setText(sdf.format(myCalendar.time))

    }

    private fun setListener() {
        myCalendar = Calendar.getInstance()

        dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()

            }

        val datePickerDialog = DatePickerDialog(
            this, dateSetListener, myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        //Mon, 5 Jan 2020
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        finalDate = myCalendar.time.time
        ETDate.setText(sdf.format(myCalendar.time))
    }

    private val timeInterval = 2000
    private var backPressed: Long = 0

    override fun onBackPressed() {
        if (backPressed + timeInterval > System.currentTimeMillis()) {
            startActivity(Intent(this, MainActivity::class.java))
            return
        } else {
            Toast.makeText(this, "Press again to go Back", Toast.LENGTH_SHORT).show()
            backPressed = System.currentTimeMillis()
        }
    }

}