package craicoverflow89.kraql.components

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class KraQLTable(private val database: KraQLDatabase, val name: String, private val fieldList: ArrayList<KraQLTableField> = arrayListOf(), private val recordList: ArrayList<KraQLTableRecord> = arrayListOf()) {

    var idCount = -1
    // NOTE: need to restore this from file, when loading instead of creating

    fun addField(name: String, type: KraQLTableFieldType): KraQLTableField {
        // NOTE: what about existing records?

        // Create Field
        val field = KraQLTableField(this, name, type)

        // Add Field
        fieldList.add(field)

        // Return Field
        return field
    }

    fun addRecord(data: HashMap<String, Any>) {

        // Validate Fields
        fieldList.forEach {

            // Validate Exists
            if(!data.containsKey(it.name)) throw KraQLTableInsertFieldException(it.name)

            // Validate Type
            if(!it.type.accepts(data[it.name]!!)) throw KraQLTableInsertTypeException(it.name, it.type)
        }

        // NOTE: we are currently not implementing default field values (so aren't required in data)

        // Generate ID
        val id = idGenerate()

        // Add Record
        recordList.add(KraQLTableRecord(id, data))
    }

    fun get(): KraQLResult = KraQLResult(recordList)

    private fun idGenerate(): Int {
        idCount ++
        return idCount
    }

    fun toFile() = ArrayList<String>().apply {

        // Table Data
        add("$name:${fieldList.size}:${recordList.size}")

        // Field Data
        fieldList.forEach {
            add(it.toFile())
        }

        // Record Data
        recordList.forEach {
            add(it.toFile())
        }
    }

    override fun toString() = "{name: $name, database: ${database.name}, fields: ${if(fieldList.isEmpty()) "none" else "[" + fieldList.joinToString {"{name:${it.name}, type:${it.type}}"} + "]"}}"

}

class KraQLTableField(private val table: KraQLTable, val name: String, val type: KraQLTableFieldType) {

    fun toFile() = "$name:${type.name}"

    override fun toString() = "{name: $name, type: $type}"

}

enum class KraQLTableFieldType {
    BOOLEAN, INTEGER, STRING, TIMESTAMP;

    fun accepts(value: Any): Boolean {
        return when(this) {
            BOOLEAN -> value is Boolean
            INTEGER -> value is Integer
            STRING -> value is String
            TIMESTAMP -> value is Date
        }
    }

    companion object {

        fun valueOf(value: String): KraQLTableFieldType {
            return when(value) {
                "BOOLEAN" -> BOOLEAN
                "INTEGER" -> INTEGER
                "STRING" -> STRING
                "TIMESTAMP" -> TIMESTAMP
                else -> throw KraQLTableFieldTypeParseException(value)
            }
        }

    }

}

class KraQLTableFieldTypeParseException(value: String): Exception("Failed to parse field type from value $value!")
class KraQLTableInsertFieldException(field: String): Exception("Must supply data for the $field field!")
class KraQLTableInsertTypeException(field: String, type: KraQLTableFieldType): Exception("Must supply data of type $type for the $field field!")

class KraQLTableRecord(val id: Int, val data: HashMap<String, Any>) {

    fun toFile() = ArrayList<String>().apply {
        data.forEach {
            add(it.value.toString())
        }
    }.joinToString(",")

    override fun toString() = "{id: $id, data: {${data.map {
        it.toString()
    }.joinToString(", ")}}}"

}