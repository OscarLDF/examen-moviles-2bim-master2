package com.example.Oscar.examen_moviles_2bim.parser

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import java.text.SimpleDateFormat
import java.util.*

object DateFieldConverter: Converter {
    override fun canConvert(cls: Class<*>): Boolean {
        return cls == Date::class.java
    }

    override fun fromJson(jv: JsonValue): Any {
        if(jv.string != null)
            return  SimpleDateFormat("dd-MM-yyyy").parse(jv.string)
        else
            return Date()
    }

    override fun toJson(value: Any): String {
        return ""
    }


}