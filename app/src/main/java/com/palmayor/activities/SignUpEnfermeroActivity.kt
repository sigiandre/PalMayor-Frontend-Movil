package com.palmayor.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.palmayor.R
import com.palmayor.database.PalMayorDB
import com.palmayor.models.entities.Usuario
import com.palmayor.models.request.EnfermeroRequest
import com.palmayor.models.request.PersonaRequest
import com.palmayor.models.request.UsuarioRequest
import com.palmayor.models.response.EspecialidadResponse
import com.palmayor.models.response.GradoResponse
import com.palmayor.network.EnfermeroService
import com.palmayor.network.EspecialidadService
import com.palmayor.network.GradoService
import com.palmayor.network.UsuarioService
import kotlinx.android.synthetic.main.activity_sign_up_enfermero.*
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SignUpEnfermeroActivity : AppCompatActivity() {

    lateinit var persona: PersonaRequest
    lateinit var usuario: UsuarioRequest
    lateinit var especialidades: List<EspecialidadResponse>
    lateinit var grados: List<GradoResponse>
    lateinit var etColegiatura: TextInputEditText
    lateinit var etCentroEstudios: TextInputEditText
    lateinit var etExperiencia: TextInputEditText
    lateinit var btnFinalizar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_enfermero)

        persona = intent.getSerializableExtra("persona") as PersonaRequest
        usuario = intent.getSerializableExtra("usuario") as UsuarioRequest
        etColegiatura = findViewById(R.id.etColegiatura)
        etCentroEstudios = findViewById(R.id.etCentroEstudios)
        btnFinalizar = findViewById(R.id.btnFinalizar)
        etExperiencia = findViewById(R.id.etExperienciaEnfermero)

        loadEspecialidades(this)
        loadGrados(this)

        btnFinalizar.setOnClickListener{
            validarCampos(this)
        }

    }

    private fun loadEspecialidades(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val especialidadService: EspecialidadService = retrofit.create(EspecialidadService::class.java)

        val especialidadResponse = especialidadService.getEspecialidades()
        especialidadResponse.enqueue(object: Callback<List<EspecialidadResponse>>{
            override fun onFailure(call: Call<List<EspecialidadResponse>>, t: Throwable) {
                Log.d("Error: ", t.toString())

            }

            override fun onResponse(
                call: Call<List<EspecialidadResponse>>,
                response: Response<List<EspecialidadResponse>>
            ) {
                if(response.isSuccessful){
                    Log.d("Lista especialidades: ", response.body()!!.toString())
                    especialidades = response.body()!! ?: ArrayList()
                    var especialidadesString = arrayListOf<String>()
                    especialidades.forEach {
                        especialidadesString.add(it.nombre)
                    }

                    var adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        context,
                        R.layout.dropdown_menu_popup_item,
                        especialidadesString.toList()
                    )

                    dropdownEspecialidades.setAdapter(adapter)
                }
            }
        })

    }

    private fun loadGrados(context: Context){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val gradoService: GradoService = retrofit.create(GradoService::class.java)

        val gradoResponse = gradoService.getGrados()
        gradoResponse.enqueue(object: Callback<List<GradoResponse>>{
            override fun onFailure(call: Call<List<GradoResponse>>, t: Throwable) {
                Log.d("Error: ", t.toString())
            }

            override fun onResponse(
                call: Call<List<GradoResponse>>,
                response: Response<List<GradoResponse>>
            ) {
                if(response.isSuccessful){
                    grados = response.body()!! ?: ArrayList()
                    var gradosString = arrayListOf<String>()
                    grados.forEach {
                        gradosString.add(it.nombre)
                    }

                    var adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        context,
                        R.layout.dropdown_menu_popup_item,
                        gradosString.toList()
                    )

                    dropdownGrados.setAdapter(adapter)
                }
            }
        })
    }

    private fun validarCampos(context: Context) {
        var error: Int = 0
        if(etColegiatura.text.isNullOrBlank()){
            error++
            etColegiatura.setError("Ingrese su código de Colegiatura")
        }
        if(etCentroEstudios.text.isNullOrBlank()){
            error++
            etCentroEstudios.setError("Ingrese el Centro de Estudios de egreso")
        }
        if(dropdownEspecialidades.text.isNullOrBlank()){
            error++
            dropdownEspecialidades.setError("Seleccione una especialidad")
        }
        if(dropdownGrados.text.isNullOrBlank()){
            error++
            dropdownGrados.setError("Seleccione un grado académico")
        }
        if(error == 0){
            registrarEnfermero(context)
        }
    }

    private fun registrarEnfermero(context: Context){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val usuarioService: UsuarioService = retrofit.create(UsuarioService::class.java)
        val enfermeroService: EnfermeroService = retrofit.create(EnfermeroService::class.java)

        var usuarioId = 0
        val colegiatura = etColegiatura.text.toString()
        val universidad = etCentroEstudios.text.toString()
        val especialidadId = (especialidades.filter { esp -> esp.nombre.equals(dropdownEspecialidades.text.toString())})[0].id
        val gradoId = (grados.filter { gr -> gr.nombre.equals(dropdownGrados.text.toString())})[0].id
        var experiencia: String?
        if(etExperiencia.text.isNullOrBlank()){
            experiencia = null
        }
        else{
            experiencia = etExperiencia.text.toString()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val usuarioResponse = usuarioService.postUsuario(usuario)
            withContext(Dispatchers.Main){
                try{
                    if(usuarioResponse.isSuccessful){
                        Log.d("Post Usuario Success", usuarioResponse.body()!!.toString())
                        usuarioId = usuarioResponse.body()!!.id
                        val us = Usuario(usuarioResponse.body()!!.id,usuarioResponse.body()!!.correo,usuarioResponse.body()!!.rolId)
                        PalMayorDB.getInstance(context).getUsuarioDAO().insertUsuario(us)
                    }
                    else{
                        Log.d("Activity Fail", "Error")
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
            delay(5000)
            val enfermero: EnfermeroRequest = EnfermeroRequest(colegiatura,universidad,experiencia,usuarioId,especialidadId,gradoId,persona)
            val enfermeroResponse = enfermeroService.postEnfermero(enfermero)
            withContext(Dispatchers.Main){
                try{
                    if(enfermeroResponse.isSuccessful){
                        Log.d("Post Enfermero Success", enfermeroResponse.body()!!.toString())
                        //
                        val intent = Intent(context,WelcomeActivity::class.java).apply {
                            putExtra("correo",usuario.correo)
                        }
                        startActivity(intent)
                    }
                    else{
                        Log.d("Post Fail", "Error ${enfermeroResponse.code()}")
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
}
