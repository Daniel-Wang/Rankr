package ca.danielw.rankr.activities

import android.app.Activity
import android.content.Intent
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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


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

        val queue = Volley.newRequestQueue(this)
        val url = Constants.SEND_EMAIL_ENDPOINT

        val jsonBody = JSONObject()

        val emailList = JSONArray()

        mutableList.forEach {
            val emailJson = JSONObject()
            emailJson.put(it, true)
            emailList.put(emailJson)
        }

        jsonBody.put(Constants.EMAILS, emailList)
        jsonBody.put(Constants.NODE_LEAGUE, mLeagueName)
        jsonBody.put("userEmail", mEmail)

        // Request a string response from the provided URL.
        val stringRequest = JsonObjectRequest(url, jsonBody,
                Response.Listener<JSONObject> { }, Response.ErrorListener { })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    override fun onBackPressed() {

        finish()
        val intent = Intent(this@InviteActivity, MainActivity::class.java)
        startActivity(intent)
    }
}