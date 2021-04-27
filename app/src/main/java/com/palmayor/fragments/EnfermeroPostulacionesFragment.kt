package com.palmayor.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.palmayor.R
import com.palmayor.adapters.EnfermeroOfertasAdapter
import com.palmayor.models.response.EnfermeroOfertaResponse
import com.palmayor.network.EnfermeroOfertaService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EnfermeroPostulacionesFragment(correoEnfermero: String) : Fragment() {

    var correo= correoEnfermero
    lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enfermero_postulaciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.rvPostulaciones)
        loadPostulaciones(correo, view.context)
    }

    private fun loadPostulaciones(correo: String, context: Context) {
        if(correo != null){
            if(correo.isNotEmpty()){
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://apivp.azurewebsites.net/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(EnfermeroOfertaService::class.java)
                val postulaciones = service.getEnfermeroOfertasByCorreo(correo)
                postulaciones.enqueue(object : Callback<List<EnfermeroOfertaResponse>>{
                    override fun onFailure(
                        call: Call<List<EnfermeroOfertaResponse>>,
                        t: Throwable
                    ) {
                        Log.d("Error en postualciones", t.toString())
                    }

                    override fun onResponse(
                        call: Call<List<EnfermeroOfertaResponse>>,
                        response: Response<List<EnfermeroOfertaResponse>>
                    ) {
                        if(response.isSuccessful){
                            Log.d("Lista de postualciones", response.body()!!.toString())
                            val postulaciones: List<EnfermeroOfertaResponse> = response.body()!!
                            rv.layoutManager = LinearLayoutManager(context)
                            rv.adapter = EnfermeroOfertasAdapter(postulaciones, context)
                        }
                        else{
                            Log.d("Error: ", response.body()!!.toString())
                        }
                    }
                })

            }
        }
    }
}