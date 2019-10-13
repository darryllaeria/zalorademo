package com.example.testzalora

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.yinglan.keyboard.HideUtil
import java.lang.Exception
import kotlin.collections.ArrayList
import java.util.regex.Pattern
import android.content.DialogInterface



class MainActivity : AppCompatActivity() {

    private lateinit var txtInput: EditText
    private lateinit var txtCounter: TextView
    private lateinit var btnSend: Button
    private val MAX_CHAR: Int = 50
    private lateinit var outputArray: ArrayList<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        outputArray = ArrayList()

        initUI()
    }

    private fun initUI() {
        HideUtil.init(this@MainActivity)

        txtCounter = findViewById(R.id.txt_count)

        txtInput = findViewById(R.id.txt_input)
        //txtInput.setText("I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself.")

        txtInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                txtCounter.text = txtInput.text.length.toString()

                if(txtInput.text.length > MAX_CHAR && !txtInput.text.trim().contains(" ")) {
                    Toast.makeText(this@MainActivity, getString(R.string.input_invalid), Toast.LENGTH_SHORT).show()

                }
            }
        })

        txtCounter.setText(txtInput.text.length.toString())

        btnSend = findViewById(R.id.btn_send)
        btnSend.setOnClickListener {
            if(txtInput.text.isEmpty()) {
                Toast.makeText(this@MainActivity, getString(R.string.input_empty), Toast.LENGTH_SHORT).show()

            } else {
                splitMessage(txtInput.text.trim().toString())
            }
        }
    }

    private fun splitMessage(input: String?) {
        try {
            outputArray.clear()

            val mPattern = Pattern.compile("\\G\\s*(.{1,$MAX_CHAR})(?=\\s|$)", Pattern.DOTALL)
            val match = mPattern.matcher(input)
            while (match.find()) {
                outputArray.add((match.group(1)))
                Log.d("OUTPUT_VALUE", outputArray.toString())
            }

            Log.d("OUTPUT_ARRAY_SIZE", outputArray.size.toString())

            for (i in 0 until outputArray.size) {
                Log.d("OUTPUT_VALUE_MODIFY", "${i+1}/${outputArray.size} ${outputArray[i]}")

                outputArray[i] = "${i+1}/${outputArray.size} ${outputArray[i]}"
            }

            val dialogItems = outputArray.toTypedArray()
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle(getString(R.string.output_message))
            dialogBuilder.setItems(dialogItems) { dialog, item ->
                //val selectedText = dialogItems[item]
                Toast.makeText(this@MainActivity, dialogItems[item], Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            val alertDialogObject = dialogBuilder.create()
            alertDialogObject.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
