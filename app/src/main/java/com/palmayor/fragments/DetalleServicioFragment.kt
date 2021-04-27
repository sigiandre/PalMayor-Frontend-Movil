package com.palmayor.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.palmayor.R
import com.palmayor.models.response.ServicioResponse
import com.palmayor.network.ServicioService
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detalle_servicio.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat


class DetalleServicioFragment(idServicio: Int) : Fragment() {

    lateinit var tvNombreAbuelo: TextView
    lateinit var tvNombreEnfermero: TextView
    lateinit var tvEspecialidad: TextView
    lateinit var tvFechaInicio: TextView
    lateinit var tvFechaFin: TextView
    lateinit var tvHoras: TextView
    lateinit var tvDireccion: TextView
    lateinit var ivAbuelo: ImageView
    lateinit var ivEnfermero: ImageView
    val idServ = idServicio

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_servicio, container, false)
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

        LoadDetalleServicio(view.context)

    }

    private fun LoadDetalleServicio(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val servicioService: ServicioService
        servicioService = retrofit.create(ServicioService::class.java)

        val responseServicio = servicioService.getServicioById(idServ)
        responseServicio.enqueue(object: Callback<ServicioResponse>{
            override fun onFailure(call: Call<ServicioResponse>, t: Throwable) {
                Log.d("Get ServicioById Fail", "Error: "+t.toString())
            }

            override fun onResponse(
                call: Call<ServicioResponse>,
                response: Response<ServicioResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("ServicioById Success", response.body()!!.toString())
                    val servicio: ServicioResponse = response.body()!!

                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val formatterfecha = SimpleDateFormat("d MMM yyyy")
                    val formatterdia = SimpleDateFormat("h:mm a")
                    val diaformateadoInicio = formatterfecha.format(parser.parse(servicio.oferta.fechaAtenciones[0].fecha)!!)
                    val diaformateadoFin = formatterfecha.format(parser.parse(servicio.oferta.fechaAtenciones[servicio.oferta.fechaAtenciones.size-1].fecha)!!)
                    val horaformateadaInicio = formatterdia.format(parser.parse("2020-01-01T"+servicio.oferta.fechaAtenciones[0].rangoHora.inicio)!!)
                    val horaformateadaFin = formatterdia.format(parser.parse("2020-01-01T"+servicio.oferta.fechaAtenciones[0].rangoHora.fin)!!)
                    //servicio.oferta.fechaAtenciones[0].fecha.slice(IntRange(0,9))
                    //servicio.oferta.fechaAtenciones[servicio.oferta.fechaAtenciones.size-1].fecha.slice(IntRange(0,9))
                    //servicio.oferta.fechaAtenciones[0].rangoHora.inicio.slice(IntRange(0,4))+ " - " + servicio.oferta.fechaAtenciones[0].rangoHora.fin.slice(IntRange(0,4))
                    tvNombreAbuelo.text = servicio.oferta.anciano.persona.nombre + " " + servicio.oferta.anciano.persona.apellidos
                    tvNombreEnfermero.text = servicio.enfermero.persona.nombre + " " + servicio.enfermero.persona.apellidos
                    tvEspecialidad.text = servicio.enfermero.especialidad.nombre
                    tvFechaInicio.text = diaformateadoInicio.toString()
                    tvFechaFin.text = diaformateadoFin.toString()
                    tvHoras.text = horaformateadaInicio.toString() + " - " + horaformateadaFin.toString()
                    tvDireccion.text = servicio.oferta.direccion

                    val picBuilder = Picasso.Builder(context)
                    picBuilder.downloader(OkHttp3Downloader(context))
                    picBuilder.build().load(servicio.oferta.anciano.persona.foto)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(ivAbuelo)

                    picBuilder.build().load(servicio.enfermero.persona.foto)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(ivEnfermero)

                }
            }
        })
    }
}
