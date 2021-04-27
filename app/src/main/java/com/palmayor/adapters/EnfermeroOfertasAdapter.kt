package com.palmayor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.palmayor.R
import com.palmayor.models.response.EnfermeroOfertaResponse
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class EnfermeroOfertasAdapter(private val postulaciones: List<EnfermeroOfertaResponse>, private val context: Context)
    : RecyclerView.Adapter<EnfermeroOfertasAdapter.ViewHolder>(){

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val txtNombreAbueloOferta: TextView = view.findViewById(R.id.txtNombreAbueloOferta)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
        val txtHorario : TextView = view.findViewById(R.id.txtHorario)
        val txtDireccionOferta : TextView = view.findViewById(R.id.txtDireccionOferta)
        val imgAbuelo: ImageView = view.findViewById(R.id.imgAbuelo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.prototype_oferta, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postulaciones.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postulacion = postulaciones[position]

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatterfecha = SimpleDateFormat("d MMM yyyy")
        val formatterdia = SimpleDateFormat("h:mm a")
        val diaformateadoInicio = formatterfecha.format(parser.parse(postulacion.oferta.fechaAtenciones[0].fecha)!!)
        val diaformateadoFin = formatterfecha.format(parser.parse(postulacion.oferta.fechaAtenciones[postulacion.oferta.fechaAtenciones.size-1].fecha)!!)
        val horaformateadaInicio = formatterdia.format(parser.parse("2020-01-01T"+postulacion.oferta.fechaAtenciones[0].rangoHora.inicio)!!)
        val horaformateadaFin = formatterdia.format(parser.parse("2020-01-01T"+postulacion.oferta.fechaAtenciones[0].rangoHora.fin)!!)

        holder.txtNombreAbueloOferta.text = postulacion.oferta.anciano.persona.nombre + " " + postulacion.oferta.anciano.persona.apellidos
        holder.txtFecha.setText("${diaformateadoInicio} - ${diaformateadoFin}")
        holder.txtHorario.text = horaformateadaInicio.toString() + " - " + horaformateadaFin.toString()
        holder.txtDireccionOferta.text = postulacion.oferta.direccion
        val picBuilder = Picasso.Builder(context)
        picBuilder.downloader(OkHttp3Downloader(context))
        picBuilder.build().load(postulacion.oferta.anciano.persona.foto)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgAbuelo)
    }

}