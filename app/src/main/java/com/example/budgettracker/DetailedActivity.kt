package com.example.budgettracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {
    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)



        val addTransactionButton = findViewById<Button>(R.id.updateButton)
        val labelInput = findViewById<EditText>(R.id.labelinput)
        val amountInput = findViewById<EditText>(R.id.amountinput)
        val descriptionInput = findViewById<EditText>(R.id.descriptioninput)
        val labelLayout = findViewById<TextInputLayout>(R.id.labellayout)
        val amountLayout = findViewById<TextInputLayout>(R.id.amountlayout)
        val closeButton = findViewById<ImageButton>(R.id.closeBtn)
        val updateBtn = findViewById<Button>(R.id.updateButton)
        val rootView = findViewById<View>(R.id.rootView)


        transaction = intent.getSerializableExtra("transaction") as Transaction
        labelInput.setText(transaction.label)
        amountInput.setText(transaction.amount.toString())
        descriptionInput.setText(transaction.description)

        rootView.setOnClickListener{
            this.window.decorView.clearFocus()

            val imm=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken,0)
        }




        labelInput.addTextChangedListener {
            updateBtn.visibility= View.VISIBLE

            if (it!!.isNotEmpty())
                labelLayout.error = null
        }

        amountInput.addTextChangedListener{
            updateBtn.visibility= View.VISIBLE
            if (it!!.isNotEmpty())
                amountLayout.error = null
        }
        descriptionInput.addTextChangedListener {
            updateBtn.visibility= View.VISIBLE

        }

        addTransactionButton.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if(label.isEmpty()){
                labelLayout.error = "Please enter valid label"

            }
            else if(amount==null){
                amountLayout.error = "Please enter a valid amount"
            }else{
                val transaction = amount?.let { it1 -> Transaction(transaction.id,label, it1,description) }
                if (transaction != null) {
                    update(transaction)
                }


            }

        }
        closeButton.setOnClickListener{
            finish()
        }





    }

    private fun update(transaction: Transaction){
        val db:AppDatabase = Room.databaseBuilder(
            this,AppDatabase::class.java,"transactions").build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }

    }
}