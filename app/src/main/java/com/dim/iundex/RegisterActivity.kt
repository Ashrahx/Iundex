package com.dim.iundex

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    // Elementos
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var termsCheckbox: CheckBox
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Referencias
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        termsCheckbox = findViewById(R.id.termsCheckbox)
        registerButton = findViewById(R.id.registerButton)
        loginLink = findViewById(R.id.loginLink)

        registerButton.setOnClickListener {
            // Valores de los campos
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val termsAccepted = termsCheckbox.isChecked

            // Validación
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Please complete all fields.")
            } else if (password != confirmPassword) {
                showToast("Passwords do not match.")
            } else if (!termsAccepted) {
                showToast("You must agree to terms and conditions.")
            } else {
                showToast("Welcome $username")

                // A la página de inicio
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()  // Opcional: cierra la actividad actual si no se espera volver a ella
            }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, SecretActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
