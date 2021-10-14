package uabc.axel.ornelas.memorama

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import uabc.axel.ornelas.memorama.databinding.ActivityJuegoBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.properties.Delegates

/**
 * Controla el memorama y lo enlaza con android
 */
class Juego : AppCompatActivity() {

    private var colorPrincipal by Delegates.notNull<Int>()
    private lateinit var binding: ActivityJuegoBinding
    private lateinit var memorama: Memorama
    private var tableroCuadros = ArrayList<Button>()
    private var cuadroAnterior: Button? = null
    private var esperarTurno: Boolean = false
    private val tiempoInicial: Long = Date().time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        colorPrincipal = application.getColor(R.color.design_default_color_primary)
        //Obtiene si es de 2 jugadores o no
        val jugandoContraCPU: Boolean = intent.getBooleanExtra("jugandoContraCPU", false)
        //Crea el juego y los cuadros
        memorama = Memorama(jugandoContraCPU, 3, 4, "letras")
        val j1: String = intent.getStringExtra("j1")!!
        memorama.agregarJugador(j1)
        //Si juega con el cpu se agrega el cpu de jugador
        if (jugandoContraCPU)
            memorama.agregarJugador("CPU")
        else {
            val j2 = intent.getStringExtra("j2")!!
            memorama.agregarJugador(j2)
        }
        crearCuadros()
        actualizarTextos()
    }

    /**
     * Muestra el menu de opciones
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opciones, menu)
        return true
    }

    /**
     * Opciones que se ejecutan cuando se presiona un boton en el app bar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val diseno = memorama.diseno
        val columnas = memorama.tablero.columnas
        val renglones = memorama.tablero.renglones
        //Crea un nuevo juego con las dimensiones o el diseño indicado
        val retorno = when (item.itemId) {
            R.id.facil -> {
                memorama.nuevoJuego(3, 4, diseno)
                crearCuadros()
                true
            }
            R.id.normal -> {
                memorama.nuevoJuego(4, 4, diseno)
                crearCuadros()
                true
            }
            R.id.dificil -> {
                memorama.nuevoJuego(4, 5, diseno)
                crearCuadros()
                true
            }
            R.id.letras -> {
                memorama.nuevoJuego(columnas, renglones, "letras")
                colorPrincipal = application.getColor(R.color.design_default_color_primary)
                crearCuadros()
                true
            }
            R.id.números -> {
                memorama.nuevoJuego(columnas, renglones, "números")
                colorPrincipal = Color.rgb(0, 209, 180)
                crearCuadros()
                true
            }
            R.id.colores -> {
                memorama.nuevoJuego(columnas, renglones, "colores")
                colorPrincipal = Color.rgb(0, 189, 49)
                crearCuadros()
                true
            }
            R.id.reiniciar -> {
                memorama.nuevoJuego(columnas, renglones, diseno)
                crearCuadros()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        actualizarTextos()
        return retorno
    }

    /**
     * Se crean los cuadros del tablero y se colocan en la vista
     */
    private fun crearCuadros() {
        val renglones = memorama.tablero.renglones - 1
        val columnas = memorama.tablero.columnas - 1
        //Se crean los parametros y se establecen sus atributos
        val parametros = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        //Sirve para que respeten su espacio
        parametros.weight = 1f
        //Limpia el tablero
        tableroCuadros.clear()
        //Se establece para que se miren los bordes
        parametros.setMargins(2, 2, 2, 2)
        //Remueve todos los elementos anteriores que haya
        binding.tablero.removeAllViews()
        for (i in 0..renglones) {
            //Se crea un renglon nuevo y se ponen sus atributos
            val nuevoRenglon = LinearLayout(applicationContext)
            //Le establece las propiedades al renglon
            nuevoRenglon.orientation = LinearLayout.HORIZONTAL
            nuevoRenglon.layoutParams = parametros
            nuevoRenglon.gravity = Gravity.CENTER
            for (j in 0..columnas) {
                //Crea el boton
                val cuadro = Button(applicationContext)
                //Le establece los parametros al boton
                cuadro.layoutParams = parametros
                cuadro.text = "MEMO"
                cuadro.textSize = 28f
                cuadro.typeface = Typeface.DEFAULT_BOLD

                //Le cambia el color
                cuadro.setTextColor(application.getColor(R.color.design_default_color_on_primary))
                cuadro.setBackgroundColor(colorPrincipal)
                cuadro.setOnClickListener {
                    if (esperarTurno)
                        return@setOnClickListener
                    verificarCuadroPresionado(cuadro)
                }
                //Agrega el boton al tablero
                tableroCuadros += cuadro
                //Lo agrega a la vista y se mira
                nuevoRenglon.addView(cuadro)
            }
            //Agrega el renglon a la vista
            binding.tablero.addView(nuevoRenglon)
        }
    }

    /**
     * Realiza la jugada el CPU
     */
    private fun jugadaCPU() {
        //Verifica los posibles movimientos
        val movimientosPosibles = tableroCuadros.filter { it.isEnabled } as ArrayList<Button>
        //Revuelve los movimientos
        movimientosPosibles.shuffle()
        //Indica que debe de esperar el turno de la cpu
        esperarTurno = true
        //Primer movimiento de la cpu
        verificarCuadroPresionado(movimientosPosibles.removeFirst())
        //Se espera un segundo para que se vea la acción
        Timer().schedule(1500) {
            verificarCuadroPresionado(movimientosPosibles.removeFirst())
        }
    }

    /**
     * Verifica cuando se presiona un cuadro si se ha ganado
     */
    private fun verificarCuadroPresionado(cuadro: Button) {
        val num: Int = tableroCuadros.indexOf(cuadro)
        val carta = memorama.tablero.cartas[num]
        val contenidoCarta = carta.contenido
        //No se permite que se presione el mismo cuadro
        if (cuadro == cuadroAnterior)
            return
        //Si es diferente de colores significa que es texto
        if (memorama.diseno != "colores")
            cuadro.text = contenidoCarta
        else {
            //Coloca el color en el boton
            cuadro.text = ""
            cuadro.setBackgroundColor(contenidoCarta.toInt())
        }
        when {
            //Si la carta anterior esta vacia le indica que la tomada es esa
            memorama.cartaAnterior == null -> {
                cuadroAnterior = cuadro
                memorama.cartaAnterior = carta
            }
            //Verifica si la nueva carta es un par
            memorama.esParDeCarta(carta) -> {
                val color = Color.parseColor("#2F2F2F")
                val texto = "${memorama.nombreJugActual()}"
                this.runOnUiThread {
                    //Deshabilita los cuadros, los cambia de color e indica quien tomo la carta
                    cuadroAnterior?.isEnabled = false
                    cuadroAnterior?.text = texto
                    cuadroAnterior?.setBackgroundColor(color)
                    cuadroAnterior = null
                    cuadro.isEnabled = false
                    cuadro.text = texto
                    cuadro.setBackgroundColor(color)
                    actualizarTextos()
                    //Se verifica si hay más cartas que agarrar
                    if (memorama.verificarFinDelJuego())
                        finDelJuego()
                }
                //Se espera unos segundos para que vuelva a realizar el movimiento
                Timer().schedule(1500) {
                    if (memorama.jugandoContraCPU && memorama.turno == 1 && !memorama.findDelJuego) {
                        jugadaCPU()
                    }
                }
            }
            //Si la carta que tomo no es par, la carta anterior la deja, se espera un segundo y luego se actualiza
            else -> {
                cuadroAnterior = null
                memorama.cartaAnterior = null
                memorama.cambiarTurno()
                esperarTurno = true
                Timer().schedule(1500) {
                    regresarCartasDefecto()
                    esperarTurno = false
                    actualizarTextos()
                    //Verifica si es turno del cpu y si esta jugando contra el
                    if (memorama.jugandoContraCPU && memorama.turno == 1)
                        jugadaCPU()
                }

            }
        }
    }

    /**
     * Muestra los mensaje cuando se acaba el juego
     */
    private fun finDelJuego() {
        val jugador: Jugador? = memorama.verificarGanador()
        //Si es empate no guarda las puntuaciones
        val texto: String = if (jugador != null) {
            "El Ganador es ${jugador.nombre}"
        } else
            "Fue empate"
        guardarPuntuacion(jugador)
        //Muesta quien fue el ganador o si fue empate en el dialogo
        AlertDialog.Builder(this)
            .setTitle("FIN DEL JUEGO")
            .setMessage(texto)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    /**
     * Guarda la puntuacion del jugador
     */
    private fun guardarPuntuacion(ganador: Jugador?) {
        val nombreArchivo = "Puntuaciones"
        val duracionPartida: Long = (Date().time - tiempoInicial) / 1000
        val infoJugadores = "${memorama.jugadores[0].nombre},${memorama.jugadores[1].nombre}"
        //Contenido para el archivo
        val contenido = if (ganador != null)
            "$infoJugadores,${Date()},${duracionPartida},${ganador.nombre},${ganador.obtenerPuntaje()}\n"
        else
            "$infoJugadores,${Date()},${duracionPartida},Fue Empate,Quedo Empate\n"
        //Escribe el contenido en el archivo
        applicationContext.openFileOutput(nombreArchivo,
            Context.MODE_PRIVATE or Context.MODE_APPEND).use {
            it.write(contenido.toByteArray())
        }
        Toast.makeText(applicationContext,
            "Puntuación Guardada",
            Toast.LENGTH_SHORT).show()
    }

    /**
     * Regresa los valores de los cuadros por defecto
     */
    private fun regresarCartasDefecto() {
        //Recorre todos los cuadros y le pone el texto defecto
        tableroCuadros.forEach { cuadro ->
            if (cuadro.isEnabled) {
                cuadro.text = "MEMO"
                cuadro.setBackgroundColor(colorPrincipal)
            }
        }
    }

    /**
     * Actualiza las puntuaciones y el turno del jugador
     */
    private fun actualizarTextos() {
        binding.turno.text = "Es turno del jugador: ${memorama.nombreJugActual()}"
        val jugador1: Jugador = memorama.jugadores[0]
        binding.puntosJ1.text = "Puntos ${jugador1.nombre}: ${jugador1.obtenerPuntaje()}"
        val jugador2: Jugador = memorama.jugadores[1]
        binding.puntosJ2.text = "Puntos ${jugador2.nombre}: ${jugador2.obtenerPuntaje()}"

    }

}

