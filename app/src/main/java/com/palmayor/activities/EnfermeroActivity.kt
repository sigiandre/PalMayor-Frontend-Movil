package com.palmayor.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.palmayor.R
import com.palmayor.fragments.EnfermeroHomeFragment
import com.palmayor.fragments.EnfermeroPostulacionesFragment
import com.palmayor.fragments.EnfermeroServiciosFragment
import kotlinx.android.synthetic.main.activity_enfermero.*

class EnfermeroActivity : AppCompatActivity() {

    lateinit var correo: String
    lateinit var ctxt: Context
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        item -> navigateTo(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enfermero)

        ctxt = this
        correo = intent.getStringExtra("correo")

        loadFragment(EnfermeroHomeFragment(correo))

        bottomEnfermeroNavigationBar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        //agarro el menu layout y lo meto al menu que es parametro de entrada
        inflater.inflate(R.menu.menu_cerrar_sesion  , menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_exit){
            val intent = Intent(ctxt, LoginActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun loadFragment(fragment: Fragment): Boolean {
        if(fragment != null){
            supportFragmentManager.beginTransaction().replace(R.id.fragmentEnfermeroContainer,fragment).commit()
            return true
        }
        return false
    }

    private fun navigateTo(item: MenuItem): Boolean{
        item.isChecked = true
        return supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentEnfermeroContainer, getFragmentFor(item))
            .commit() > 0
    }

    private fun getFragmentFor(item: MenuItem): Fragment {
        return when(item.itemId){
            R.id.homeEnfermero -> EnfermeroHomeFragment(intent.getStringExtra("correo").toString())
            R.id.serviciosEnfermero -> EnfermeroServiciosFragment(intent.getStringExtra("correo").toString())
            R.id.postulacionesEnfermero -> EnfermeroPostulacionesFragment(intent.getStringExtra("correo").toString())
            else -> EnfermeroHomeFragment(intent.getStringExtra("correo").toString())
        }
    }
}
