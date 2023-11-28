package com.dim.iundex

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var biometricPrompt: BiometricPrompt

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fingerprintButton: Button = findViewById(R.id.fingerprintButton)

        if (isBiometricPromptAvailable()) {
            setupBiometricPrompt(fingerprintButton)
        } else {
            fingerprintButton.isEnabled = false
        }
    }

    private fun isBiometricPromptAvailable(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.USE_BIOMETRIC
        ) == PackageManager.PERMISSION_GRANTED &&
                BiometricPrompt.from(this).isAvailable
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setupBiometricPrompt(fingerprintButton: Button) {
        val executor: Executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Autenticación exitosa
                Toast.makeText(
                    this@MainActivity,
                    "Biometric authentication succeeded",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Errores de autenticación
                Toast.makeText(
                    this@MainActivity,
                    "Biometric authentication failed: $errString",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        biometricPrompt = BiometricPrompt(this, executor, callback)
    }
}
