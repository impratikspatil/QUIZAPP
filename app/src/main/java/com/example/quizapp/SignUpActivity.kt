package com.example.quizapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import com.example.quizapp.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth=FirebaseAuth.getInstance()


        //google sign in option
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient=GoogleSignIn.getClient(this,gso)


        binding.googlesignin.setOnClickListener {

                signingoogle()
        }


        //if user alread have an acouunt then redirect to login screen
        binding.alreadyaccount.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }


        //email and password manual sign in option
        binding.signupbtn.setOnClickListener {
            val email=binding.signupemail.text.toString()
            val pass=binding.signuppass.text.toString()
            val cnfpass=binding.cnfpasslayout.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && cnfpass.isNotEmpty())
            {
                if(pass == cnfpass)
                {
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if(it.isSuccessful)
                        {
                            Toast.makeText(this,"successfully created account",Toast.LENGTH_SHORT).show()
                            val intent= Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this,"Password is not matching",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Empty Feild Are Not Allowed !!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signingoogle()
    {
        val signinIntent=googleSignInClient.signInIntent
            launcher.launch(signinIntent)
    }

    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        result ->
        if(result.resultCode==Activity.RESULT_OK)
        {
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful)
        {
        val account:GoogleSignInAccount? = task.result
            if(account!=null)
            {
                updateUI(account)
            }
        }else{
            Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUI(account: GoogleSignInAccount) {

        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful)
            {
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT)
            }
        }

    }

}