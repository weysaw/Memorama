package uabc.axel.ornelas.memorama

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Puntaje(
    val jugadores: ArrayList<String>,
    val fecha: String,
    val duracion: String,
    val ganador: String,
    val pares: String,
) : Parcelable
