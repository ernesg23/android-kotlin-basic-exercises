package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Variables de estado para Ejercicio 3
    private var intentosRestantes = 3
    private val usuarioCorrecto = "admin"
    private val passwordCorrecta = "1234"

    // Variable de estado para Ejercicio 1
    private var textoAlterno = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- NAVEGACION ---
        val btnNavEj1 = findViewById<Button>(R.id.btnActividad1)
        val btnNavEj2 = findViewById<Button>(R.id.btnActividad2)
        val btnNavEj3 = findViewById<Button>(R.id.btnActividad3)

        val contenedorEj1 = findViewById<LinearLayout>(R.id.contenedorEj1)
        val contenedorEj2 = findViewById<LinearLayout>(R.id.contenedorEj2)
        val contenedorEj3 = findViewById<LinearLayout>(R.id.contenedorEj3)

        btnNavEj1.setOnClickListener {
            contenedorEj1.visibility = View.VISIBLE
            contenedorEj2.visibility = View.GONE
            contenedorEj3.visibility = View.GONE
        }
        btnNavEj2.setOnClickListener {
            contenedorEj1.visibility = View.GONE
            contenedorEj2.visibility = View.VISIBLE
            contenedorEj3.visibility = View.GONE
        }
        btnNavEj3.setOnClickListener {
            contenedorEj1.visibility = View.GONE
            contenedorEj2.visibility = View.GONE
            contenedorEj3.visibility = View.VISIBLE
        }

        // --- LÓGICA EJERCICIO 1 ---
        val tvTextoEj1 = findViewById<TextView>(R.id.tvTextoEj1)
        val btnCambiarTextoEj1 = findViewById<Button>(R.id.btnCambiarTextoEj1)

        btnCambiarTextoEj1.setOnClickListener {
            if (textoAlterno) {
                tvTextoEj1.text = "Texto original"
            } else {
                tvTextoEj1.text = "¡El texto ha sido cambiado exitosamente!"
            }
            textoAlterno = !textoAlterno
        }

        // --- LÓGICA EJERCICIO 2 ---
        val etNumero1 = findViewById<EditText>(R.id.etNumero1)
        val etNumero2 = findViewById<EditText>(R.id.etNumero2)
        val tvResultadoEj2 = findViewById<TextView>(R.id.tvResultadoEj2)
        val btnSumarEj2 = findViewById<Button>(R.id.btnSumarEj2)

        btnSumarEj2.setOnClickListener {
            val num1 = etNumero1.text.toString().toIntOrNull()
            val num2 = etNumero2.text.toString().toIntOrNull()

            if (num1 != null && num2 != null) {
                val suma = num1 + num2
                tvResultadoEj2.text = "Resultado: $suma"
            } else {
                Toast.makeText(this, "Por favor ingrese números enteros válidos", Toast.LENGTH_SHORT).show()
            }
        }

        // --- LÓGICA EJERCICIO 3 ---
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnValidarEj3 = findViewById<Button>(R.id.btnValidarEj3)
        val tvEstadoLogin = findViewById<TextView>(R.id.tvEstadoLogin)

        btnValidarEj3.setOnClickListener {
            val userIngresado = etUsuario.text.toString()
            val passIngresado = etPassword.text.toString()

            if (userIngresado == usuarioCorrecto && passIngresado == passwordCorrecta) {
                Toast.makeText(this, "¡Login Exitoso!", Toast.LENGTH_LONG).show()
                tvEstadoLogin.text = "Acceso concedido"
                intentosRestantes = 3
            } else {
                intentosRestantes--
                if (intentosRestantes > 0) {
                    tvEstadoLogin.text = "Credenciales incorrectas. Intentos restantes: $intentosRestantes"
                } else {
                    tvEstadoLogin.text = "Bloqueado por 5 segundos..."

                    etUsuario.isEnabled = false
                    etPassword.isEnabled = false
                    btnValidarEj3.isEnabled = false

                    Handler(Looper.getMainLooper()).postDelayed({
                        intentosRestantes = 3
                        tvEstadoLogin.text = "Intentos restantes: 3"
                        etUsuario.isEnabled = true
                        etPassword.isEnabled = true
                        btnValidarEj3.isEnabled = true
                        etUsuario.text.clear()
                        etPassword.text.clear()
                    }, 5000)
                }
            }
        }
    }
}