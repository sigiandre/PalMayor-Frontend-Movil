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
import com.palmayor.adapters.ListaAbuelosAdapter
import com.palmayor.models.response.AncianoResponse
import com.palmayor.models.response.RangoHoraResponse
import com.palmayor.network.AncianoService
import com.palmayor.network.RangoHorasService
import kotlinx.android.synthetic.main.fragment_lista_abuelos.*
import kotlinx.android.synthetic.main.fragment_registrar_abuelo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class ListaAbuelosFragment(correoFamiliar: String) : Fragment(),
    ListaAbuelosAdapter.OnItemClickListener {

    var correo = correoFamiliar
    lateinit var rv: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista_abuelos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.rvListAbuelos)
        loadListAbuelos(correo, view.context)
        btAddAbuelo.setOnClickListener {
            ///enviar informaciona al fragment REgsitrarAbuelo(CORREO)
            agregarAbuelo(correo)
        }
    }

    private fun agregarAbuelo(correo: String) {
        var fm= fragmentManager
        var fragRegistrarAbuelo = RegistrarAbuelo(correo)

        var ft = fm!!.beginTransaction()
        ft.addToBackStack(null)///para regresar a este fragment
        ft.replace(R.id.fragmentContainer,fragRegistrarAbuelo)
        ft.commit()
    }

    private fun loadListAbuelos(correo: String, context: Context) {
        if (correo != null) {
            if (correo.isNotEmpty()) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://apivp.azurewebsites.net/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(AncianoService::class.java)

                val abuelosResponse = service.getAncianos(correo)
                abuelosResponse.enqueue(object : Callback<List<AncianoResponse>> {
                    override fun onFailure(call: Call<List<AncianoResponse>>, t: Throwable) {
                        Log.d("Error: ", t.toString())
                    }

                    override fun onResponse(
                        call: Call<List<AncianoResponse>>,
                        response: Response<List<AncianoResponse>>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("Error Lista abuelos: ", response.body()!!.toString())
                            val abuelos: List<AncianoResponse> = response.body()!! ?: ArrayList()
                            rv.layoutManager = LinearLayoutManager(context)
                            rv.adapter =
                                ListaAbuelosAdapter(abuelos, context, this@ListaAbuelosFragment)
                        } else {
                            Log.d("Error Lista abuelos: ", response.body()!!.toString())
                        }
                    }

                })
            }
        }
    }



    override fun onItemClicked(anciano: AncianoResponse) {

        var fm = fragmentManager
        var fragDetalle = DetalleAbueloFragment(anciano)

        var ft = fm!!.beginTransaction()
        ft.addToBackStack(null)///para regresar a este fragment
        ft.replace(R.id.fragmentContainer, fragDetalle)
        ft.commit()
    }


}