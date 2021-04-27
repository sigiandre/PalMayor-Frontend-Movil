package com.palmayor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.palmayor.models.entities.*

@Database(
    entities = [Usuario::class,
        Rol::class,
        Persona::class,
        Familiar::class,
        Anciano::class],
    version = 1)
abstract class PalMayorDB:RoomDatabase() {
    abstract fun getUsuarioDAO(): UsuarioDAO
    abstract fun getFamiliarDAO(): FamiliarDAO
    abstract fun getPersonaDAO(): PersonaDAO

    companion object{
        private var INSTANCE: PalMayorDB? = null

        fun getInstance(context: Context): PalMayorDB{
            if(INSTANCE == null){
                INSTANCE = Room
                    .databaseBuilder(context,PalMayorDB::class.java,"palmayor.db")
                    .allowMainThreadQueries()
                    .build()
            }

            return INSTANCE as PalMayorDB
        }
    }
}