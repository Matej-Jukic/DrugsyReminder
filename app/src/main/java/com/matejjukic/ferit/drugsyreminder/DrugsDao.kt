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

    @Query("UPDATE Drugs SET Hour = :outerHour WHERE id")
    suspend fun updateHour(outerHour: Int)

    @Query("UPDATE Drugs SET hour=hour+step WHERE id")
    suspend fun updateHour()

    @Query("SELECT * FROM Drugs WHERE Id = :id")
    suspend fun getDrug(id: String):Drugs

    @Query( "UPDATE Drugs SET Name=:name, Hour=:hour, Minute=:minute, Step=:step WHERE Id=:id")
    suspend fun updateDrug(id: String, name: String, hour: Int, minute: Int, step: Int)

    @Insert
    suspend fun insert(drugs: Drugs)

    @Delete
    suspend fun delete(drugs: Drugs)
}