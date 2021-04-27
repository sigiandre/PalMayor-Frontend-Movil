package com.palmayor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.palmayor.R
import com.palmayor.models.response.EnfermeroOfertaResponse
import com.palmayor.models.response.EnfermeroResponse
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso


class PostulanteAdapter(private val postulantes: List<EnfermeroOfertaResponse>, private val context: Context, private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<PostulanteAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val cvPostulante = view.findViewById<CardView>(R.id.cvPostulante)
        val tvNombrePostulante = view.findViewById<TextView>(R.id.tvNombrePostulante)
        val ivFotoPostulante = view.findViewById<ImageView>(R.id.ivFotoPostulante)
        val tvContactoPostulante = view.findViewById<TextView>(R.id.tvContactoPostulante)
    }

    interface OnItemClickListener{
        fun onItemClicked(enfermero: EnfermeroResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.prototype_postulante, parent, false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postulantes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postulante = postulantes[position]

        val pic = Picasso.Builder(context)
        pic.downloader(OkHttp3Downloader(context))

        pic.build().load(postulante.enfermero.persona.foto)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.ivFotoPostulante)

        holder.tvNombrePostulante.text = postulante.enfermero.persona.nombre + " " + postulante.enfermero.persona.apellidos
        holder.tvContactoPostulante.text = postulante.enfermero.persona.telefono

        holder.cvPostulante.setOnClickListener{
            itemClickListener.onItemClicked(postulante.enfermero)
        }

    }
}