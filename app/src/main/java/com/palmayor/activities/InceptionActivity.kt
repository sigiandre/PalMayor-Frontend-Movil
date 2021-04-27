package com.palmayor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.palmayor.R
import com.palmayor.activities.SignUpActivity
import kotlinx.android.synthetic.main.activity_inception.*

class InceptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inception)

        btnInceptionFamiliar.setOnClickListener{
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            signUpIntent.putExtra("rol","2")
            startActivity(signUpIntent)
        }

        btnInceptionEnfermero.setOnClickListener{
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            signUpIntent.putExtra("rol","3")
            startActivity(signUpIntent)
        }
    }
}
