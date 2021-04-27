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

import com.palmayor.R
import com.palmayor.activities.MainActivity
import com.palmayor.adapters.OfertasAdapter
import com.palmayor.models.response.OfertaResponse
import com.palmayor.network.OfertasService
import kotlinx.android.synthetic.main.fragment_ofertas.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class OfertasFragment(correoFamiliar: String) : Fragment(), OfertasAdapter.OnItemClickListener {

    var correo = correoFamiliar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ofertas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("asd", "asdasd")
        loadOfertas(correo, view.context)

        btnAgregarOferta.setOnClickListener {
            (activity as MainActivity).loadFragment(RegistrarOferta(correo))
        }

    }

    private fun loadOfertas(correo: String, context: Context) {
        correo.let {
            Log.d("asd", "asdasd")
            if (it.isNotEmpty()) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://apivp.azurewebsites.net/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(OfertasService::class.java)

                val ofertasResponse = service.getOfertasByEmail(it)

                ofertasResponse.enqueue(object : Callback<List<OfertaResponse>> {
                    override fun onFailure(call: Call<List<OfertaResponse>>, t: Throwable) {
                        Log.d("ERROR:", t.toString())
                    }

                    override fun onResponse(
                        call: Call<List<OfertaResponse>>,
                        response: Response<List<OfertaResponse>>
                    ) {
                        if (response.isSuccessful) {
                            val ofertas: List<OfertaResponse> = response.body()!! ?: ArrayList()
                            recyclerOfertas.layoutManager = LinearLayoutManager(context)
                            recyclerOfertas.adapter = OfertasAdapter(ofertas, context, this@OfertasFragment)
                        } else {
                            Log.d("Error Ofertas: ", response.message())
                        }
                    }

                })
            }
        }
    }

    override fun onItemClicked(idOferta: Int) {
        var fm = fragmentManager
        var fragListaPostulantes = ListaPostulantesFragment(idOferta,correo)
        var ft = fm!!.beginTransaction()
        ft.addToBackStack(null)
        ft.replace(R.id.fragmentContainer,fragListaPostulantes).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

}
