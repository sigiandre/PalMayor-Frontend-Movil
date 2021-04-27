package com.palmayor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.palmayor.R
import com.palmayor.models.response.ServicioResponse
import java.text.SimpleDateFormat

class ServicioEnfermeroAdapter (private val servicios: List<ServicioResponse>, private val context: Context, private val itemClickListener: OnItemClickListener)
    :RecyclerView.Adapter<ServicioEnfermeroAdapter.ViewHolder>()
{
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val cvServicio: CardView = view.findViewById(R.id.cvServicio)
        val tvNombreAbuelo: TextView = view.findViewById(R.id.tvNombreAbuelo)
        val tvNombreEnfermero: TextView = view.findViewById(R.id.tvNombreEnfermero)
        val tvFechaInicio: TextView = view.findViewById(R.id.tvFechaInicio)
        val tvFechaFin: TextView = view.findViewById(R.id.tvFechaFin)
        val tvDireccion: TextView = view.findViewById(R.id.tvDireccion)
        val tvServicioNum: TextView = view.findViewById(R.id.tvServicionNum)
    }
    interface OnItemClickListener{
        fun onItemClicked(idServicio: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.prototype_servicio, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return servicios.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val servicio = servicios[position]


        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("d MMM yyyy")
        val diaformateadoInicio = formatter.format(parser.parse(servicio.oferta.fechaAtenciones[0].fecha)!!)
        val diaformateadoFin = formatter.format(parser.parse(servicio.oferta.fechaAtenciones[servicio.oferta.fechaAtenciones.size-1].fecha)!!)
        holder.tvServicioNum.text = "Servicio #" + (position+1).toString()
        holder.tvNombreAbuelo.setText(servicio.oferta.anciano.persona.nombre + " " + servicio.oferta.anciano.persona.apellidos)
        holder.tvNombreEnfermero.setText(servicio.enfermero.persona.nombre + " " + servicio.enfermero.persona.apellidos)
        holder.tvFechaInicio.text = diaformateadoInicio.toString()
        holder.tvFechaFin.text = diaformateadoFin.toString()
        holder.tvDireccion.text = servicio.oferta.direccion

        holder.cvServicio.setOnClickListener {
            itemClickListener.onItemClicked(servicio.id)
        }
    }
}