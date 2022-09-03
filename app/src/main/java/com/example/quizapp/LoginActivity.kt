package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.quizapp.databinding.ActivityLoginBinding
import com.example.quizapp.databinding.ActivitySignUpBinding

import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        binding.notregistered.setOnClickListener {
            val intent= Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.signinbtn.setOnClickListener {

            val email=binding.loginemail.text.toString()
            val pass=binding.loginpassword.text.toString()

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.loginemail.error="Invalid email format!"
                binding.loginemail.requestFocus()

            }
            else if(pass.isEmpty()){
                binding.loginpassword.error="Password can not be empty!"
                binding.loginpassword.requestFocus()
            }

            if(email.isNotEmpty() && pass.isNotEmpty() )
            {

                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if(it.isSuccessful)
                        {
                            val intent= Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this,"LOGIN FAILED", Toast.LENGTH_SHORT).show()
                        }
                    }


            }
            else{
                Toast.makeText(this,"Empty Feild Are Not Allowed !!", Toast.LENGTH_SHORT).show()
            }

        }
    }
}