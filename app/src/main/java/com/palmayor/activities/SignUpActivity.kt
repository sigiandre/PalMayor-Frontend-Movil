package com.palmayor.activities

import android.app.DatePickerDialog
import android.app.Person
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.palmayor.R
import com.palmayor.database.PalMayorDB
import com.palmayor.models.entities.Familiar
import com.palmayor.models.entities.Persona
import com.palmayor.models.entities.Usuario
import com.palmayor.models.request.FamiliarRequest
import com.palmayor.models.request.PersonaRequest
import com.palmayor.models.request.UsuarioRequest
import com.palmayor.models.response.FamiliarResponse
import com.palmayor.models.response.UsuarioResponse
import com.palmayor.network.FamiliarService
import com.palmayor.network.UsuarioService
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {

    lateinit var ctxt: Context
    lateinit var etNombres: TextInputEditText
    lateinit var etApellidos: TextInputEditText
    lateinit var etFechaNacimiento: TextInputEditText
    lateinit var etDNI: TextInputEditText
    lateinit var radioSexo: RadioGroup
    lateinit var etCorreo: TextInputEditText
    lateinit var etContrasenya: TextInputEditText
    lateinit var calendar: Calendar
    val ROL_FAMILIAR = 2
    val ROL_ENFERMERO = 3
    var usId = 0
    var sexo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        ctxt = this
        etNombres = findViewById(R.id.etSignUpNames)
        etApellidos = findViewById(R.id.etSignUpLastNames)
        etFechaNacimiento = findViewById(R.id.etSignUpBirthDay)
        etDNI = findViewById(R.id.etSignUpDNI)
        radioSexo = findViewById(R.id.radioGrpSignUpGender)
        etCorreo = findViewById(R.id.etSignUpCorreo)
        etContrasenya = findViewById(R.id.etSignUpPassword)
        calendar = Calendar.getInstance()
        val canyo = calendar.get(Calendar.YEAR)
        val cmes = calendar.get(Calendar.MONTH)
        val cdia = calendar.get(Calendar.DAY_OF_MONTH)

        etFechaNacimiento.setOnClickListener{
            val datepicker = DatePickerDialog(this,DatePickerDialog.OnDateSetListener {
                    view, year, month, dayOfMonth -> etFechaNacimiento.setText(""+year+"-"+(month+1)+"-"+dayOfMonth)
            },canyo,cmes,cdia)
            datepicker.show()
        }

        radioSexo.setOnCheckedChangeListener{ group, checkedId ->
            if(checkedId == R.id.radioSignUpMale){
                sexo = "M"
            }

            if(checkedId == R.id.radioSignUpFemale){
                sexo = "F"
            }
        }

        btnSignUp.setOnClickListener {
            validarCampos()
        }
    }

    private fun validarCampos(){
        var error: Int = 0
        if(etNombres.text.isNullOrBlank()){
            error++
            etNombres.setError("Ingrese sus nombres")
        }
        if(etApellidos.text.isNullOrBlank()){
            error++
            etApellidos.setError("Ingrese sus apellidos")
        }
        if(etFechaNacimiento.text.isNullOrBlank()){
            error++
            etFechaNacimiento.setError("Ingrese una fecha de nacimiento válida")
        }
        if(etDNI.text.isNullOrBlank()){
            error++
            etDNI.setError("Ingrese su número de DNI")
        }
        if(etCorreo.text.isNullOrBlank()){
            error++
            etCorreo.setError("Ingrese su correo electrónico")
        }
        if(etContrasenya.text.isNullOrBlank()){
            error++
            etContrasenya.setError("Ingrese su contraseña")
        }
        if(etContrasenya.text.toString().length < 8){
            error++
            etContrasenya.setError("Mínimo 8 caracteres")
        }
        if(error == 0){
            crearUsuario()
        }
    }

    private fun crearUsuario(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val usuarioService: UsuarioService
        usuarioService = retrofit.create(UsuarioService::class.java)
        val familiarService: FamiliarService
        familiarService = retrofit.create(FamiliarService::class.java)

        val correo = etCorreo.text.toString().trim()
        val contrasenya = etContrasenya.text.toString()
        val nombres = etNombres.text.toString()
        val apellidos = etApellidos.text.toString()
        val fechaNacimiento = etFechaNacimiento.text.toString()
        val dni = etDNI.text.toString().trim()
        //val foto = "https://i.imgur.com/ELyEiFK.jpg" url abuelo
        val foto = "https://i.imgur.com/rGvneH6.jpg"
        val telefono = "942434529"

        val rolId = intent.getStringExtra("rol").toInt()

        if(rolId == ROL_FAMILIAR){
            CoroutineScope(Dispatchers.IO).launch {
                val usuario = UsuarioRequest(correo,contrasenya,rolId)
                val responseUsuario = usuarioService.postUsuario(usuario)
                withContext(Dispatchers.Main){
                    try {
                        if(responseUsuario.isSuccessful){
                            Log.d("Post Usuario Success", responseUsuario.body()!!.toString())
                            usId = responseUsuario.body()!!.id
                            val us = Usuario(responseUsuario.body()!!.id,responseUsuario.body()!!.correo,responseUsuario.body()!!.rolId)
                            PalMayorDB.getInstance(ctxt).getUsuarioDAO().insertUsuario(us)

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
                val persona = PersonaRequest(nombres,apellidos,fechaNacimiento,dni,sexo,telefono,foto)
                val familiar = FamiliarRequest(usId,persona)
                val responseFamiliar = familiarService.postFamiliar(familiar)
                withContext(Dispatchers.Main){
                    try{
                        if(responseFamiliar.isSuccessful){
                            Log.d("Post Familiar Success", responseFamiliar.body()!!.toString())
                            val fm = Familiar(responseFamiliar.body()!!.id,responseFamiliar.body()!!.usuarioId,responseFamiliar.body()!!.personaId)
                            val psn = Persona(
                                responseFamiliar.body()!!.persona!!.id,
                                responseFamiliar.body()!!.persona!!.nombre,
                                responseFamiliar.body()!!.persona!!.apellidos,
                                responseFamiliar.body()!!.persona!!.fechaNacimiento,
                                responseFamiliar.body()!!.persona!!.dni,
                                responseFamiliar.body()!!.persona!!.sexo,
                                responseFamiliar.body()!!.persona!!.telefono,
                                responseFamiliar.body()!!.persona!!.foto
                            )
                            PalMayorDB.getInstance(ctxt).getFamiliarDAO().insertFamiliar(fm)
                            PalMayorDB.getInstance(ctxt).getPersonaDAO().insertPersona(psn)

                            val intent = Intent(ctxt, WelcomeActivity::class.java).apply {
                                putExtra("correo",correo)
                            }
                            startActivity(intent)
                        }
                        else{
                            Log.d("Post Fail 1", "Error ${responseFamiliar.code()}")
                        }

                    }
                    catch (e: HttpException){
                        Log.d("Post Fail 2", "Error: "+e.toString())
                    }
                    catch (e: Throwable){
                        Log.d("Post Fail 2", "Error: "+e.toString())
                    }
                }
            }
        }

        if(rolId == ROL_ENFERMERO){
            val usuario = UsuarioRequest(correo,contrasenya,rolId)
            val persona = PersonaRequest(nombres,apellidos,fechaNacimiento,dni,sexo,telefono,foto)
            val intent = Intent(ctxt,SignUpEnfermeroActivity::class.java).apply {
                putExtra("usuario",usuario)
                putExtra("persona",persona)
            }
            startActivity(intent)
        }



    }
}
