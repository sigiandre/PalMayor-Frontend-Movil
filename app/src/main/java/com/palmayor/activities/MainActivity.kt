package com.palmayor.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.palmayor.R
import com.palmayor.fragments.FamiliarHomeFragment
import com.palmayor.fragments.ListaAbuelosFragment
import com.palmayor.fragments.OfertasFragment

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var correo: String
    lateinit var ctxt: Context
    val REQUEST_CODE = 1
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener{
        item -> navigateTo(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ctxt = this
        correo = intent.getStringExtra("correo")
        loadFragment(FamiliarHomeFragment(correo))

        bottomNavigationBar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
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
        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment)
                .commit()
            return true
        }
        return false
    }

    private fun navigateTo(item: MenuItem): Boolean {
        item.isChecked = true
        return supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,getFragmentFor(item))
            .commit() > 0
    }

    private fun getFragmentFor(item: MenuItem): Fragment {
        return when(item.itemId){
            R.id.home -> FamiliarHomeFragment(intent.getStringExtra("correo").toString())
            R.id.elders->ListaAbuelosFragment(intent.getStringExtra("correo").toString())
            R.id.offers->OfertasFragment(intent.getStringExtra("correo").toString())
            else -> FamiliarHomeFragment(intent.getStringExtra("correo").toString())
        }
    }

}
