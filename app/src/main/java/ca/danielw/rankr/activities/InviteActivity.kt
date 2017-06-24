package ca.danielw.rankr.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ca.danielw.rankr.R
import ca.danielw.rankr.fragments.EmailFragment
import ca.danielw.rankr.utils.Constants
import net.sargue.mailgun.Configuration
import net.sargue.mailgun.Mail


class InviteActivity : Activity() {

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
                    tvWarning.visibility = View.GONE
                    btnSend.isEnabled = true
                } else if(et1.text.isNotBlank()) {
                    tvWarning.visibility = View.VISIBLE
                    emails[0] = null
                    btnSend.isEnabled = false
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
                    tvWarning.visibility = View.GONE;
                    btnSend.isEnabled = true
                } else if(et2.text.isNotBlank()) {
                    tvWarning.visibility = View.VISIBLE;
                    emails[1] = null
                    btnSend.isEnabled = false
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
                    tvWarning.visibility = View.GONE
                    btnSend.isEnabled = true
                } else if(et3.text.isNotBlank()) {
                    tvWarning.visibility = View.VISIBLE
                    emails[2] = null
                    btnSend.isEnabled = false
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
                    tvWarning.visibility = View.GONE
                    btnSend.isEnabled = true
                } else if(et4.text.isNotBlank()) {
                    tvWarning.visibility = View.VISIBLE
                    emails[3] = null
                    btnSend.isEnabled = false
                }
            }

        })

        btnSend.setOnClickListener {
            emails = emails.filter { it != null }.toMutableList()
            Log.e("hi", "1")
            if(emails.isNotEmpty()){
                sendEmails(emails)
            }
        }
    }

    private fun sendEmails(mutableList: MutableList<String?>){
        val configuration = Configuration()
                .domain(Constants.DOMAIN_NAME)
                .apiKey(Constants.API_KEY)
                .from("Team Rankr", Constants.FROM_DOMAIN)

        Log.e("inside", "send emails")
        for (item in mutableList){
            Log.e("Yo invites", item)
            Mail.using(configuration)
                    .to(item)
                    .subject("Invitation to a Rankr League")
                    .text("Hey there, you've been invited to this rankr league")
                    .build()
                    .send()
        }
    }
}
