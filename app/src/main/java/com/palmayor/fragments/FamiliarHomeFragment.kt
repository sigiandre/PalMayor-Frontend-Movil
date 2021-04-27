package com.palmayor.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.palmayor.R
import com.palmayor.adapters.ServicioAdapter
import com.palmayor.models.response.FamiliarResponse
import com.palmayor.models.response.ServicioResponse
import com.palmayor.network.ServicioService
import kotlinx.android.synthetic.main.fragment_familiar_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FamiliarHomeFragment(correoFamiliar: String) : Fragment(), ServicioAdapter.OnItemClickListener {

    var correo = correoFamiliar
    lateinit var familiar: FamiliarResponse
    lateinit var servicios: List<ServicioResponse>
    lateinit var flHome: FrameLayout
    lateinit var recycleView: RecyclerView
    lateinit var tvAviso1: TextView
    lateinit var tvAviso2: TextView
    lateinit var ctxt: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_familiar_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ctxt = view.context
        flHome = view.findViewById(R.id.flHome)
        recycleView = view.findViewById(R.id.rvServicios)
        loadServicios(correo)
        fabHomeAgregar.setOnClickListener{
            nuevaOferta()
        }
    }

    private fun nuevaOferta() {
        var fm= fragmentManager
        var fragRegistrarOferta = RegistrarOferta(correo)

        var ft = fm!!.beginTransaction()
        ft.addToBackStack(null)///para regresar a este fragment
        ft.replace(R.id.fragmentContainer,fragRegistrarOferta)
        ft.commit()
    }

    private fun loadServicios(correo: String?){
        if(correo != null) {
            if(correo.isNotEmpty()) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://apivp.azurewebsites.net/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val servicioService: ServicioService
                servicioService = retrofit.create(ServicioService::class.java)

                val responseServicio = servicioService.getServiciosByFamiliarCorreo(correo)
                responseServicio.enqueue(object: Callback<List<ServicioResponse>>{
                    override fun onFailure(call: Call<List<ServicioResponse>>, t: Throwable) {
                        Log.d("Get Servicios Fail", "Error: "+t.toString())
                    }

                    override fun onResponse(
                        call: Call<List<ServicioResponse>>,
                        response: Response<List<ServicioResponse>>
                    ) {
                        if(response.isSuccessful){
                            Log.d("Get Servicios Success", response.body()!!.toString())
                            servicios = response.body()!! ?: ArrayList()
                            if(servicios.size > 0){
                                recycleView.layoutManager = LinearLayoutManager(ctxt)
                                recycleView.adapter = ServicioAdapter(servicios, ctxt, this@FamiliarHomeFragment )
                            }
                            else{
                                tvAviso1 = TextView(ctxt)
                                tvAviso1.textSize = 25f
                                tvAviso1.text = "Woops!"
                                tvAviso1.setPaddingRelative(450,550,200,0)
                                tvAviso2 = TextView(ctxt)
                                tvAviso2.textSize = 25f
                                tvAviso2.text = "No tienes ninguna oferta en servicio..."
                                tvAviso2.setPaddingRelative(190,650,150,0)
                                flHome.addView(tvAviso1)
                                flHome.addView(tvAviso2)
                            }
                        }
                    }
                })
            }
        }
    }

    override fun onItemClicked(id: Int) {
        var fm= fragmentManager
        var fragmentDetalleServicio = DetalleServicioFragment(id)

        var ft = fm!!.beginTransaction()
        ft.addToBackStack(null)
        ft.replace(R.id.fragmentContainer,fragmentDetalleServicio)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

}
