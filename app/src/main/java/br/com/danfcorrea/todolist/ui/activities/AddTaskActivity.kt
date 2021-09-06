package br.com.danfcorrea.todolist.ui.activities

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.danfcorrea.todolist.databinding.ActivityAddTaskBinding
import br.com.danfcorrea.todolist.datasource.TaskDataSource
import br.com.danfcorrea.todolist.extensions.format
import br.com.danfcorrea.todolist.extensions.text
import br.com.danfcorrea.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.titulo
                binding.tilDate.text = it.data
                binding.tilHour.text = it.hora
            }
        }
        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
            datePicker.addOnPositiveButtonClickListener {
                binding.tilDate.text = Date(it).format()
            }
        }

        binding.tilHour.editText?.setOnClickListener {
            val hourPicker =
                MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            hourPicker.show(supportFragmentManager, "HOUR_PICKER_TAG")
            hourPicker.addOnPositiveButtonClickListener {
                val minute =
                    if (hourPicker.minute in 0..9) "0${hourPicker.minute}" else hourPicker.minute
                val hour = if (hourPicker.hour in 0..9) "0${hourPicker.hour}" else hourPicker.hour
                binding.tilHour.text = "$hour:$minute"
            }
        }

        binding.btnNewTask.setOnClickListener {
            val task = Task(
                titulo = binding.tilTitle.text,
                data = binding.tilDate.text,
                hora = binding.tilHour.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}