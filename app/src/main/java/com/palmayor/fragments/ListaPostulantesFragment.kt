package com.palmayor.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.palmayor.R
import com.palmayor.adapters.PostulanteAdapter
import com.palmayor.models.response.EnfermeroOfertaResponse
import com.palmayor.models.response.EnfermeroResponse
import com.palmayor.network.EnfermeroOfertaService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListaPostulantesFragment(idOferta: Int, correoFamiliar: String) : Fragment(), PostulanteAdapter.OnItemClickListener {

    var idOferta = idOferta
    var correo = correoFamiliar
    lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_postulantes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.rvPostulantes)
        loadPostulantes(idOferta, view.context)
    }

    private fun loadPostulantes(id: Int, context: Context){
        val retrofit = Retrofit.Builder()
                                .baseUrl("https://apivp.azurewebsites.net/api/v1/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

        val service = retrofit.create(EnfermeroOfertaService::class.java)

        val enfermeroOfertaResponse = service.getEnfermeroOfertasByOfertaId(id)
        enfermeroOfertaResponse.enqueue(object: Callback<List<EnfermeroOfertaResponse>>{
            override fun onFailure(call: Call<List<EnfermeroOfertaResponse>>, t: Throwable) {
                Log.d("Error: ", t.toString())
            }

            override fun onResponse(
                call: Call<List<EnfermeroOfertaResponse>>,
                response: Response<List<EnfermeroOfertaResponse>>
            ) {
                if(response.isSuccessful){
                    val postulantes: List<EnfermeroOfertaResponse> = response.body()!! ?: ArrayList()
                    recycler.layoutManager = LinearLayoutManager(context)
                    recycler.adapter = PostulanteAdapter(postulantes, context, this@ListaPostulantesFragment)
                }
                else{
                    Log.d("Error: ", response.errorBody().toString())
                }
            }

        })
    }

    override fun onItemClicked(enfermero: EnfermeroResponse){
        var fm = fragmentManager
        var fragDetallePostulante = DetalleEnfermeroPostulanteFragment(enfermero,idOferta,correo)

        var ft = fm!!.beginTransaction()
        ft.addToBackStack(null)
        ft.replace(R.id.fragmentContainer,fragDetallePostulante).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }
}
