package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var formula: TextView
    private var currentExpression = StringBuilder()
    private var lastResult: Double? = null
    private var lastOperator: Char? = null
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        formula = findViewById(R.id.formula)
        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onNumberClick((it as Button).text.toString()) }
        }

        val operators = mapOf(
            R.id.btnPlus to '+',
            R.id.btnMinus to '-',
            R.id.btnMultiply to '*',
            R.id.btnDivide to '/'
        )

        operators.forEach { (id, op) ->
            findViewById<Button>(id).setOnClickListener { onOperatorClick(op) }
        }

        findViewById<Button>(R.id.btnPoint).setOnClickListener { onDecimalClick() }
        findViewById<Button>(R.id.btnNegate).setOnClickListener { onNegateClick() }
        findViewById<Button>(R.id.btnCompute).setOnClickListener { onCalculate() }
        findViewById<Button>(R.id.btnCE).setOnClickListener { onClearEntry() }
        findViewById<Button>(R.id.btnE).setOnClickListener { onClear() }
        findViewById<Button>(R.id.btnBS).setOnClickListener { onBackspace() }
    }

    private fun onNumberClick(number: String) {
        if (isNewOperation) {
            currentExpression.clear()
            isNewOperation = false
        }
        currentExpression.append(number)
        updateDisplay()
    }

    private fun onOperatorClick(operator: Char) {
        if (currentExpression.isNotEmpty()) {
            lastResult = evaluateExpression()
            lastOperator = operator
            currentExpression.clear()
            updateDisplay(lastResult.toString() + " " + operator)
        }
    }

    private fun onDecimalClick() {
        if (!currentExpression.contains(".")) {
            if (currentExpression.isEmpty()) {
                currentExpression.append("0")
            }
            currentExpression.append(".")
            updateDisplay()
        }
    }

    private fun onNegateClick() {
        if (currentExpression.isNotEmpty() && currentExpression.toString() != "0") {
            if (currentExpression.startsWith("-")) {
                currentExpression.deleteCharAt(0)
            } else {
                currentExpression.insert(0, "-")
            }
            updateDisplay()
        }
    }

    private fun onCalculate() {
        if (lastOperator != null && currentExpression.isNotEmpty()) {
            lastResult = evaluateExpression()
            lastOperator = null
            currentExpression.clear()
            currentExpression.append(lastResult.toString())
            updateDisplay()
        }
    }

    private fun onClearEntry() {
        currentExpression.clear()
        updateDisplay("0")
    }

    private fun onClear() {
        currentExpression.clear()
        lastResult = null
        lastOperator = null
        updateDisplay("0")
    }

    private fun onBackspace() {
        if (currentExpression.isNotEmpty()) {
            currentExpression.deleteCharAt(currentExpression.length - 1)
            updateDisplay()
        }
    }

    private fun evaluateExpression(): Double {
        val num1 = lastResult ?: 0.0
        val num2 = currentExpression.toString().toDoubleOrNull() ?: return num1
        return when (lastOperator) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '*' -> num1 * num2
            '/' -> if (num2 != 0.0) num1 / num2 else Double.NaN
            else -> num2
        }
    }

    private fun updateDisplay(text: String = currentExpression.toString()) {
        formula.text = text
    }
}
