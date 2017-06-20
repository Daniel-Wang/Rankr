package ca.danielw.rankr.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import ca.danielw.rankr.R
import ca.danielw.rankr.fragments.EmailFragment
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class InviteActivity : AppCompatActivity() {

    var emails = mutableListOf<String?>(null, null, null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)

        val btnSend = findViewById(R.id.btnSend) as Button

        val tvWarning = findViewById(R.id.tvWarning) as TextView

        val et1 = findViewById(R.id.etEmail1) as EditText
        val et2 = findViewById(R.id.etEmail2) as EditText
        val et3 = findViewById(R.id.etEmail3) as EditText
        val et4 = findViewById(R.id.etEmail4) as EditText

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT)

        et1.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = et1.text.toString()
                if(EmailFragment.isValidEmail(text)){
                    emails[0] = text
                    btnSend.isEnabled = true
                } else if(et1.text.isNotBlank()) {
                    btnSend.isEnabled = false
                    emails[0] = null
                }
            }

        })

        et2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = et2.text.toString()
                if(EmailFragment.isValidEmail(text)){
                    emails[1] = text
                    btnSend.isEnabled = true
                } else if(et2.text.isNotBlank()) {
                    btnSend.isEnabled = false
                    emails[1] = null
                }
            }

        })

        et3.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = et3.text.toString()
                if(EmailFragment.isValidEmail(text)){
                    emails[2] = text
                    btnSend.isEnabled = true
                } else if(et3.text.isNotBlank()) {
                    btnSend.isEnabled = false
                    emails[2] = null
                }
            }

        })

        et4.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = et4.text.toString()
                if(EmailFragment.isValidEmail(text)){
                    emails[3] = text
                    btnSend.isEnabled = true
                } else if(et4.text.isNotBlank()) {
                    btnSend.isEnabled = false
                    emails[3] = null
                }
            }

        })

        btnSend.setOnClickListener {
            emails = emails.filter { it != null }.toMutableList()
            if(emails.isNotEmpty()){
                sendEmails(emails)
            }
        }
    }

    private fun sendEmails(mutableList: MutableList<String?>){
        for (item in mutableList) Log.e("Emails Sent", item)
    }
}
