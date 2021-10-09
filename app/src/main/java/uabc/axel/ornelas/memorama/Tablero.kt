package uabc.axel.ornelas.memorama

import android.graphics.Color

/**
 * Define las propiedades que puede tener un tablero
 */
class Tablero(var columnas: Int, var renglones: Int) {
    var cartas: ArrayList<Carta> = ArrayList()
        private set

    /**
     * Limpias las cartas que tiene el tablero
     */
    fun limpiarTablero() {
        cartas.clear()
    }

    /**
     * Coloca las cartas en el tablero
     */
    fun colocarCartasTablero(diseno: String) {
        val numPares: Int = (columnas * renglones / 2)
        val cartasAgregar = ArrayList<Carta>()
        when (diseno) {
            "letras" -> {
                val letraFinal: Char = (numPares + 'A'.code - 1).toChar()
                for (letra in 'A'..letraFinal) {
                    val texto: String = letra.toString()
                    cartasAgregar += Carta(texto)
                    cartasAgregar += Carta(texto)
                }
            }
            "números" -> {
                for (i in 1..numPares) {
                    val texto: String = i.toString()
                    cartasAgregar += Carta(texto)
                    cartasAgregar += Carta(texto)
                }
            }
            "colores" -> {
                val secuencia = arrayListOf<Int>(
                    Color.rgb(0, 255, 0),
                    Color.rgb(0, 255, 255),
                    Color.rgb(255, 0, 0),
                    Color.rgb(50, 0, 255),
                    Color.rgb(255, 255, 0),
                    Color.rgb(255, 0, 255),
                    Color.rgb(255, 128, 255),
                    Color.rgb(0, 137, 48),
                    Color.rgb(137, 0, 0),
                    Color.rgb(0, 0, 137),
                )
                for (i in 1..numPares) {
                    secuencia.shuffle()
                    val color: String = secuencia.removeFirst().toString()
                    cartasAgregar += Carta(color)
                    cartasAgregar += Carta(color)
                }
            }
        }
        cartasAgregar.shuffle()
        cartas = cartasAgregar
    }
}