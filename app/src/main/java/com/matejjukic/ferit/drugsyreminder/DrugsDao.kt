package com.matejjukic.ferit.drugsyreminder

import androidx.room.*

@Dao
interface DrugsDao {
    @Query("SELECT * FROM Drugs")
    suspend fun getAll(): List<Drugs>

    @Query("SELECT id FROM Drugs")
    suspend fun getAllIds(): List<String>

    @Query("DELETE FROM Drugs")
    suspend fun removeAll()

    @Query("UPDATE Drugs SET hour=(hour+step)%24 WHERE id=:id")
    suspend fun updateHour(id: String)

    @Query("UPDATE Drugs SET Current = :state WHERE id=:id")
    suspend fun updateState(state: Boolean, id: String)

    @Query("SELECT * FROM Drugs WHERE Id = :id")
    suspend fun getDrug(id: String):Drugs

    @Query( "UPDATE Drugs SET Name=:name, Hour=:hour, Minute=:minute, Step=:step WHERE Id=:id")
    suspend fun updateDrug(id: String, name: String, hour: Int, minute: Int, step: Int)

    @Insert
    suspend fun insert(drugs: Drugs)

    @Delete
    suspend fun delete(drugs: Drugs)
}