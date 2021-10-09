package uabc.axel.ornelas.memorama

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import uabc.axel.ornelas.memorama.databinding.ActivityScoreBinding
import java.io.FileNotFoundException

class Score : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Se obtiene los datos del archivo
        try {
            var infoGuardada = ""
            val nombreArchivo = "Puntuaciones"
            //Busca el archivo por si hay records previos
            applicationContext.openFileInput(nombreArchivo).bufferedReader().useLines { lines ->
                infoGuardada = lines.fold("") { textAnt, textDesp ->
                    "$textAnt$textDesp\n"
                }
            }
            if (infoGuardada == "")
                throw FileNotFoundException()
            //Divide los datos por comas
            val datosPartidos: List<String> = infoGuardada.split("\n")
            var puntajes = ArrayList<Puntaje>()
            datosPartidos.forEach {
                if (it == "")
                    return@forEach
                val puntaje = it.split(",")
                puntajes += Puntaje(arrayListOf(puntaje[0], puntaje[1]),
                    puntaje[2],
                    puntaje[3],
                    puntaje[4],
                    puntaje[5])
            }

            // Se le indica el layout al reclycler
            binding.lista.layoutManager = LinearLayoutManager(this)
            // Se crea un adaptador con el arreglo
            val adapter = ScoreRecycler(puntajes)
            // Se colocan las peliculas en el adaptador
            binding.lista.adapter = adapter
            //Acción que se realiza cuando se presiona una pelicula
            adapter.onClickListener = View.OnClickListener { v ->
                val pos: Int = binding.lista.getChildAdapterPosition(v)
                val intent = Intent(this, InformacionPuntaje::class.java)
                intent.putExtra("puntaje", puntajes[pos])
                startActivity(intent)
            }
        } catch (e: Exception) {
            when (e) {
                is FileNotFoundException -> {
                    Toast.makeText(applicationContext, "Scores no registrados", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> throw e
            }
        }

    }

    /**
     * Opciones del menu que cambian los cuadros
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opciones_score, menu)
        return true
    }

    /**
     * Se indican las opciones para modificar la vista del menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.limpiar -> {
                val nombreArchivo = "Puntuaciones"
                //Escribe el contenido en el archivo
                applicationContext.openFileOutput(nombreArchivo, Context.MODE_PRIVATE).use {
                    it.write("".toByteArray())
                }
                Toast.makeText(this, "Scores limpiados con exito", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}