package ca.danielw.rankr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.intro_1.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent




class IntroActivity : AppCompatActivity() {

    private val TAG = IntroActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_1)

        btnSignIn.setOnClickListener {
            Log.e(TAG, "Sign in")
            //launch the signin flow
        }

        btnCreateLeague.setOnClickListener {
            Log.e(TAG, "Create league")
            //Start create league flow

            val myIntent = Intent(this@IntroActivity, CreateLeagueActivity::class.java)
            this@IntroActivity.startActivity(myIntent)

        }
    }

}
