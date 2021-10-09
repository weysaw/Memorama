package uabc.axel.ornelas.memorama

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Clase que se usa como adaptador para mostrar el recycler
 */
class ScoreRecycler(private val datosLocales: ArrayList<Puntaje>) :
    RecyclerView.Adapter<ScoreRecycler.ViewHolder>() {
    //Tal vez se deba poner protected
    lateinit var onClickListener: View.OnClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        //Indica el estilo que debe tener el recycler
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fila_puntaje, parent, false)
        view.setOnClickListener(onClickListener)
        //Devuelve la vista creada
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Coloca la información del puntaje en los campos de texto
        val puntaje = datosLocales[position]
        val jugadores = puntaje.jugadores
        holder.jugadores.text = "${jugadores[0]} vs ${jugadores[1]}"
        holder.fecha.text = puntaje.fecha
    }

    /**
     * Tamaño de los datos
     */
    override fun getItemCount(): Int {
        return datosLocales.size
    }

    /**
     * Clase interna para localizar los datos que se necesitan para colocar la info
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jugadores: TextView = itemView.findViewById(R.id.jugadores)
        val fecha: TextView = itemView.findViewById(R.id.fecha)
    }
}