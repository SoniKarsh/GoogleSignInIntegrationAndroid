package com.example.karshsoni.googleintegrationdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val TAG = "MainActivity"
    val RC_SIGN_IN = 9999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        sign_in_button.setOnClickListener {
            when (it.getId()) {
                R.id.sign_in_button -> signIn()
            }

        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            displayName.text = account!!.displayName
            emailId.text = account.email
            val profileImage = account.photoUrl
            Picasso.get().load(profileImage).into(profilePicture)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }

    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){
            displayName.text = account.displayName
            emailId.text = account.email
            val profileImage = account.photoUrl
            Picasso.get().load(profileImage).into(profilePicture)
        }else{
            Toast.makeText(this@MainActivity, "onStart Failed", Toast.LENGTH_LONG).show()
        }

    }

}
