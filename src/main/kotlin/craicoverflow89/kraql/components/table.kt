package craicoverflow89.kraql.components

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class KraQLTable(val database: KraQLDatabase, val name: String, private val fieldList: ArrayList<KraQLTableField> = arrayListOf(), private val recordList: ArrayList<KraQLTableRecord> = arrayListOf()) {

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
            if(!it.type.accepts(data[it.name]!!)) {

                // Convert Data
                data[it.name] = convertData(it, data[it.name].toString())
            }
        }

        // NOTE: we are currently not implementing default field values (so aren't required in data)

        // Generate ID
        val id = idGenerate()

        // Add Record
        recordList.add(KraQLTableRecord(id, data))
    }

    fun convertData(field: KraQLTableField, value: String): Any {

        // Boolean Parser
        val parseBoolean = fun(value: String): Boolean {
            return when(value) {
                "true" -> true
                "false" -> false
                else -> throw KraQLTableInsertTypeException(field.name, field.type)
            }
        }

        // Timestamp Parser
        val parseTimestamp = fun(value: String): Date {

            // TEMP RETURN
            return Date()
        }

        // Type Conversion
        return when(field.type) {
            KraQLTableFieldType.BOOLEAN -> parseBoolean(value)
            KraQLTableFieldType.INTEGER -> Integer.parseInt(value)
            KraQLTableFieldType.TIMESTAMP -> parseTimestamp(value)
            else -> value
        }
    }

    fun get(): KraQLResult = KraQLResult(recordList)

    private fun idGenerate(): Int {
        idCount ++
        return idCount
    }

    fun save() {

        // TEMP DEBUG
        println("KraQLTable.save()")
        println(this.get())

        // Save Database
        database.save()
    }

    fun toFile() = ArrayList<String>().apply {

        // Table Data
        add("$name:${fieldList.size}:${recordList.size}")

        // Field Data
        fieldList.forEach {
            add(it.toFile())
        }

        // Record Data
        recordList.forEach {record ->
            add(fieldList.map {
                record.toFile(it)
            }.joinToString("|"))
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
class KraQLTableNotFoundException(name: String): Exception("Could not find a table named $name!")

class KraQLTableRecord(val id: Int, val data: HashMap<String, Any>) {

    fun toFile(field: KraQLTableField) = data[field.name]!!.toString().replace("|", "\\|")

    override fun toString() = "{id: $id, data: {${data.map {
        it.toString()
    }.joinToString(", ")}}}"

}