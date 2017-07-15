package ca.danielw.rankr.activities

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ca.danielw.rankr.R
import ca.danielw.rankr.fragments.EmailFragment
import ca.danielw.rankr.utils.Constants
import net.sargue.mailgun.Configuration
import net.sargue.mailgun.Mail
import net.sargue.mailgun.content.Body


class InviteActivity : Activity() {

    var emails = mutableListOf<String?>(null, null, null, null)
    var mLeagueName = ""
    var mEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)

        mLeagueName = intent.getStringExtra(Constants.LEAGUE_NAME)
        mEmail = intent.getStringExtra(Constants.EMAIL)

        val btnSend = findViewById(R.id.btnSend) as Button
        val btnSkip = findViewById(R.id.btnSkip) as Button

        val tvWarning = findViewById(R.id.tvWarning) as TextView

        val et1 = findViewById(R.id.etEmail1) as EditText
        val et2 = findViewById(R.id.etEmail2) as EditText
        val et3 = findViewById(R.id.etEmail3) as EditText
        val et4 = findViewById(R.id.etEmail4) as EditText

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

        btnSkip.setOnClickListener {
            val intent = Intent(this@InviteActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSend.setOnClickListener {
            emails = emails.filter { it != null }.toMutableList()
            if(emails.isNotEmpty()){
                sendEmails(emails)
            }
            val intent = Intent(this@InviteActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun sendEmails(mutableList: MutableList<String?>){
        val configuration = Configuration()
                .domain(Constants.DOMAIN_NAME)
                .apiKey(Constants.API_KEY)
                .from("Rankr", Constants.FROM_DOMAIN)

        val body = Body.builder()
                .h3("Join " + mLeagueName + " on Rankr")
                .p(mEmail + " has invited you to join the Rankr league <b>" + mLeagueName + "</b>.")
                .link("http://play.google.com/store/apps/details?id=" + Constants.PACKAGE_NAME, "Download the App")
                .br()
                .link("www.danielw.ca/rankr/signup?" + Constants.NODE_LEAGUE + "=" + mLeagueName, "Join Now")
                .br()
                .p("Make by the Rankr team (Daniel Wang)")
                .p("Toronto, ON")
                .build()

        for (item in mutableList){
            if (item != null) {
                SendEmailTask(configuration, item, body).execute()
            }
        }
    }

    private class SendEmailTask (val configuration: Configuration, val item: String, val body: Body): AsyncTask<Unit, Unit, Unit>(){

        override fun doInBackground(vararg params: Unit?) {
            Mail.using(configuration)
                    .to(item)
                    .subject("You've been invited to join a Rankr League")
                    .content(body)
                    .build()
                    .send()
        }
    }

    override fun onBackPressed() {

        finish()
        val intent = Intent(this@InviteActivity, MainActivity::class.java)
        startActivity(intent)
    }
}