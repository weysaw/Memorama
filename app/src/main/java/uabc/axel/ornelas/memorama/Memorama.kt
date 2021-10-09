package uabc.axel.ornelas.memorama

/**
 * Contiene la logica del juego de memorama
 */
class Memorama(
    val jugandoContraCPU: Boolean,
    columnas: Int,
    renglones: Int,
    var diseno: String,
) {
    //Contiene las cartas en el tablero
    var tablero = Tablero(columnas, renglones)
        private set

    //Es el jugador que le toca agarrar carta
    var turno: Int = 0
        private set

    //Jugadores que juegan en la partida
    var jugadores: ArrayList<Jugador> = ArrayList()
        private set
    //Carta que se toma anterior para verificar el par
    var cartaAnterior: Carta? = null

    //Indica el fin del juego
    var findDelJuego: Boolean = false
        private set

    init {
        tablero.colocarCartasTablero(diseno)
    }

    /**
     * Reinicia a los valores por defecto del juego
     */
    private fun valoresDefecto() {
        tablero.limpiarTablero()
        tablero.colocarCartasTablero(diseno)
        turno = 0
        findDelJuego = false
        jugadores.forEach {
            it.quitarPares()
        }
    }

    /**
     * Descarta las cartas que esten juego
     */
    private fun descartarCartas(carta: Carta) {
        cartaAnterior?.descartar()
        carta.descartar()
    }

    /**
     * Inicia un nuevo juego con nuevo diseño o dimensiones
     */
    fun nuevoJuego(columnas: Int, renglones: Int, diseno: String) {
        tablero.columnas = columnas
        tablero.renglones = renglones
        this.diseno = diseno
        valoresDefecto()
    }


    /**
     * Agrega el jugador a la partida
     */
    fun agregarJugador(nombre: String) {
        jugadores += Jugador(nombre)
    }

    /**
     * Verifica si se encontro un par de cartas
     */
    fun esParDeCarta(cartaTomada: Carta): Boolean {
        //Si la carta anterior tiene el mismo contenido a la carta tomada es un par
        if (cartaAnterior?.contenido == cartaTomada.contenido) {
            jugadores[turno].paresRecogidos += cartaTomada.contenido
            //Se descartan las cartas del tablero
            descartarCartas(cartaTomada)
            cartaAnterior = null
            return true
        }
        return false
    }

    /**
     * Cambia el turno del jugador
     */
    fun cambiarTurno() {
        turno = ++turno % 2
    }

    /**
     * Si todas las cartas estan descartadas se acaba el juego
     */
    fun verificarFinDelJuego(): Boolean {
        //Verfiica que todas las cartas esten descartadas
        findDelJuego = tablero.cartas.all { carta -> carta.descartada }
        return findDelJuego
    }

    /**
     * Verifica cual es el ganador y se lo devuelve
     */
    fun verificarGanador(): Jugador? {
        return when {
            jugadores[0].obtenerPuntaje() > jugadores[1].obtenerPuntaje() -> jugadores[0]
            jugadores[0].obtenerPuntaje() < jugadores[1].obtenerPuntaje() -> jugadores[1]
            else -> null
        }
    }

    /**
     * Devuelve el nombre del jugador actual
     */
    fun nombreJugActual(): String {
        return jugadores[turno].nombre
    }


}