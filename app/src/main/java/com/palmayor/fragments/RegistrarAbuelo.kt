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
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputEditText
import com.palmayor.R
import com.palmayor.models.request.AncianoABVCRequest
import com.palmayor.models.request.AncianoRequest
import com.palmayor.models.request.PersonaRequest
import com.palmayor.models.response.ABVCResponse
import com.palmayor.models.response.AncianoResponse
import com.palmayor.models.response.FamiliarResponse
import com.palmayor.network.AbvcService
import com.palmayor.network.AncianoService
import com.palmayor.network.FamiliarService
import kotlinx.android.synthetic.main.fragment_registrar_abuelo.*
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class RegistrarAbuelo(correo : String) : Fragment() {
    //
    //var ctx : Context
    //
    lateinit var abvcs: List<ABVCResponse>
    lateinit var etNombresAbuelo : TextInputEditText
    lateinit var etApellidosAbuelo : TextInputEditText
    lateinit var etFechaAbuelo  :TextInputEditText
    lateinit var etDniAbuelo : TextInputEditText
    lateinit var radioGeneroAbuelo : RadioGroup
    var sexo = ""
    lateinit var calendar : Calendar

    var abvcsRequest: ArrayList<AncianoABVCRequest> = ArrayList()
    var correofamiliar = correo
    var familiarId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registrar_abuelo, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /////incializamos la variables
        //ctx = wiew.context

        etNombresAbuelo = view.findViewById(R.id.etNombresAbuelo)
        etApellidosAbuelo = view.findViewById(R.id.etApellidosAbuelo)
        etFechaAbuelo = view.findViewById(R.id.etFechaAbuelo)
        etDniAbuelo = view.findViewById(R.id.etDniAbuelo)
        radioGeneroAbuelo = view.findViewById(R.id.radioGeneroAbuelo)


        calendar = Calendar.getInstance()
        val canyo = calendar.get(Calendar.YEAR)
        val cmes = calendar.get(Calendar.MONTH)
        val cdia = calendar.get(Calendar.DAY_OF_MONTH)

        etFechaAbuelo.setOnClickListener{
            val dp = DatePickerDialog(view.context, DatePickerDialog.OnDateSetListener{
                view,year,moth,dayOfMoth ->etFechaAbuelo.setText(""+year+"-"+(moth+1)+"-"+dayOfMoth)
            },canyo,cmes,cdia)
            dp.show()
        }

        radioGeneroAbuelo.setOnCheckedChangeListener{ group, checkdId->
            if(checkdId == R.id.radioGeneroMasculino){sexo = "M"}
            if(checkdId == R.id.radioGeneroFemenino){sexo = "F"}
        }

        loadAbvcs(view.context)
        loadFamiliar(view.context)

        btnRegistrarAbuelo.setOnClickListener {
            validarCampos()
        }

    }

    private fun loadFamiliar(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        ////otenermos familiar
        val familiarService = retrofit.create(FamiliarService::class.java)
        val familiar = familiarService.getFamiliar(correofamiliar)
        familiar.enqueue(object : Callback<FamiliarResponse>{
            override fun onFailure(call: Call<FamiliarResponse>, t: Throwable) {
                Log.d("Error: no entró", t.toString())
            }

            override fun onResponse(call: Call<FamiliarResponse>,response: Response<FamiliarResponse>) {
                if(response.isSuccessful){
                    Log.d("familiar del abuelo: ", response.body()!!.toString())
                    familiarId = response.body()!!.id
                }
                else{
                    Log.d("No se puedo obetner: ", response.body()!!.toString())
                }
            }
        })
    }


    private fun loadAbvcs(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val abvcService = retrofit.create(AbvcService::class.java)

        val abvcResponse = abvcService.getAbvcs()
        abvcResponse.enqueue(object: Callback<List<ABVCResponse>>{
            override fun onFailure(call: Call<List<ABVCResponse>>, t: Throwable) {
                Log.d("Error: ", t.toString())
            }

            override fun onResponse(
                call: Call<List<ABVCResponse>>,
                response: Response<List<ABVCResponse>>
            ) {
                if(response.isSuccessful){
                    Log.d("Lista abcvs: ",response.body()!!.toString())
                    abvcs = response.body()!! ?: ArrayList()

                    //medicamentos
                    var medicString = arrayListOf<String>()
                    abvcs.forEach { if(it.tipoId == 7) medicString.add(it.descripcion) }
                    var adapter: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.dropdown_menu_popup_item, medicString.toList())
                    dropdownMedicamentos.setAdapter(adapter)

                    //dispositivos especiales
                    var dispositivoString = arrayListOf<String>()
                    abvcs.forEach { if(it.tipoId == 8) dispositivoString.add(it.descripcion) }
                    var adapter2: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.dropdown_menu_popup_item, dispositivoString.toList())
                    dropdownDispositivos.setAdapter(adapter2)

                    //baño
                    var banyoString = arrayListOf<String>()
                    abvcs.forEach { if(it.tipoId == 1) banyoString.add(it.descripcion) }
                    var adapter3: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.dropdown_menu_popup_item, banyoString)
                    dropdownBanyo.setAdapter(adapter3)

                    //vestido
                    var vestidoString = arrayListOf<String>()
                    abvcs.forEach { if(it.tipoId == 2) vestidoString.add(it.descripcion) }
                    var adapter4: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.dropdown_menu_popup_item, vestidoString.toList())
                    dropdownVestido.setAdapter(adapter4)

                    //uso del baño
                    var usobanyoString = arrayListOf<String>()
                    abvcs.forEach { if(it.tipoId == 3) usobanyoString.add(it.descripcion) }
                    var adapter5: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.dropdown_menu_popup_item, usobanyoString.toList())
                    dropdownUsoBanyo.setAdapter(adapter5)

                    //movilidad
                    var movilidadString = arrayListOf<String>()
                    abvcs.forEach { if(it.tipoId == 4) movilidadString.add(it.descripcion) }
                    var adapter6: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.dropdown_menu_popup_item, movilidadString.toList())
                    dropdownMovilidad.setAdapter(adapter6)

                    //continencia
                    var continenciaString = arrayListOf<String>()
                    abvcs.forEach { if(it.tipoId == 5) continenciaString.add(it.descripcion) }
                    var adapter7: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.dropdown_menu_popup_item, continenciaString.toList())
                    dropdownContinencia.setAdapter(adapter7)

                    //alimentacion
                    var alimentacionString = arrayListOf<String>()
                    abvcs.forEach { if(it.tipoId == 6) alimentacionString.add(it.descripcion) }
                    var adapter8: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.dropdown_menu_popup_item, alimentacionString.toList())
                    dropdownAlimentacion.setAdapter(adapter8)
                }
                else {
                    Log.d("Error abvcs: ", response.body()!!.toString())
                }
            }
        })
    }

    private fun validarCampos(){
        var errors: Int = 0
        if(etNombresAbuelo.text.isNullOrBlank()){
            errors++
            etNombresAbuelo.setError("Ingrese los nombres")
        }
        if (etApellidosAbuelo.text.isNullOrBlank()){
            errors++
            etApellidosAbuelo.setError("Ingrese los apellidos")
        }
        if(etFechaAbuelo.text.isNullOrBlank()){
            errors++
            etFechaAbuelo.setError("Ingrese la fecha de naciemiento")
        }
        if(etDniAbuelo.text.isNullOrBlank()){
            errors++
            etDniAbuelo.setError("Ingrese el número de DNI")
        }
        if(etDniAbuelo.text.toString().length != 8){
            errors++
            etDniAbuelo.setError("DNI incorrecto")
        }
        if(errors == 0){
            registrarAbuelo()
        }
        if(errors > 0){
            Toast.makeText(context,"Complete los campos requeridos", Toast.LENGTH_LONG).show()
        }

    }

    private fun registrarAbuelo() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val ancianoService = retrofit.create(AncianoService::class.java)

        //obteniendo lo valores para PERSONA del ABUELO
        val nombres = etNombresAbuelo.text.toString()
        val apellidos = etApellidosAbuelo.text.toString()
        val fecha = etFechaAbuelo.text.toString()
        val dni = etDniAbuelo.text.toString()
        val telefono = null
        var foto = ""
        if( sexo == "F"){
            foto = "https://i.imgur.com/8StYtpu.jpg"
        }
        if(sexo == "M"){
            foto = "https://img.freepik.com/foto-gratis/sonriente-hombre-mayor_102671-1197.jpg?size=626&ext=jpg"
        }
        abvcsRequest = actividadesSeleccionadas()
        Log.d("ABVCSSELECC",abvcsRequest.toString())

        val persona: PersonaRequest = PersonaRequest(nombres,apellidos,fecha,dni,sexo,telefono,foto)
        val abuelo: AncianoRequest = AncianoRequest(familiarId,persona,abvcsRequest)

        val responseAbuelo = ancianoService.postAnciano(abuelo)
        responseAbuelo.enqueue(object: Callback<AncianoResponse>{
            override fun onFailure(call: Call<AncianoResponse>, t: Throwable) {
                Log.d("Error: ",t.toString())
            }

            override fun onResponse(
                call: Call<AncianoResponse>,
                response: Response<AncianoResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("Post Success: ",response.body()!!.toString())
                    var fm= fragmentManager
                    var fragmentAbuelos = ListaAbuelosFragment(correofamiliar)

                    var ft = fm!!.beginTransaction()
                    ft.addToBackStack(null)
                    ft.replace(R.id.fragmentContainer,fragmentAbuelos)
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ft.commit()
                }else{
                    Log.d("Post Fail: ", response.errorBody()!!.toString() )
                }
            }
        })
    }

    private fun actividadesSeleccionadas(): ArrayList<AncianoABVCRequest> {
        var arr: ArrayList<AncianoABVCRequest> = ArrayList()
        if(!dropdownMedicamentos.text.isNullOrBlank()){
            val idMedicamentos = (abvcs.filter { a -> (a.descripcion.equals(dropdownMedicamentos.text.toString())) })[0].id
            arr.add(AncianoABVCRequest(idMedicamentos))
        }
        if(!dropdownDispositivos.text.isNullOrBlank()){
            val idDispositivos = (abvcs.filter { a -> (a.descripcion.equals(dropdownDispositivos.text.toString())) })[0].id
            arr.add(AncianoABVCRequest(idDispositivos))
        }
        if(!dropdownBanyo.text.isNullOrBlank()){
            val idBanyo = (abvcs.filter { a -> (a.descripcion.equals(dropdownBanyo.text.toString()))})[0].id
            arr.add(AncianoABVCRequest(idBanyo))
        }
        if(!dropdownVestido.text.isNullOrBlank()){
            val idVestido = (abvcs.filter { a -> (a.descripcion.equals(dropdownVestido.text.toString())) })[0].id
            arr.add(AncianoABVCRequest(idVestido))
        }
        if(!dropdownUsoBanyo.text.isNullOrBlank()){
            val idUso = (abvcs.filter { a -> (a.descripcion.equals(dropdownUsoBanyo.text.toString())) })[0].id
            arr.add(AncianoABVCRequest(idUso))
        }
        if(!dropdownMovilidad.text.isNullOrBlank()){
            val idMovilidad = (abvcs.filter { a -> (a.descripcion.equals(dropdownMovilidad.text.toString())) })[0].id
            arr.add(AncianoABVCRequest(idMovilidad))
        }
        if(!dropdownContinencia.text.isNullOrBlank()){
            val idContinencia = (abvcs.filter { a -> (a.descripcion.equals(dropdownContinencia.text.toString())) })[0].id
            arr.add(AncianoABVCRequest(idContinencia))
        }
        if(!dropdownAlimentacion.text.isNullOrBlank()){
            val idAlimentacion = (abvcs.filter { a -> (a.descripcion.equals(dropdownAlimentacion.text.toString())) })[0].id
            arr.add(AncianoABVCRequest(idAlimentacion))
        }
        return arr
    }

}