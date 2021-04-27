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
import com.palmayor.models.response.AncianoResponse
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

class ListaAbuelosAdapter(private val ancianos: List<AncianoResponse>, private val context:Context, private val itemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<ListaAbuelosAdapter.ViewHolder>()
{
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cvAbuelo = view.findViewById(R.id.cvAbuelo) as CardView
        val tvNombreAbuelo = view.findViewById(R.id.tvNombreAbuelo) as TextView
        val ivFotoAbuelo = view.findViewById(R.id.ivFotoAbuelo) as ImageView
        val tvEdadAbuelo = view.findViewById(R.id.tvAbueloEdadpt) as TextView
    }

    interface OnItemClickListener{
        fun onItemClicked(anciano: AncianoResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.prototype_lista_abuelos, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ancianos.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anciano = ancianos[position]

        val pic = Picasso.Builder(context)
        pic.downloader(OkHttp3Downloader(context))

        pic.build().load(anciano.persona!!.foto).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).into(holder.ivFotoAbuelo)
        holder.tvNombreAbuelo.text = anciano.persona.nombre + " " + anciano.persona.apellidos
        holder.tvEdadAbuelo.text = (2020 - anciano.persona.fechaNacimiento.slice(IntRange(0,3)).toInt()).toString()+" años"
        holder.cvAbuelo.setOnClickListener{
            itemClickListener.onItemClicked(anciano)
        }


    }


}