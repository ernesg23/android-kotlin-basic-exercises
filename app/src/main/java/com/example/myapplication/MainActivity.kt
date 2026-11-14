package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {

    // Variables de estado ej1
    private var textoAlterno = false

    // Variables de estado ej3
    private var intentosRestantes = 3
    private val usuarioCorrecto = "admin"
    private val passwordCorrecta = "1234"

    // Variables de estado ej5
    private lateinit var sensorManager: SensorManager
    private var sensorActivo: Sensor? = null
    private var tipoSensorSeleccionado = 0

    private lateinit var tarjetaCreativaSensor: LinearLayout
    private lateinit var tvTituloSensor: TextView
    private lateinit var tvInterpretacionCreativa: TextView
    private lateinit var barraNivelSensor: ProgressBar
    private lateinit var tvValoresCrudosSensor: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // NAVEGACIÓN OPTIMIZADA
        val tolvas = mapOf(
            R.id.btnActividad1 to findViewById<LinearLayout>(R.id.contenedorEj1),
            R.id.btnActividad2 to findViewById<LinearLayout>(R.id.contenedorEj2),
            R.id.btnActividad3 to findViewById<LinearLayout>(R.id.contenedorEj3),
            R.id.btnActividad5 to findViewById<LinearLayout>(R.id.contenedorEj5)
        )

        tolvas.forEach { (btnId, contenedorDestino) ->
            findViewById<Button>(btnId).setOnClickListener {
                tolvas.values.forEach { it.visibility = if (it == contenedorDestino) View.VISIBLE else View.GONE }
                if (contenedorDestino.id == R.id.contenedorEj5 && tipoSensorSeleccionado != 0) {
                    activarSensor(tipoSensorSeleccionado)
                } else {
                    detenerSensor()
                }
            }
        }

        // EJERCICIO 1
        val tvTextoEj1 = findViewById<TextView>(R.id.tvTextoEj1)
        findViewById<Button>(R.id.btnCambiarTextoEj1).setOnClickListener {
            tvTextoEj1.text = if (textoAlterno) "Texto original" else "¡El texto ha sido cambiado exitosamente!"
            textoAlterno = !textoAlterno
        }

        // EJERCICIO 2
        val etNumero1 = findViewById<EditText>(R.id.etNumero1)
        val etNumero2 = findViewById<EditText>(R.id.etNumero2)
        val tvResultadoEj2 = findViewById<TextView>(R.id.tvResultadoEj2)

        findViewById<Button>(R.id.btnSumarEj2).setOnClickListener {
            val num1 = etNumero1.text.toString().toIntOrNull()
            val num2 = etNumero2.text.toString().toIntOrNull()

            if (num1 != null && num2 != null) {
                tvResultadoEj2.text = "Resultado: ${num1 + num2}"
            } else {
                Toast.makeText(this, "Por favor ingrese números enteros válidos", Toast.LENGTH_SHORT).show()
            }
        }

        // EJERCICIO 3
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnValidarEj3 = findViewById<Button>(R.id.btnValidarEj3)
        val tvEstadoLogin = findViewById<TextView>(R.id.tvEstadoLogin)
        val controlesLogin = listOf(etUsuario, etPassword, btnValidarEj3)

        btnValidarEj3.setOnClickListener {
            if (etUsuario.text.toString() == usuarioCorrecto && etPassword.text.toString() == passwordCorrecta) {
                Toast.makeText(this, "¡Login Exitoso!", Toast.LENGTH_LONG).show()
                tvEstadoLogin.text = "Acceso concedido"
                intentosRestantes = 3
                return@setOnClickListener
            }

            intentosRestantes--
            if (intentosRestantes > 0) {
                tvEstadoLogin.text = "Credenciales incorrectas. Intentos restantes: $intentosRestantes"
            } else {
                tvEstadoLogin.text = "Bloqueado por 5 segundos..."
                controlesLogin.forEach { it.isEnabled = false }

                Handler(Looper.getMainLooper()).postDelayed({
                    intentosRestantes = 3
                    tvEstadoLogin.text = "Intentos restantes: 3"
                    controlesLogin.forEach { it.isEnabled = true }
                    etUsuario.text.clear()
                    etPassword.text.clear()
                }, 5000)
            }
        }

        // EJERCICIO 5 (SENSORES)
        tarjetaCreativaSensor = findViewById(R.id.tarjetaCreativaSensor)
        tvTituloSensor = findViewById(R.id.tvTituloSensor)
        tvInterpretacionCreativa = findViewById(R.id.tvInterpretacionCreativa)
        barraNivelSensor = findViewById(R.id.barraNivelSensor)
        tvValoresCrudosSensor = findViewById(R.id.tvValoresCrudosSensor)

        val botonesSensores = mapOf(
            R.id.btnSensorAccel to (Sensor.TYPE_ACCELEROMETER to "Sensor: Acelerómetro / Orientación"),
            R.id.btnSensorLuz to (Sensor.TYPE_LIGHT to "Sensor: Luz Ambiental"),
            R.id.btnSensorProx to (Sensor.TYPE_PROXIMITY to "Sensor: Proximidad")
        )

        botonesSensores.forEach { (btnId, config) ->
            findViewById<Button>(btnId).setOnClickListener {
                tvTituloSensor.text = config.second
                activarSensor(config.first)
            }
        }
    }

    private fun activarSensor(tipoSensor: Int) {
        detenerSensor()
        tipoSensorSeleccionado = tipoSensor
        sensorActivo = sensorManager.getDefaultSensor(tipoSensor)

        if (sensorActivo != null) {
            sensorManager.registerListener(this, sensorActivo, SensorManager.SENSOR_DELAY_UI)
        } else {
            tvInterpretacionCreativa.text = "Sensor no disponible en este dispositivo"
            tvValoresCrudosSensor.text = "Hardware no detectado"
            barraNivelSensor.progress = 0
        }
    }

    private fun detenerSensor() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        val (estado, colorHex, progreso) = when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val (x, y, z) = event.values
                val fuerzaTotal = sqrt(x * x + y * y + z * z)
                val porcentaje = ((fuerzaTotal / 20f) * 100).toInt().coerceIn(0, 100)

                tvValoresCrudosSensor.text = String.format("X: %.2f | Y: %.2f | Z: %.2f m/s²", x, y, z)

                when {
                    fuerzaTotal > 15f -> Triple("¡TERREMOTO! Estás agitando el celular", "#FFCDD2", porcentaje)
                    y > 7.5f -> Triple("Parado", "#BBDEFB", porcentaje)
                    y < -7.5f -> Triple("Parado invertido", "#D1C4E9", porcentaje)
                    x > 3.5f -> Triple("Inclinado a la IZQUIERDA", "#C8E6C9", porcentaje)
                    x < -3.5f -> Triple("Inclinado a la DERECHA", "#FFF9C4", porcentaje)
                    else -> Triple("Nivelado / En reposo", "#F5F5F5", porcentaje)
                }
            }

            Sensor.TYPE_LIGHT -> {
                val lux = event.values[0]
                val porcentaje = ((lux / 1000f) * 100).toInt().coerceIn(0, 100)
                tvValoresCrudosSensor.text = String.format("Intensidad lumínica: %.1f lx", lux)

                when {
                    lux < 15f -> Triple("Modo Noche: Oscuridad total", "#CFD8DC", porcentaje)
                    lux < 250f -> Triple("Iluminación de interior", "#FFF9C4", porcentaje)
                    else -> Triple("¡Luz intensa / Exterior!", "#FFE082", porcentaje)
                }
            }

            Sensor.TYPE_PROXIMITY -> {
                val distancia = event.values[0]
                val maxRango = sensorActivo?.maximumRange ?: 5f
                tvValoresCrudosSensor.text = String.format("Distancia detectada: %.1f cm", distancia)

                if (distancia < maxRango) {
                    Triple("¡ALERTA! Objeto muy cerca", "#FFCDD2", 100)
                } else {
                    Triple("Camino despejado", "#C8E6C9", 10)
                }
            }

            else -> return
        }

        tvInterpretacionCreativa.text = estado
        tarjetaCreativaSensor.setBackgroundColor(Color.parseColor(colorHex))
        barraNivelSensor.progress = progreso
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onPause() {
        super.onPause()
        detenerSensor()
    }

    override fun onResume() {
        super.onResume()
        if (tipoSensorSeleccionado != 0 && findViewById<LinearLayout>(R.id.contenedorEj5).visibility == View.VISIBLE) {
            activarSensor(tipoSensorSeleccionado)
        }
    }
}
