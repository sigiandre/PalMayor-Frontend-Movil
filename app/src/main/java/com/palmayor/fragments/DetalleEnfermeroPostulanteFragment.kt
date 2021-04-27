package com.palmayor.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.palmayor.R
import com.palmayor.activities.MainActivity
import com.palmayor.models.request.ServicioRequest
import com.palmayor.models.response.EnfermeroResponse
import com.palmayor.models.response.ServicioResponse
import com.palmayor.network.EnfermeroOfertaService
import com.palmayor.network.ServicioService
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detalle_enfermero_postulante.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetalleEnfermeroPostulanteFragment(postulante:EnfermeroResponse, idOferta: Int, correoFamiliar: String) : Fragment() {

    lateinit var ivPostulante: ImageView
    lateinit var tvNombrePostulante: TextView
    lateinit var tvEstudios: TextView
    lateinit var tvGrado: TextView
    lateinit var tvEspecialidad: TextView
    lateinit var tvContacto: TextView
    lateinit var tvExperiencia: TextView
    lateinit var btnContrato: Button
    val enfermero: EnfermeroResponse = postulante
    val correo: String = correoFamiliar
    val idOferta = idOferta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_enfermero_postulante, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivPostulante = view.findViewById(R.id.ivEnfermeroDEP)
        tvNombrePostulante = view.findViewById(R.id.tvNombreEnfermeroDEP)
        tvEstudios = view.findViewById(R.id.tvCentroEstudiosDEP)
        tvGrado = view.findViewById(R.id.tvGradoDEP)
        tvEspecialidad = view.findViewById(R.id.tvEspecialidadDEP)
        tvContacto = view.findViewById(R.id.tvContactoDEP)
        tvExperiencia = view.findViewById(R.id.tvExperienciaDEP)
        btnContrato = view.findViewById(R.id.btnContratarDEP)

        loadDetallePostulante(view.context)

        btnContrato.setOnClickListener {
            contratarEnfermeroPostulante()
        }

    }

    private fun contratarEnfermeroPostulante() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ServicioService::class.java)

        val contrato = ServicioRequest(enfermeroId = enfermero.id, ofertaId = idOferta)

        val servicioResponse = service.postServicio(contrato)

        servicioResponse.enqueue(object: Callback<ServicioResponse>{
            override fun onFailure(call: Call<ServicioResponse>, t: Throwable) {
                Log.d("ERROR:", t.toString())
            }

            override fun onResponse(
                call: Call<ServicioResponse>,
                response: Response<ServicioResponse>
            ) {
                if(response.isSuccessful){
                    Toast.makeText(context, "Enfermero Contratado", Toast.LENGTH_LONG).show()
                    (activity as MainActivity).loadFragment(FamiliarHomeFragment(correo))
                }
            }
        })

    }

    private fun loadDetallePostulante(context: Context) {
        val pic = Picasso.Builder(context)
        pic.downloader(OkHttp3Downloader(context))
        pic.build().load(enfermero.persona.foto)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(ivEnfermeroDEP)

        tvNombrePostulante.text = enfermero.persona.nombre+" "+enfermero.persona.apellidos
        tvEstudios.text = enfermero.universidad
        tvGrado.text = enfermero.grado.nombre
        tvEspecialidad.text = enfermero.especialidad.nombre
        tvContacto.text = enfermero.persona.telefono
        tvExperiencia.text = enfermero.experiencia
    }
}
