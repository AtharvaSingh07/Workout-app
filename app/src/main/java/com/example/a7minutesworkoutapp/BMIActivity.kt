package com.example.a7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }
    private var currentVisibleView : String = METRIC_UNITS_VIEW
    private var binding : ActivityBmiBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBMIActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBMIActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeMetricUnitsViewVisible()
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId : Int->
            if(checkedId == R.id.rbMetricUnits){
                makeMetricUnitsViewVisible()
            }else{
                makeUSUnitsViewVisible()
            }
        }
        binding?.btnCalculateUnits?.setOnClickListener {
           calculateUnits()
        }
    }

    private fun makeMetricUnitsViewVisible(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilUSUnitWeight?.visibility = View.GONE
        binding?.tilMetricUnitUSHeightInch?.visibility = View.GONE
        binding?.tilMetricUnitUSHeightFeet?.visibility = View.GONE

        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeUSUnitsViewVisible(){
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilUSUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitUSHeightInch?.visibility = View.VISIBLE
        binding?.tilMetricUnitUSHeightFeet?.visibility = View.VISIBLE

        binding?.etUSUnitWeight?.text!!.clear()
        binding?.etUSMetricUnitHeightFeet?.text!!.clear()
        binding?.etUSMetricUnitHeightInch?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi : Float){
        val bmiLabel : String
        val bmiDescription : String
        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Extremely serious underweight problem"
            bmiDescription = "You need to take care of yourself. Eat more!!!"
        }else if(bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Serious underweight problem"
            bmiDescription = "You need to take care of yourself. Eat more!!!"
        }else if(bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight problem"
            bmiDescription = "You need to take care of yourself. Eat more!!!"
        }else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "BMI Normal"
            bmiDescription = "Congrats!!! You are in a good shape!!!"
        }else if(bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight problem"
            bmiDescription = "You need to take care of yourself. Workout!!!"
        }else if(bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese Class I (Moderately Obese)"
            bmiDescription = "You need to take care of yourself. Workout!!!"
        }else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese Class II (Seriously Obese)"
            bmiDescription = "You need to take care of yourself. Workout more!!!"
        }else {
            bmiLabel = "Obese Class III (Very Seriously Obese)"
            bmiDescription = "You need to take care of yourself. Only workout can save you!!!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription
    }
    private fun validateMetricUnits():Boolean{
        var isValid = true
        if(binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun calculateUnits(){
        if(currentVisibleView == METRIC_UNITS_VIEW) {
                if (validateMetricUnits()) {
                val heightValue: Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()
                val bmi = weightValue / (heightValue * heightValue)
                displayBMIResult(bmi)
            }
                else {
                Toast.makeText(this@BMIActivity, "Please enter valid values", Toast.LENGTH_LONG)
                    .show()
            }
        } else{
            if(validateUSUnits()) {
                val usUnitHeightValueFeet : String = binding?.etUSMetricUnitHeightFeet?.text.toString()
                val usUnitHeightValueInch : String = binding?.etUSMetricUnitHeightInch?.text.toString()
                val usUnitWeightValue : Float = binding?.etUSUnitWeight?.text.toString().toFloat()

                val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12
                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                displayBMIResult(bmi)
            }else {
                Toast.makeText(this@BMIActivity, "Please enter valid values", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

   private fun validateUSUnits() : Boolean{
       var isValid = true
       when{
           binding?.etUSUnitWeight?.text.toString().isEmpty() -> {
               isValid = false
           }
           binding?.etUSMetricUnitHeightFeet?.text.toString().isEmpty() -> {
               isValid = false
           }
           binding?.etUSMetricUnitHeightInch?.text.toString().isEmpty() -> {
               isValid = false
           }
       }
       return isValid
   }

}