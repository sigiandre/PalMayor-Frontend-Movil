package com.palmayor.database

import androidx.room.*
import com.palmayor.models.entities.Familiar
import com.palmayor.models.response.FamiliarResponse
import com.palmayor.models.response.UsuarioResponse

@Dao
interface FamiliarDAO {
    @Insert
    fun insertFamiliar(vararg familiar: Familiar)

    @Query("SELECT * FROM familiares")
    fun getAllFamiliares(): MutableList<Familiar>

    @Query("SELECT * FROM familiares WHERE id = :id")
    fun getFamiliarById(id: Int): Familiar

    @Delete
    fun deleteFamiliar(vararg familiar: Familiar)

    @Update
    fun updateFamiliar(vararg  familiar: Familiar)
}