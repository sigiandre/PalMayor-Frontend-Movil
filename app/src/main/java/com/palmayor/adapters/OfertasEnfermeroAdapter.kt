package com.palmayor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.palmayor.R
import com.palmayor.fragments.EnfermeroHomeFragment
import com.palmayor.fragments.OfertasFragment
import com.palmayor.models.response.OfertaResponse
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_enfermero_home.view.*
import kotlinx.android.synthetic.main.prototype_oferta.view.*

class OfertasEnfermeroAdapter(private val ofertas: List<OfertaResponse>, private val context: Context, private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<OfertasEnfermeroAdapter.ViewHolder>() {
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

        holder.view.txtFecha.text = fecha.fecha
        holder.view.txtNombreAbueloOferta.text = "${oferta.anciano.persona.nombre} ${oferta.anciano.persona.apellidos}"
        holder.view.txtDireccionOferta.text = oferta.direccion
        holder.view.txtHorario.text = "${fecha.rangoHora.inicio} - ${fecha.rangoHora.fin}"

        val pic = Picasso.Builder(context)
        pic.downloader(OkHttp3Downloader(context))

        pic.build().load(oferta.anciano.persona.foto).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).into(holder.view.imgAbuelo)

        holder.view.cvOferta.setOnClickListener{
            itemClickListener.onItemClicked(oferta.id)
        }

    }

}