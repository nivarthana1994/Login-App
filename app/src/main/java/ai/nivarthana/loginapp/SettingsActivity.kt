package ai.nivarthana.loginapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ai.nivarthana.loginapp.model.Gender

class SettingsActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        nameEditText = findViewById(R.id.nameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        genderSpinner = findViewById(R.id.genderSpinner)
        saveButton = findViewById(R.id.saveButton)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val savedName = sharedPreferences.getString("name", "")
        val savedAge = sharedPreferences.getInt("age", 0)
        val savedGender = sharedPreferences.getString("gender", "")

        nameEditText.setText(savedName)
        ageEditText.setText(if (savedAge != 0) savedAge.toString() else "")

        // Create an ArrayAdapter with the Gender enum values
        val genderAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Gender.values()
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        // Set the selection based on the saved gender value
        savedGender?.let { setSpinnerSelection(it) }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull()
            val gender = genderSpinner.selectedItem as Gender

            if (validateFields(name, age, gender)) {
                saveData(name, age, gender)
                finish()
            }
        }
    }

    private fun validateFields(name: String, age: Int?, gender: Gender): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            nameEditText.error = "Please enter your name"
            isValid = false
        }

        if (age == null || age <= 0) {
            ageEditText.error = "Please enter a valid age"
            isValid = false
        }

        if (gender == Gender.GENDER) {
            val errorText = genderSpinner.selectedView as TextView
            errorText.error = "Please select a valid gender"
            isValid = false
        }

        if (!isValid) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }

    private fun saveData(name: String, age: Int?, gender: Gender) {
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putInt("age", age ?: 0)
        editor.putString("gender", gender.name)
        editor.apply()
        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
    }

    private fun setSpinnerSelection(gender: String) {
        val adapter = genderSpinner.adapter as ArrayAdapter<*>
        val count = adapter.count
        for (i in 0 until count) {
            if (adapter.getItem(i).toString() == gender) {
                genderSpinner.setSelection(i)
                break
            }
        }
    }
}
