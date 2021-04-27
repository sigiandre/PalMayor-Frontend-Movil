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
import com.palmayor.adapters.ServicioEnfermeroAdapter
import com.palmayor.models.response.ServicioResponse
import com.palmayor.network.ServicioService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class EnfermeroServiciosFragment(correoEnfermero: String): Fragment(),
ServicioEnfermeroAdapter.OnItemClickListener {
    var correo = correoEnfermero
    lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enfermero_servicios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.rvEnfServicios)
        loadListServicios(correo, view.context)
    }

    private fun loadListServicios(correo: String, context: Context ) {
        if(correo != null){
            if( correo.isNotEmpty() ){
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://apivp.azurewebsites.net/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(ServicioService::class.java)

                val serviciosResponse = service.getServicioByEnfermeroCorreo(correo)
                serviciosResponse.enqueue(object : Callback<List<ServicioResponse>>{
                    override fun onFailure(call: Call<List<ServicioResponse>>, t: Throwable) {
                        Log.d("Error: ", t.toString())
                    }

                    override fun onResponse(call: Call<List<ServicioResponse>>,
                        response: Response<List<ServicioResponse>>
                    ) {
                        if(response.isSuccessful){
                            Log.d("Lista de servicios: ", response.body()!!.toString())
                            val servicios: List<ServicioResponse> = response.body()!! ?: ArrayList()
                            rv.layoutManager = LinearLayoutManager(context)
                            rv.adapter = ServicioEnfermeroAdapter(servicios, context, this@EnfermeroServiciosFragment)
                        }
                        else{
                            Log.d("Error de servicios: ", response.body()!!.toString())
                        }
                    }
                })
            }
        }
    }

    override fun onItemClicked(servicioId: Int) {
        var fm = fragmentManager
        var fragDetalle = DetalleServicioEnfFragment(servicioId)
        var ft = fm!!.beginTransaction()
        ft.addToBackStack(null)
        ft.replace(R.id.fragmentEnfermeroContainer, fragDetalle)
        ft.commit()
    }


}