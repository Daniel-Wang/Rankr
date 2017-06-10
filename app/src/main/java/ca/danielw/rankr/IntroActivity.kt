package ca.danielw.rankr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.intro_1.*
import android.content.Intent
import ca.danielw.rankr.activities.CreateLeagueActivity
import ca.danielw.rankr.activities.SignInActivity


class IntroActivity : AppCompatActivity() {

    private val TAG = IntroActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_1)

        btnSignIn.setOnClickListener {
            Log.e(TAG, "Sign in")
            //launch the signin flow

            val myIntent = Intent(this@IntroActivity, SignInActivity::class.java)
            this@IntroActivity.startActivity(myIntent)
        }

        btnCreateLeague.setOnClickListener {
            Log.e(TAG, "Create league")
            //Start create league flow

            val myIntent = Intent(this@IntroActivity, CreateLeagueActivity::class.java)
            this@IntroActivity.startActivity(myIntent)

        }
    }

}
