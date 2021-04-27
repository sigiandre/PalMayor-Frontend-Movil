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
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.palmayor.R
import com.palmayor.models.request.EnfermeroOfertaRequest
import com.palmayor.models.response.OfertaResponse
import com.palmayor.network.EnfermeroOfertaService
import com.palmayor.network.OfertasService
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat


class DetalleOfertaEnfermeroFragment(idOferta: Int, idEnfermero: Int, correoEnfermero: String) : Fragment() {

    lateinit var  tvNombreAbuelo: TextView
    lateinit var ivAbuelo: ImageView
    lateinit var tvEdad: TextView
    lateinit var tvFechaInicio: TextView
    lateinit var tvFechaFin: TextView
    lateinit var tvRangoHoras: TextView
    lateinit var tvDireccion: TextView
    lateinit var tvDescripcion: TextView
    lateinit var btnPostular: TextView
    val idOferta = idOferta
    val idEnfermero = idEnfermero
    val correo = correoEnfermero

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_oferta_enfermero, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNombreAbuelo = view.findViewById(R.id.tvNombreAbueloDOE)
        ivAbuelo = view.findViewById(R.id.ivAbueloDOE)
        tvEdad = view.findViewById(R.id.tvEdadAbueloDOE)
        tvFechaInicio = view.findViewById(R.id.tvFechaInicioDOE)
        tvFechaFin = view.findViewById(R.id.tvFechaFinDOE)
        tvRangoHoras = view.findViewById(R.id.tvRangoHorasDOE)
        tvDireccion = view.findViewById(R.id.tvDireccionDOE)
        tvDescripcion = view.findViewById(R.id.tvDescripcionDOE)
        btnPostular = view.findViewById(R.id.btnPostular)

        LoadDetalleOferta(view.context)

        btnPostular.setOnClickListener {
            postularOferta(view.context)
        }
    }

    private fun postularOferta(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val postulacion: EnfermeroOfertaRequest = EnfermeroOfertaRequest(idEnfermero,idOferta)

        val postularService: EnfermeroOfertaService = retrofit.create(EnfermeroOfertaService::class.java)
        val postularRequest = postularService.postEnfermeroOferta(postulacion)
        postularRequest.enqueue(object: Callback<EnfermeroOfertaRequest>{
            override fun onFailure(call: Call<EnfermeroOfertaRequest>, t: Throwable) {
                Log.d("Post Postulacion Fail", "Error: "+t.toString())
            }

            override fun onResponse(
                call: Call<EnfermeroOfertaRequest>,
                response: Response<EnfermeroOfertaRequest>
            ) {
                if(response.isSuccessful){
                    //Log.d("OfertaById Success", response.body()!!.toString())
                    Toast.makeText(context,"Se ha registrado su postulación a la Oferta",Toast.LENGTH_LONG).show()
                    var fm= fragmentManager
                    var fragmentPostulaciones = EnfermeroPostulacionesFragment(correo)

                    var ft = fm!!.beginTransaction()
                    ft.addToBackStack(null)
                    ft.replace(R.id.fragmentEnfermeroContainer,fragmentPostulaciones)
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ft.commit()
                }
            }
        })

    }

    private fun LoadDetalleOferta(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val ofertasService: OfertasService
        ofertasService = retrofit.create(OfertasService::class.java)

        val ofertaResponse = ofertasService.getOfertaById(idOferta)
        ofertaResponse.enqueue(object: Callback<OfertaResponse>{
            override fun onFailure(call: Call<OfertaResponse>, t: Throwable) {
                Log.d("Get OfertaById Fail", "Error: "+t.toString())

            }

            override fun onResponse(
                call: Call<OfertaResponse>,
                response: Response<OfertaResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("OfertaById Success", response.body()!!.toString())
                    val oferta: OfertaResponse = response.body()!!

                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val formatterfecha = SimpleDateFormat("d MMM yyyy")
                    val formatterdia = SimpleDateFormat("h:mm a")
                    val diaformateadoInicio = formatterfecha.format(parser.parse(oferta.fechaAtenciones[0].fecha)!!)
                    val diaformateadoFin = formatterfecha.format(parser.parse(oferta.fechaAtenciones[oferta.fechaAtenciones.size-1].fecha)!!)
                    val horaformateadaInicio = formatterdia.format(parser.parse("2020-01-01T"+oferta.fechaAtenciones[0].rangoHora.inicio)!!)
                    val horaformateadaFin = formatterdia.format(parser.parse("2020-01-01T"+oferta.fechaAtenciones[0].rangoHora.fin)!!)


                    tvNombreAbuelo.text = "${oferta.anciano.persona.nombre} ${oferta.anciano.persona.apellidos}"
                    val edad = 2020 - oferta.anciano.persona.fechaNacimiento.slice(IntRange(0,3)).toInt()
                    tvEdad.text = edad.toString()+" años"
                    tvFechaInicio.text = diaformateadoInicio.toString()
                    tvFechaFin.text = diaformateadoFin.toString()

                    tvRangoHoras.text = "${horaformateadaInicio} - ${horaformateadaFin}"
                    tvDireccion.text = oferta.direccion
                    tvDescripcion.text = oferta.descripcion

                    val picBuilder = Picasso.Builder(context)
                    picBuilder.downloader(OkHttp3Downloader(context))
                    picBuilder.build().load(oferta.anciano.persona.foto)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(ivAbuelo)
                }
            }
        })
    }
}
