package com.shellrider.demonstrator2.datasource

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.shellrider.demonstrator2.model.Encounter

class EncounterTableHandler(context: Context): SQLiteOpenHelper(context, dbName, null, 1) {
    companion object EncounterTable{
        private const val dbName:String = "AppDB"
        private const val tableName = "Encounters"
        private const val id = "_id"
        private const val name = "name"
        private const val description = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $tableName" +
                "($id INTEGER PRIMARY KEY," +
                "$name TEXT," +
                "$description TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $tableName;")
        onCreate(db)
    }

    fun addEncounter(name: String, description: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(EncounterTableHandler.name, name)
        contentValues.put(EncounterTableHandler.description, description)
        db.insert(EncounterTableHandler.tableName, null, contentValues)
    }

    fun listOfEncounters(): List<Encounter> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $tableName", null)
        val encounterList = mutableListOf<Encounter>()
        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex(id)
            val nameIndex = cursor.getColumnIndex(name)
            val descriptionIndex = cursor.getColumnIndex(description)
            val encounter = Encounter(
                cursor.getInt(idIndex),
                cursor.getString(nameIndex),
                cursor.getString(descriptionIndex)
            )
            encounterList.add(encounter)
        }
        return encounterList as List<Encounter>
    }

}