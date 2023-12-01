package com.dim.iundex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.emailEditText)
        editTextPassword = findViewById(R.id.passwordEditText)
        btnLogin = findViewById(R.id.loginButton)

        btnLogin.setOnClickListener {
            if (validateInput()) {
                runLoginService("https://50c9-2806-267-1489-8689-8dd6-2b48-5c1b-4db3.ngrok-free.app/Iundex/login.php")
            }
        }
    }

    private fun runLoginService(url: String) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                if (response == "success") {

                } else {
                    Toast.makeText(applicationContext, "$response", Toast.LENGTH_SHORT).show()
                    println("$response")
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                Log.e("VolleyError", error.toString())
            }) {

            override fun getParams(): Map<String, String> {
                val parameters = HashMap<String, String>()
                parameters["email"] = editTextEmail.text.toString()
                parameters["password"] = editTextPassword.text.toString()
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

    private fun runGetUsernameService(url: String) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                // El response debe contener el nombre de usuario
                val username = response.trim()
                Toast.makeText(applicationContext, "Welcome $username", Toast.LENGTH_SHORT).show()

                // Redirigir a la actividad deseada después del inicio de sesión
                val intent = Intent(this@LoginActivity, SecretActivity::class.java)
                startActivity(intent)
                finish()  // Cerrar la actividad actual
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                Log.e("VolleyError", error.toString())
            }) {

            override fun getParams(): Map<String, String> {
                val parameters = HashMap<String, String>()
                parameters["email"] = editTextEmail.text.toString()
                return parameters
            }
        }

        // Configurar el tiempo de espera en milisegundos (30000 para 30 segundos)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            1500,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun validateInput(): Boolean {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        // Validar que no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validar formato de email
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
            Toast.makeText(applicationContext, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validaciones exitosas
        return true
    }
}
