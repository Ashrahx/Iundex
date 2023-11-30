package com.dim.iundex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class RegisterActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var checkBoxTerms: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextUsername = findViewById(R.id.usernameEditText)
        editTextEmail = findViewById(R.id.emailEditText)
        editTextPassword = findViewById(R.id.passwordEditText)
        editTextConfirmPassword = findViewById(R.id.confirmPasswordEditText)
        btnRegister = findViewById(R.id.registerButton)
        checkBoxTerms = findViewById(R.id.termsCheckbox)

        btnRegister.setOnClickListener {
            if (validateInput()) {
                runService("https://50c9-2806-267-1489-8689-8dd6-2b48-5c1b-4db3.ngrok-free.app/Iundex/get.php")
            }
        }
    }

    private fun runService(url: String) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { _ ->
                Toast.makeText(applicationContext, "Welcome $editTextUsername", Toast.LENGTH_SHORT).show()
                // Redirigir
                val intent = Intent(this@RegisterActivity, SecretActivity::class.java)
                startActivity(intent)
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                Log.e("VolleyError", error.toString())
            }) {

            override fun getParams(): Map<String, String> {
                val parameters = HashMap<String, String>()
                parameters["username"] = editTextUsername.text.toString()
                parameters["email"] = editTextEmail.text.toString()
                parameters["password"] = editTextPassword.text.toString()
                Log.d("MyApp", "Params: $parameters")
                return parameters
            }
        }

        // Configurar el tiempo de espera en milisegundos (30000 para 30 segundos)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun validateInput(): Boolean {
        val username = editTextUsername.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val confirmPassword = editTextConfirmPassword.text.toString()

        // Validar que no estén vacíos
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validar formato de email
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
            Toast.makeText(applicationContext, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validar contraseñas
        if (password != confirmPassword) {
            Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validar términos y condiciones
        if (!checkBoxTerms.isChecked) {
            Toast.makeText(applicationContext, "You must agree to terms and conditions", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validaciones exitosas
        return true
    }
}
