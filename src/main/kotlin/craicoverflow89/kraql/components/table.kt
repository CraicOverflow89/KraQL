package craicoverflow89.kraql.components

class KraQLTable(private val database: KraQLDatabase, private val name: String, private val fieldList: ArrayList<KraQLTableField> = arrayListOf(), private val recordList: ArrayList<KraQLTableRecord> = arrayListOf()) {

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
            if(!data.containsKey(it.name)) throw KraQLTableInsertException(it.name)

            // NOTE: and validate type?
        }

        // Add Record
        recordList.add(KraQLTableRecord())
    }

    fun get(): ArrayList<KraQLTableRecord> {
        return recordList
        // NOTE: a result set would be better for debugging (and access)?
    }

    fun toFile() = ArrayList<String>().apply {
        add("table:$name")
        // NOTE: list both field types and data
    }

    override fun toString() = "{database: ${database.name}, name: $name}"

}

class KraQLTableField(private val table: KraQLTable, val name: String, private val type: KraQLTableFieldType) {

    override fun toString() = "{name: $name, type: $type}"

}

enum class KraQLTableFieldType {
    BOOLEAN, INTEGER, STRING, TIMESTAMP
}

class KraQLTableInsertException(field: String): Exception("Must supply data for the $field field!")

class KraQLTableRecord() {

    // ??

}