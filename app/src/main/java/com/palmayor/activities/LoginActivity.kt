    package com.palmayor.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.palmayor.R
import com.palmayor.database.PalMayorDB
import com.palmayor.models.entities.Usuario
import com.palmayor.models.request.UsuarioRequest
import com.palmayor.models.response.UsuarioResponse
import com.palmayor.network.UsuarioService
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    lateinit var ctxt: Context
    lateinit var etCorreo: EditText
    lateinit var etContrasenya: EditText
    val SIN_ROL = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ctxt = this
        etCorreo = findViewById(R.id.etLoginCorreo)
        etContrasenya = findViewById(R.id.etLoginContrasenya)


        lblNewAccountLink.setOnClickListener { v ->
            val inceptionIntent = Intent(this, InceptionActivity::class.java)
            startActivity(inceptionIntent)
        }

        btnSignIn.setOnClickListener { v ->
            signIn()
        }
    }

    private fun signIn() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apivp.azurewebsites.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val usuarioService: UsuarioService
        usuarioService = retrofit.create(UsuarioService::class.java)

        val correo = etLoginCorreo.text.toString()
        val contrasenya = etLoginContrasenya.text.toString()
        val usuarioRequest = UsuarioRequest(correo,contrasenya,SIN_ROL)

        val responseUsuario = usuarioService.loginUsuario(usuarioRequest)
        responseUsuario.enqueue(object: Callback<UsuarioResponse>{
            override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                Log.d("Login Fail", "Error: "+t.toString())
                }

            override fun onResponse(
                call: Call<UsuarioResponse>,
                response: Response<UsuarioResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("Login Usuario Success", response.body()!!.toString())
                    val us = Usuario(response.body()!!.id,response.body()!!.correo,response.body()!!.rolId)
                    saveUsuario(us)
                    if(us.rolId == 2){
                        val mainIntent = Intent(ctxt, MainActivity::class.java).apply {
                            putExtra("correo", response.body()!!.correo)
                        }
                        startActivity(mainIntent)
                    }
                    if(us.rolId == 3){
                        val enfermeroIntent = Intent(ctxt,EnfermeroActivity::class.java).apply {
                            putExtra("correo", response.body()!!.correo)
                            putExtra("rol", response.body()!!.rolId)
                        }
                        startActivity(enfermeroIntent)
                    }
                }
                else{
                    Toast.makeText(ctxt, "Correo o contraseña incorrectas", Toast.LENGTH_LONG).show()
                }
            }
        })


    }

        private fun saveUsuario(usuario: Usuario?) {
            if(usuario != null){
                val exist = PalMayorDB.getInstance(ctxt).getUsuarioDAO().getUsuarioById(usuario.id)

                if( exist != null){
                    Log.d("Usuario Exist?", exist.toString())
                    PalMayorDB.getInstance(ctxt).getUsuarioDAO().updateUsuario(usuario)
                }
                else{
                    Log.d("Usuario Exist?", "Usuario Doesn't Exist")
                    PalMayorDB.getInstance(ctxt).getUsuarioDAO().insertUsuario(usuario)
                }
            }
        }
    }
