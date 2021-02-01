package com.example.simpleshoppingrework.db.converters

import androidx.room.TypeConverter
import com.example.simpleshoppingrework.util.data.Details
import com.google.gson.Gson

class ConverterToJson {

    @TypeConverter
    fun toJson(details: List<Details>): String = Gson().toJson(details)

    @TypeConverter
    fun fromJson(json: String): List<Details> = Gson().fromJson(json, Array<Details>::class.java).toList()
}