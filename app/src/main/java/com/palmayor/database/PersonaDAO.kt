package com.palmayor.database

import androidx.room.*
import com.palmayor.models.entities.Familiar
import com.palmayor.models.entities.Persona

@Dao
interface PersonaDAO {
    @Insert
    fun insertPersona(vararg persona: Persona)

    @Query("SELECT * FROM personas")
    fun getAllPersonas(): MutableList<Persona>

    @Query("SELECT * FROM personas WHERE id = :id")
    fun getPersonaById(id: Int): Persona

    @Delete
    fun deletePersona(vararg persona: Persona)

    @Update
    fun updatePersona(vararg persona: Persona)
}