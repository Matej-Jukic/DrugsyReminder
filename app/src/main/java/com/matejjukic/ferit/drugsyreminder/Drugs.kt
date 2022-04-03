package com.matejjukic.ferit.drugsyreminder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity
data class Drugs(
    @PrimaryKey val id:String,
    @ColumnInfo(name = "Name") val name: String?,
    @ColumnInfo(name = "Hour") val hour: Int?,
    @ColumnInfo(name = "Minute") val minute: Int?,
    @ColumnInfo(name = "Step") val step: Int?,
    @ColumnInfo(name = "Current") val current: Boolean?,
)
