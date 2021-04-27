package com.palmayor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.palmayor.R
import com.palmayor.fragments.OfertasFragment
import com.palmayor.models.response.OfertaResponse
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.prototype_oferta.view.*
import java.text.SimpleDateFormat

class OfertasAdapter(private val ofertas: List<OfertaResponse>, private val context: Context, private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<OfertasAdapter.ViewHolder>(){
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    }

    interface OnItemClickListener {
        fun onItemClicked(idOferta: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.prototype_oferta,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ofertas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oferta = ofertas[position]
        val fecha = ofertas[position].fechaAtenciones.filter { o -> o.ofertaId == oferta.id}[0]

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatterfecha = SimpleDateFormat("d MMM yyyy")
        val formatterdia = SimpleDateFormat("h:mm a")

        val diaformateadoInicio = formatterfecha.format(parser.parse(oferta.fechaAtenciones[0].fecha)!!).toString()
        val diaformateadoFin = formatterfecha.format(parser.parse(oferta.fechaAtenciones[oferta.fechaAtenciones.size-1].fecha)!!).toString()
        val horaformateadaInicio = formatterdia.format(parser.parse("2020-01-01T"+fecha.rangoHora.inicio)!!).toString()
        val horaformateadaFin = formatterdia.format(parser.parse("2020-01-01T"+fecha.rangoHora.fin)!!).toString()

        //"${oferta.fechaAtenciones[0].fecha.slice(IntRange(0,9))} - ${oferta.fechaAtenciones[oferta.fechaAtenciones.size-1].fecha.slice(IntRange(0,9))}"
        //"${fecha.rangoHora.inicio.slice(IntRange(0,4))} - ${fecha.rangoHora.fin.slice(IntRange(0,4))}"
        holder.view.txtFecha.text = "${diaformateadoInicio} - ${diaformateadoFin}"
        holder.view.txtNombreAbueloOferta.text = "${oferta.anciano.persona.nombre} ${oferta.anciano.persona.apellidos}"
        holder.view.txtDireccionOferta.text = oferta.direccion
        holder.view.txtHorario.text = "${horaformateadaInicio} - ${horaformateadaFin}"

        val pic = Picasso.Builder(context)
        pic.downloader(OkHttp3Downloader(context))

        pic.build().load(oferta.anciano.persona.foto).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).into(holder.view.imgAbuelo)

        holder.view.cvOferta.setOnClickListener{
            itemClickListener.onItemClicked(oferta.id)
        }

    }


}