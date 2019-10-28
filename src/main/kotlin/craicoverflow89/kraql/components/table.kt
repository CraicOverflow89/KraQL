package craicoverflow89.kraql.components

import craicoverflow89.kraql.KraQLApplication
import craicoverflow89.kraql.KraQLReservedCreateException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class KraQLTable(val database: KraQLDatabase, val name: String, private val fieldList: ArrayList<KraQLTableField> = arrayListOf(), private val recordList: ArrayList<KraQLTableRecord> = arrayListOf(), private val indexList: ArrayList<KraQLTableIndex> = arrayListOf()) {

    var idCount = -1
    // NOTE: need to restore this from file, when loading instead of creating

    fun addField(name: String, type: KraQLTableFieldType): KraQLTableField {
        // NOTE: what about existing records?

        // Reserved Name
        if(KraQLApplication.isReserved(name)) throw KraQLReservedCreateException(name)

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
            // NOTE: still need to write this
        }

        // Type Conversion
        return when(field.type) {
            KraQLTableFieldType.BOOLEAN -> parseBoolean(value)
            KraQLTableFieldType.INTEGER -> Integer.parseInt(value)
            KraQLTableFieldType.TIMESTAMP -> parseTimestamp(value)
            else -> value
        }
    }

    fun deleteRecords(conditionList: List<KraQLQueryCondition>): Int {

        // Create Result
        val records = recordList.filter {record ->
            !conditionList.all {condition ->
                condition.matches(record.data[condition.field]!!.toString())
            }
        }

        // Delete Records
        recordList.removeAll(recordList)
        recordList.addAll(records)

        // Return Count
        return recordList.size - records.size
    }

    fun get(fieldList: ArrayList<KraQLTableField> = arrayListOf(), conditionList: ArrayList<Pair<KraQLTableField, KraQLQueryCondition>> = arrayListOf(), orderList: List<KraQLQueryOrder> = listOf()): KraQLResult {

        // NOTE: many things to consider when it comes to arguments

        // Default Fields
        if(fieldList.isEmpty()) fieldList.addAll(getFields())

        // NOTE: if selecting by a field that has an index, it will be much quicker to use that

        // Map Conditions
        val conditions = conditionList.map {
            it.second
        }
        // NOTE: could change things to avoid having to do this

        // Return Result
        return KraQLResult(fieldList, recordList.filter {
            it.where(this, conditions)
        }.map {
            it.withFields(fieldList)
        })
        // NOTE: need to reduce recordList to new list having applied the Order By sorting
    }

    fun getFields(): ArrayList<KraQLTableField> {
        return fieldList
    }

    fun getFields(value: List<String>) = ArrayList<KraQLTableField>().apply {
        value.forEach {fieldName ->
            add(fieldList.firstOrNull {
                it.name == fieldName
            } ?: throw KraQLTableFieldNotFoundException(name, fieldName))
        }
    }

    fun getIndexes() = indexList

    fun hasIndex(field: KraQLTableField) = hasIndex(field.name)

    fun hasIndex(field: String) = indexList.any {
        it.field.name == field
    }

    private fun idGenerate(): Int {
        idCount ++
        return idCount
    }

    fun parseConditions(conditionList: List<KraQLQueryCondition>) = ArrayList<Pair<KraQLTableField, KraQLQueryCondition>>().apply {
        conditionList.forEach {
            add(Pair(getFields(listOf(it.field))[0], it))
        }
    }

    fun save() {

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

    fun update(updateList: List<KraQLQueryUpdatePair>, conditionList: List<KraQLQueryCondition>): Int {

        // Create Result
        var updateCount = 0
        val records = ArrayList<KraQLTableRecord>().apply {
            recordList.forEach {record ->
                add(KraQLTableRecord(record.id, HashMap<String, Any>().apply {
                    fieldList.forEach {field ->
                        put(field.name, record.data[field.name]!!.let {existingValue ->

                            // Find Update
                            updateList.firstOrNull {
                                it.field == field.name
                            }.let {

                                // Update Value
                                if(it != null && conditionList.all {condition ->
                                    condition.matches(record.data[condition.field]!!.toString())
                                }) it.value.apply {

                                    // Increment Count
                                    updateCount ++
                                }

                                // Existing Value
                                else existingValue
                            }
                        })
                    }
                }))
            }
        }

        // Update Records
        recordList.removeAll(recordList)
        recordList.addAll(records)

        // Return Count
        return updateCount
    }

}

class KraQLTableField(private val table: KraQLTable, val name: String, val type: KraQLTableFieldType) {

    fun toFile() = "$name:${type.name}"

    override fun toString() = "{name: $name, type: $type}"

}

class KraQLTableFieldNotFoundException(table: String, field: String): Exception("No field named $field exists in $table table!")

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

class KraQLTableIndex(val table: KraQLTable, val field: KraQLTableField) {

    fun toFile() = ""
    // NOTE: come back to this

    override fun toString() = "{table: ${table.name}, field: ${field.name}}"

}

class KraQLTableInsertFieldException(field: String): Exception("Must supply data for the $field field!")
class KraQLTableInsertTypeException(field: String, type: KraQLTableFieldType): Exception("Must supply data of type $type for the $field field!")
class KraQLTableNotFoundException(name: String): Exception("Could not find a table named $name!")

class KraQLTableRecord(val id: Int, val data: HashMap<String, Any>) {

    fun toFile(field: KraQLTableField) = data[field.name]!!.toString().replace("|", "\\|")

    override fun toString() = "{id: $id, data: {${data.map {
        it.toString()
    }.joinToString(", ")}}}"

    fun withFields(fieldList: List<KraQLTableField>) = KraQLTableRecord(id, HashMap<String, Any>().apply {
        fieldList.forEach {
            put(it.name, data[it.name]!!)
        }
    })

    fun where(table: KraQLTable, conditionList: List<KraQLQueryCondition>) = conditionList.all {condition ->

        // Apply Condition
        condition.matches(data[condition.field].toString()!!)
    }

}