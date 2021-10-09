package uabc.axel.ornelas.memorama

/**
 * Contiene la logica de un jugador de memorama
 */
class Jugador(val nombre: String, val paresRecogidos: ArrayList<String> = arrayListOf()) {
    /**
     * Limpia los pares que tenga el jugador
     */
    fun quitarPares() {
        paresRecogidos.clear()
    }

    /**
     * Obtiene el puntaje del jugador que es el tamaño del mazo
     */
    fun obtenerPuntaje(): Int {
        return paresRecogidos.size
    }
}