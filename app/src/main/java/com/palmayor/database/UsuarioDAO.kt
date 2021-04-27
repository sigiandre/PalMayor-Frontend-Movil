package com.palmayor.database

import androidx.room.*
import com.palmayor.models.entities.Usuario
import com.palmayor.models.response.UsuarioResponse

@Dao
interface UsuarioDAO {

    @Insert
    fun insertUsuario(vararg usuario: Usuario)

    @Query("SELECT * FROM usuarios")
    fun getAllUsuarios(): MutableList<Usuario>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun getUsuarioById(id: Int): Usuario

    @Delete
    fun deleteUsuario(vararg usuario: Usuario)

    @Update
    fun updateUsuario(vararg  usuario: Usuario)
}