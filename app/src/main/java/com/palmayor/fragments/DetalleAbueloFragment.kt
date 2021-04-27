package com.palmayor.fragments

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.palmayor.R
import com.palmayor.models.response.AncianoResponse
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class DetalleAbueloFragment(anciano: AncianoResponse) : Fragment() {
    lateinit var igDetalleAbuelo : ImageView
    lateinit var tvNombreAbuelo: TextView
    lateinit var tvDniAbuelo : TextView
    lateinit var tvFechaAbuelo : TextView
    var ancianoObject = anciano
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_detalle_abuelo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        igDetalleAbuelo = view.findViewById(R.id.igDetalleAbuelo)
        tvNombreAbuelo = view.findViewById(R.id.tvNombreAbuelo)
        tvDniAbuelo = view.findViewById(R.id.tvDniAbuelo)
        tvFechaAbuelo = view.findViewById(R.id.tvFechaAbuelo)

        loadDetalle(view.context)
    }

    private fun loadDetalle(context: Context) {
        val pic = Picasso.Builder(context)
        pic.downloader(OkHttp3Downloader(context))
        pic.build().load(ancianoObject?.persona!!.foto)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(igDetalleAbuelo)

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("d MMM yyyy")
        val nacimientoformateado = formatter.format(parser.parse(ancianoObject.persona!!.fechaNacimiento)!!)
        //ancianoObject.persona!!.fechaNacimiento.subSequence(0,10)
        tvNombreAbuelo.text = ancianoObject.persona!!.nombre + " " + ancianoObject.persona!!.apellidos
        tvDniAbuelo.text = ancianoObject.persona!!.dni
        tvFechaAbuelo.text = nacimientoformateado.toString()
    }

}