package com.example.slewrate

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import java.lang.Math.*

class MainActivity : AppCompatActivity() {
    private val frequencyList = listOf("KHz","Hz")
    private var frequencySelected: String = ""
    private val voltageList = listOf("V", "mV")
    private var voltageSelected: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val etFrequency = findViewById<EditText>(R.id.etFrequency)
        val etVoltage = findViewById<EditText>(R.id.etVoltage)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val btnClean = findViewById<Button>(R.id.btnClean)
        val textResult = findViewById<TextView>(R.id.textResult)
        val frequencySpinner = findViewById<Spinner>(R.id.frequencySpinner)
        val voltageSpinner = findViewById<Spinner>(R.id.voltageSpinner)

        // Create an ArrayAdapter using the string array and a custom spinner layout
        val adapter = ArrayAdapter(this, R.layout.frequency_spinner, frequencyList)
        frequencySpinner.adapter = adapter

        val adapter2 = ArrayAdapter(this, R.layout.frequency_spinner, voltageList)
        voltageSpinner.adapter = adapter2

        frequencySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                frequencySelected = frequencySpinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        voltageSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                voltageSelected = voltageSpinner.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        btnCalculate.setOnClickListener {
            if (etFrequency.text.isEmpty() && etVoltage.text.isEmpty()) {
                Toast.makeText(this, "Enter values", Toast.LENGTH_SHORT).show()
            } else if (etFrequency.text.isEmpty()) {
                Toast.makeText(this, "Enter frequency", Toast.LENGTH_SHORT).show()
            } else if (etVoltage.text.isEmpty()) {
                Toast.makeText(this, "Enter voltage", Toast.LENGTH_SHORT).show()
            } else {
                if (frequencySelected.equals("KHz")) {
                    textResult.text = "Slew rate: ${getSlewRate(
                        etFrequency.text.toString().toDouble() * 1000, 
                        etVoltage.text.toString().toDouble()
                    )} V/μs"
                } else if (frequencySelected.equals("Hz")) {
                    textResult.text = "Slew rate: ${getSlewRate(
                        etFrequency.text.toString().toDouble(),
                        etVoltage.text.toString().toDouble()
                    )} V/μs"
                }
            }
        }

        btnClean.setOnClickListener {
            if (etFrequency.text.isEmpty() && etVoltage.text.isEmpty()) {
                Toast.makeText(this,"Nothing to clear",Toast.LENGTH_SHORT).show()
            } else {
                etFrequency.setText("")
                etVoltage.setText("")
                textResult.text = "Result"
            }
        }
    }

    private fun customRound(number: Double, decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(number * multiplier) / multiplier
    }

    private fun getSlewRate(frequency:Double, peakVoltage:Double): Double {
        /* slew rate = 2 * pi * f * Vp * 1 us
         * f = frequency (Hertz)
         * Vp = peak voltage (Volts)
         * slew rate (V/us)
        */
        return customRound(2 * PI * frequency * peakVoltage * 0.000001, 4)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settingsItem -> {

                true
            }
            R.id.aboutItem -> {
                Toast.makeText(this,"About not avaible",Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}