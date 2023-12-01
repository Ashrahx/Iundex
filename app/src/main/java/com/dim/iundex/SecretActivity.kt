package com.dim.iundex

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SecretActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secret)

        bottomNavigation()
    }

    private fun bottomNavigation() {
        val homeBtn: LinearLayout = findViewById(R.id.homeBtn)

        homeBtn.setOnClickListener {
            startActivity(Intent(this@SecretActivity, SecretActivity::class.java))
        }
    }
}
