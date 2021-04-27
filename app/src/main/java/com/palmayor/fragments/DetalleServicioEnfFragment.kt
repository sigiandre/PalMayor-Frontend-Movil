package com.palmayor.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.palmayor.R
import com.palmayor.models.response.ServicioResponse
import com.palmayor.network.ServicioService
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class DetalleServicioEnfFragment(Id: Int): Fragment() {
    lateinit var tvNombreAbuelo: TextView
    lateinit var tvNombreEnfermero: TextView
    lateinit var tvEspecialidad: TextView
    lateinit var tvFechaInicio: TextView
    lateinit var tvFechaFin: TextView
    lateinit var tvHoras: TextView
    lateinit var tvDireccion: TextView
    lateinit var ivAbuelo: ImageView
    lateinit var ivEnfermero: ImageView

    var servicioId = Id
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_detalle_servicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNombreAbuelo = view.findViewById(R.id.tvDetalleNombreAbuelo)
        tvNombreEnfermero = view.findViewById(R.id.tvDetalleNombreEnfermero)
        tvEspecialidad = view.findViewById(R.id.tvDetalleEspecialidad)
        tvFechaInicio = view.findViewById(R.id.tvDetalleFechaInicio)
        tvFechaFin = view.findViewById(R.id.tvDetalleFechaFin)
        tvHoras = view.findViewById(R.id.tvDetalleHoras)
        tvDireccion = view.findViewById(R.id.tvDetalleDireccion)
        ivAbuelo = view.findViewById(R.id.ivDetalleAbuelo)
        ivEnfermero = view.findViewById(R.id.ivDetalleEnfermero)

        loadDetalle(view.context)
    }

    private fun loadDetalle(context: Context) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ServicioService::class.java)
        val servicioResponse = service.getServicioById(servicioId)

        servicioResponse.enqueue(object : Callback<ServicioResponse>{
            override fun onFailure(call: Call<ServicioResponse>, t: Throwable) {
                Log.d("Error: ", t.toString())
            }

            override fun onResponse(
                call: Call<ServicioResponse>,
                response: Response<ServicioResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("Servicio Exitoso: ", response.body()!!.toString())
                    val servicioObject : ServicioResponse = response.body()!!
                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val formatterfecha = SimpleDateFormat("d MMM yyyy")
                    val formatterdia = SimpleDateFormat("h:mm a")
                    val diaformateadoInicio = formatterfecha.format(parser.parse(servicioObject.oferta.fechaAtenciones[0].fecha)!!)
                    val diaformateadoFin = formatterfecha.format(parser.parse(servicioObject.oferta.fechaAtenciones[servicioObject.oferta.fechaAtenciones.size-1].fecha)!!)
                    val horaformateadaInicio = formatterdia.format(parser.parse("2020-01-01T"+servicioObject.oferta.fechaAtenciones[0].rangoHora.inicio)!!)
                    val horaformateadaFin = formatterdia.format(parser.parse("2020-01-01T"+servicioObject.oferta.fechaAtenciones[0].rangoHora.fin)!!)

                    tvNombreAbuelo.text = servicioObject.oferta.anciano.persona.nombre + " " + servicioObject.oferta.anciano.persona.apellidos
                    tvNombreEnfermero.text = servicioObject.enfermero.persona.nombre + " " + servicioObject?.enfermero.persona.apellidos
                    tvEspecialidad.text = servicioObject.enfermero.especialidad.nombre
                    tvFechaInicio.text = diaformateadoInicio.toString()
                    tvFechaFin.text = diaformateadoFin.toString()
                    tvHoras.text = horaformateadaInicio.toString() + " - " + horaformateadaFin.toString()
                    tvDireccion.text = servicioObject.oferta.direccion

                    val pic = Picasso.Builder(context)
                    pic.downloader(OkHttp3Downloader(context))
                    pic.build().load(servicioObject.oferta.anciano.persona.foto)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(ivAbuelo)

                    pic.build().load(servicioObject.enfermero.persona.foto)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(ivEnfermero)

                }
            }
        })


    }
}