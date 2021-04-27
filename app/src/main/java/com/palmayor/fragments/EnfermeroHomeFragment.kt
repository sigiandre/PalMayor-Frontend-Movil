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
import com.palmayor.adapters.OfertasAdapter
import com.palmayor.adapters.OfertasEnfermeroAdapter
import com.palmayor.models.response.EnfermeroResponse
import com.palmayor.models.response.OfertaResponse
import com.palmayor.network.EnfermeroService
import com.palmayor.network.OfertasService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class EnfermeroHomeFragment(correoEnfermero: String) : Fragment(), OfertasAdapter.OnItemClickListener {

    lateinit var ctxt: Context
    lateinit var recycleView: RecyclerView
    val correo = correoEnfermero
    var idEnfermero = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enfermero_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ctxt = view.context
        recycleView = view.findViewById(R.id.rvOfertasEnfermero)
        loadOfertas()
        getIdEnfermero(correo)
    }

    private fun loadOfertas(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val ofertaService: OfertasService = retrofit.create(OfertasService::class.java)

        val ofertaResponse = ofertaService.getOfertas()
        ofertaResponse.enqueue(object: Callback<List<OfertaResponse>>{
            override fun onFailure(call: Call<List<OfertaResponse>>, t: Throwable) {
                Log.d("Get Ofertas Fail", "Error: "+t.toString())

            }

            override fun onResponse(
                call: Call<List<OfertaResponse>>,
                response: Response<List<OfertaResponse>>
            ) {
                if(response.isSuccessful){
                    val ofertas: List<OfertaResponse> = response.body()!! ?: ArrayList()
                    recycleView.layoutManager = LinearLayoutManager(ctxt)
                    recycleView.adapter = OfertasAdapter(ofertas,ctxt,this@EnfermeroHomeFragment)
                }
            }
        })
    }

    private fun getIdEnfermero(correo: String){

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val enfermeroService: EnfermeroService = retrofit.create(EnfermeroService::class.java)

        val enfermeroResponse = enfermeroService.GetEnfermeroByCorreo(correo)
        enfermeroResponse.enqueue(object: Callback<EnfermeroResponse>{
            override fun onFailure(call: Call<EnfermeroResponse>, t: Throwable) {
                Log.d("Get Enfermero Fail", "Error: "+t.toString())
            }

            override fun onResponse(
                call: Call<EnfermeroResponse>,
                response: Response<EnfermeroResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("Get Enfermero Succes", response.body()!!.toString())
                    idEnfermero = response.body()!!.id
                }
            }
        })
    }

    override fun onItemClicked(idOferta: Int) {
        var fm = fragmentManager
        var fragmentDetalleOferta = DetalleOfertaEnfermeroFragment(idOferta,idEnfermero,correo)

        var ft = fm!!.beginTransaction()
        ft.addToBackStack(null)
        ft.replace(R.id.fragmentEnfermeroContainer, fragmentDetalleOferta)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }


}
