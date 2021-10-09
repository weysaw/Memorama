package uabc.axel.ornelas.memorama

/**
 * Define la propiedad que tiene una carta si esta volteado y su simbolo
 */
class Carta(val contenido: String) {
    var descartada = false
        private set

    /**
     * Voltea la cara de la carta
     */
    fun descartar() {
        descartada = true
    }
}