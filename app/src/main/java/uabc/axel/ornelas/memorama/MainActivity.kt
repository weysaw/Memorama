package uabc.axel.ornelas.memorama

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import uabc.axel.ornelas.memorama.databinding.ActivityMainBinding

/**
 * Actividad principal que lleva a las opciones para jugar el memorama
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Muestra un dialogo para ingresar el nombre
        binding.unJugador.setOnClickListener {
            val intent = Intent(applicationContext, Juego::class.java)
            //Obtiene el campo para poner el nombre
            val editText1 = obtenerEditText("Jugador 1")
            //Alert dialog que muestra el campo para el nombre
            obtenerDialogo()
                .setPositiveButton("OK") { _, _ ->
                    val nombre = editText1.text.toString()
                    if (nombre == "" || nombre.contains(',')) {
                        Toast.makeText(this,
                            "Debes de agregar algun nombre y no debe de contener comas",
                            Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    intent.putExtra("j2", "CPU")
                    intent.putExtra("j1", nombre)
                    abrirJuego(true, intent)
                }
                .setView(editText1)
                .create().show()
        }
        //Muestra 2 dialogos para ingresar los nombres
        binding.dosJugadores.setOnClickListener {
            val intent = Intent(applicationContext, Juego::class.java)
            //Obtiene el campo para poner el nombre
            val editText1 = obtenerEditText("Jugador 1")
            val toast =
                Toast.makeText(this,
                    "Debes de agregar algun nombre y no debe de contener comas",
                    Toast.LENGTH_SHORT)
            //Alert dialog que muestra el campo para el nombre
            obtenerDialogo().setPositiveButton("OK") { _, _ ->
                val nombre = editText1.text.toString()
                if (nombre == "" || nombre.contains(',')) {
                    toast.show()
                    return@setPositiveButton
                }
                val editText2 = obtenerEditText("Jugador 2")
                //Alert dialog que muestra el campo para el nombre
                obtenerDialogo().setPositiveButton("OK") { _, _ ->
                    val nombre2 = editText2.text.toString()
                    if (nombre2 == "" || nombre.contains(',')) {
                        toast.show()
                        return@setPositiveButton
                    }
                    intent.putExtra("j1", nombre)
                    intent.putExtra("j2", nombre2)
                    abrirJuego(false, intent)
                }.setView(editText2).create().show()
            }.setView(editText1).create().show()
        }
    }

    /**
     * Abre la actividad del juego
     */
    private fun abrirJuego(jugandoContraCPU: Boolean, intent: Intent) {
        intent.putExtra("jugandoContraCPU", jugandoContraCPU)
        startActivity(intent)
    }

    /**
     * Obtiene el nombre del jugador por medio de un alert dialog
     */
    private fun obtenerDialogo(): AlertDialog.Builder {
        val alerta = AlertDialog.Builder(this)
            .setTitle("Jugador")
            .setMessage("Ingrese el nombre del jugador")
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        return alerta
    }

    /**
     * Obtiene un editText se usa para ingresar al dialog
     */
    private fun obtenerEditText(texto: String): EditText {
        val editText = EditText(this)
        editText.hint = texto
        editText.inputType = InputType.TYPE_CLASS_TEXT
        return editText
    }

    /**
     * Opciones del menu que cambian los cuadros
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opciones_menu_principal, menu)
        return true
    }

    /**
     * Se indican las opciones para modificar la vista del menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.historial -> {
                startActivity(Intent(applicationContext, Score::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}