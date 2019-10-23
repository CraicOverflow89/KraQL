package craicoverflow89.kraql.components

abstract class KraQLQuery(protected val tableName: String) {

    abstract fun invoke(database: KraQLDatabase): KraQLQueryResult

    protected fun getTable(database: KraQLDatabase): KraQLTable {
        return KraQLTable(database, tableName)
    }

    abstract fun toMap(): HashMap<String, Any>

}

class KraQLQueryInsert(tableName: String, private val fieldList: ArrayList<String>, private val recordList: ArrayList<String>): KraQLQuery(tableName) {

    override fun invoke(database: KraQLDatabase): KraQLQueryResult {

        // Fetch Table
        val table = getTable(database)

        // Add Record
        table.addRecord(this.toMap())

        // Save Table
        table.save()

        // TEMP RETURN
        return KraQLQueryResult(table, "Inserted x records!")
    }

    override fun toMap(): HashMap<String, Any> {

        // Create Result
        val data = HashMap<String, Any>()

        // Invalid Sizes
        //if(fieldList.size != recordList.size) throw ?

        // Iterate Fields
        var pos = 0
        while(pos < fieldList.size) {
            data[fieldList[pos]] = recordList[pos]
            pos ++
        }

        // Return Data
        return data
    }

    override fun toString() = "{table: $tableName, fieldList: $fieldList, recordList: $recordList}"
    // NOTE: remove this after debugging?

}

class KraQLQueryNotFoundException: Exception("Could not find a query at the location supplied!")

class KraQLQueryResult(private val table: KraQLTable, private val result: String) {
    // NOTE: come back to property visibility later on

    override fun toString() = "{database: ${table.database.name}, table: ${table.name}, result: $result}"

}

class KraQLQuerySelect(tableName: String): KraQLQuery(tableName) {

    override fun invoke(database: KraQLDatabase): KraQLQueryResult {

        // Fetch Table
        val table = getTable(database)

        // TEMP RETURN
        return KraQLQueryResult(table, "Selected x records!")
    }

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf()
    }

}