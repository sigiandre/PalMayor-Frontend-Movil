package com.palmayor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.palmayor.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btnWelcomeStart.setOnClickListener { v ->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
