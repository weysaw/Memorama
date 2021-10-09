package uabc.axel.ornelas.memorama

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uabc.axel.ornelas.memorama.databinding.ActivityMostrarInfoPuntajeBinding

/**
 * Coloca la información de los puntajes en la vista
 */
class InformacionPuntaje : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarInfoPuntajeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostrarInfoPuntajeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val puntaje = intent.getParcelableExtra<Puntaje>("puntaje")
        //Si tiene puntaje coloca los valores en los textView
        if (puntaje != null) {
            binding.jugadoresInfo.text = "${puntaje.jugadores[0]} vs ${puntaje.jugadores[1]}"
            binding.fechaInfo.text = puntaje.fecha
            val duracion: Int = puntaje.duracion.toInt()
            binding.duracion.text =  String.format("%02d:%02d:%02d", duracion / 3600, (duracion % 3600) / 60, duracion % 60);
            binding.ganador.text = puntaje.ganador
            binding.pares.text = puntaje.pares
        }
    }
}