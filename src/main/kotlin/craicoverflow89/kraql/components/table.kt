package craicoverflow89.kraql.components

class KraQLTable(private val database: KraQLDatabase, private val name: String, private val fieldList: ArrayList<KraQLTableField> = arrayListOf(), private val recordList: ArrayList<KraQLTableRecord> = arrayListOf()) {

    init {
        fieldList.add(KraQLTableField(this, "id", KraQLTableFieldType.INTEGER))
    }

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
        fieldList.filter {
            it.name != "id"
        }.forEach {

            // Validate Exists
            if(!data.containsKey(it.name)) throw KraQLTableInsertException(it.name)

            // NOTE: and validate type?
        }

        // NOTE: so here we need to create a record that is valid for the current fields

        // Add Record
        recordList.add(KraQLTableRecord(data))
    }

    fun get(): KraQLResult = KraQLResult(recordList)

    fun toFile() = ArrayList<String>().apply {

        // Table Data
        add("table:$name")

        // Field Data
        fieldList.forEach {
            add(it.toFile())
        }

        // Record Data
        add("[")
        recordList.forEach {
            addAll(it.toFile())
        }
        add("]")
    }

    override fun toString() = "{database: ${database.name}, name: $name}"

}

class KraQLTableField(private val table: KraQLTable, val name: String, private val type: KraQLTableFieldType) {

    fun toFile() = ""
    // NOTE: come back to this

    override fun toString() = "{name: $name, type: $type}"

}

enum class KraQLTableFieldType {
    BOOLEAN, INTEGER, STRING, TIMESTAMP
}

class KraQLTableInsertException(field: String): Exception("Must supply data for the $field field!")

class KraQLTableRecord(val data: HashMap<String, Any>) {

    fun toFile() = ArrayList<String>().apply {

        // NOTE: come back to this
    }

    override fun toString() = ArrayList<String>().apply {
        data.forEach {
            add(it.value.toString())
        }
    }.toString()

}