package com.palmayor.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager

import com.palmayor.R
import com.palmayor.activities.MainActivity
import com.palmayor.adapters.ListaAbuelosAdapter
import com.palmayor.models.entities.FechaAtencion
import com.palmayor.models.request.OfertaRequest
import com.palmayor.models.request.RangoHoraRequest
import com.palmayor.models.response.AncianoResponse
import com.palmayor.models.response.OfertaResponse
import com.palmayor.models.response.RangoHoraResponse
import com.palmayor.network.AncianoService
import com.palmayor.network.OfertasService
import com.palmayor.network.RangoHorasService
import kotlinx.android.synthetic.main.fragment_registrar_oferta.*
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class RegistrarOferta(correoFamiliar: String) : Fragment() {

    var correo = correoFamiliar
    lateinit var abuelos: List<AncianoResponse>
    lateinit var rangoHoras: List<RangoHoraResponse>
    lateinit var ctxt: Context
    lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrar_oferta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ctxt = view.context
        calendar = Calendar.getInstance()
        val canyo = calendar.get(Calendar.YEAR)
        val cmes = calendar.get(Calendar.MONTH)
        val cdia = calendar.get(Calendar.DAY_OF_MONTH)

        etFechaOfertaInicio.setOnClickListener {
            val datepicker = DatePickerDialog(view.context, DatePickerDialog.OnDateSetListener {
                    view, year, month, dayOfMonth -> etFechaOfertaInicio.setText(""+year+"-"+(month+1)+"-"+dayOfMonth)
            },canyo,cmes,cdia)
            datepicker.show()
        }

        etFechaOfertaFin.setOnClickListener {
            val datepicker = DatePickerDialog(view.context,DatePickerDialog.OnDateSetListener {
                    view, year, month, dayOfMonth -> etFechaOfertaFin.setText(""+year+"-"+(month+1)+"-"+dayOfMonth)
            },canyo,cmes,cdia)
            datepicker.show()
        }

        loadAbuelos(view.context)
        loadRangoHoras(view.context)
        btnAceptarAgregarOferta.setOnClickListener {
            continuar()
        }
    }

    private fun loadAbuelos(context: Context) {

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
                        abuelos = response.body()!! ?: ArrayList()
                        var abuelosString = arrayListOf<String>()
                        abuelos.forEach {
                            abuelosString.add("${it.persona.nombre} ${it.persona.apellidos}")
                        }

                        var adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                            context,
                            R.layout.dropdown_menu_popup_item,
                            abuelosString.toList()
                        )
                        dropdownAbuelosOferta.setAdapter(adapter)

                    } else {
                        Log.d("Error Lista abuelos: ", response.body()!!.toString())
                    }
                }

            })
        }
    }

    private fun loadRangoHoras(context: Context) {

        val horas = (1..12).toList()
        val periodos = arrayListOf<String>("AM","PM")

        var horasInicioString = arrayListOf<String>()
        horas.forEach {
            horasInicioString.add(it.toString())
        }
        var adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context,
            R.layout.dropdown_menu_popup_item,
            horasInicioString.toList()
        )
        dropdownHorariosOfertaInicio.setAdapter(adapter)


        var horasFinString = arrayListOf<String>()
        horas.forEach {
            horasFinString.add(it.toString())
        }
        var adapter2: ArrayAdapter<String> = ArrayAdapter<String>(
            context,
            R.layout.dropdown_menu_popup_item,
            horasFinString.toList()
        )
        dropdownHorariosOfertaFin.setAdapter(adapter2)

        var adapter3: ArrayAdapter<String> = ArrayAdapter<String>(
            context,
            R.layout.dropdown_menu_popup_item,
            periodos.toList()
        )
        dropdownPeriodoInicio.setAdapter(adapter3)

        var adapter4: ArrayAdapter<String> = ArrayAdapter<String>(
            context,
            R.layout.dropdown_menu_popup_item,
            periodos.toList()
        )
        dropdownPeriodoFin.setAdapter(adapter4)
    }

    private fun continuar() {
        var errors: Int = 0
        if (etDireccionOferta.text.isNullOrBlank()) {
            errors++
            etDireccionOferta.setError("Ingrese la dirección")
        }
        if (etFechaOfertaInicio.text.isNullOrBlank()) {
            errors++
            etFechaOfertaInicio.setError("Ingrese la fecha de inicio")
        }
        if(etFechaOfertaFin.text.isNullOrBlank()){
            errors++
            etFechaOfertaFin.setError("Ingrese la fecha de término")
        }
        if (dropdownAbuelosOferta.text.isNullOrBlank()) {
            errors++
            dropdownAbuelosOferta.setError("Seleccione el adulto mayor")
        }
        if (dropdownHorariosOfertaInicio.text.isNullOrBlank()) {
            errors++
            dropdownHorariosOfertaInicio.setError("Seleccione el horario")
        }
        if(dropdownHorariosOfertaFin.text.isNullOrBlank()){
            errors++
            dropdownHorariosOfertaFin.setError("Seleccione el horario")
        }
        if(dropdownPeriodoInicio.text.isNullOrBlank()){
            errors++
            dropdownPeriodoInicio.setError("Seleccione una opción")
        }
        if(dropdownPeriodoFin.text.isNullOrBlank()){
            errors++
            dropdownPeriodoFin.setError("Seleccione una opción")
        }
        if(etDireccionOferta.text.isNullOrBlank()){
            errors++
            etDireccionOferta.setError("Ingrese una dirección")
        }
        if(errors == 0){
            registrarOferta()
        }
    }

    private fun registrarOferta() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val rangoHoraService = retrofit.create(RangoHorasService::class.java)
        val ofertaService = retrofit.create(OfertasService::class.java)

        val idAnciano = (abuelos.filter { a -> ("${a.persona.nombre} ${a.persona.apellidos}".equals(dropdownAbuelosOferta.text.toString())) })[0].id

        val rangohora: RangoHoraRequest = obtenerHoras()
        var rhId = 0

        CoroutineScope(Dispatchers.IO).launch {
            val rangoHoraResponse = rangoHoraService.postRangoHoras(rangohora)
            withContext(Dispatchers.Main){
                try {
                    if(rangoHoraResponse.isSuccessful){
                        Log.d("Post rangohora Succes", rangoHoraResponse.body()!!.toString())
                        rhId = rangoHoraResponse.body()!!.id
                    }
                    else{
                        Log.d("Post Fail", "Error")
                    }
                }
                catch (e: HttpException){
                    Log.d("Post Fail", "Error: "+e.toString())
                }
                catch (e: Throwable){
                    Log.d("Post Fail", "Error: "+e.toString())
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            var descp: String? = null
            if(!etDescripcionOferta.text.isNullOrBlank()){
                descp = etDescripcionOferta.text.toString()
            }
            val oferta: OfertaRequest = OfertaRequest(
                direccion = etDireccionOferta.text.toString(),
                descripcion = descp,
                ancianoId =  idAnciano,
                fechaAtenciones = listOf(FechaAtencion(fecha = etFechaOfertaInicio.text.toString(), rangoHoraId = rhId)
                                        ,FechaAtencion(fecha = etFechaOfertaFin.text.toString(), rangoHoraId = rhId))
            )
            val ofertaResponse = ofertaService.postOferta(oferta)
            withContext(Dispatchers.Main){
                try {
                    if(ofertaResponse.isSuccessful){
                        Log.d("Post Oferta Success", ofertaResponse.body()!!.toString())
                        Toast.makeText(ctxt,"Oferta publicada exitosamente",Toast.LENGTH_LONG).show()
                        //(activity as MainActivity).loadFragment(OfertasFragment(correo))
                        (activity as MainActivity).loadFragment(OfertasFragment(correo))
                    }
                    else{
                        Log.d("Post Fail", "Error")
                    }
                }
                catch (e: HttpException){
                    Log.d("Post Fail", "Error: "+e.toString())
                }
                catch (e: Throwable){
                    Log.d("Post Fail", "Error: "+e.toString())
                }
            }

        }
    }

    private fun obtenerHoras(): RangoHoraRequest{
        var inicio: String = ""
        var fin: String = ""
        if(dropdownPeriodoInicio.text.toString().toLowerCase() == "am"){
            inicio = dropdownHorariosOfertaInicio.text.toString()+":00"
        }
        if(dropdownPeriodoInicio.text.toString().toLowerCase() == "pm"){
            if(dropdownHorariosOfertaInicio.text.toString().toInt() == 12){
                inicio = dropdownHorariosOfertaInicio.text.toString()+":00"
            }else{
                val h = dropdownHorariosOfertaInicio.text.toString().toInt() + 12
                inicio = h.toString()+":00"
            }
        }

        if(dropdownPeriodoFin.text.toString().toLowerCase() == "am"){
            fin = dropdownHorariosOfertaFin.text.toString()+":00"
        }
        if(dropdownPeriodoFin.text.toString().toLowerCase() == "pm"){
            if(dropdownHorariosOfertaFin.text.toString().toInt() == 12){
                fin = dropdownHorariosOfertaFin.text.toString()+":00"
            }else{
                val h = dropdownHorariosOfertaFin.text.toString().toInt() + 12
                fin = h.toString()+":00"
            }
        }

        return RangoHoraRequest(inicio = inicio, fin = fin)
    }

}
