package craicoverflow89.kraql.components

abstract class KraQLQuery(protected val tableName: String) {

    abstract fun invoke(database: KraQLDatabase): KraQLQueryResult

    protected fun getTable(database: KraQLDatabase): KraQLTable {
        return database.getTable(tableName)
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

}

class KraQLQueryNotFoundException: Exception("Could not find a query at the location supplied!")

class KraQLQueryResult(private val table: KraQLTable, private val description: String, private val data: KraQLResult? = null) {
    // NOTE: come back to property visibility later on

    override fun toString() = "{database: ${table.database.name}, table: ${table.name}, description: $description, data: $data}"

}

abstract class KraQLQueryCondition(val field: String, private val value: String) {

    abstract fun matches(input: String): Boolean

    override fun toString() = "{field: $field, value: $value}"

}

class KraQLQueryConditionEquals(field: String, private val value: String): KraQLQueryCondition(field, value) {

    override fun matches(input: String): Boolean {
        return input == value
    }

}

class KraQLQueryConditionLike(field: String, private val value: String): KraQLQueryCondition(field, value) {

    override fun matches(input: String): Boolean {
        return input == value
        // NOTE: this is where we parse the % char for special behaviour
    }

}

class KraQLQuerySelect(tableName: String, private val fieldList: ArrayList<String>, private val conditionList: List<KraQLQueryCondition>): KraQLQuery(tableName) {

    override fun invoke(database: KraQLDatabase): KraQLQueryResult {

        // Fetch Table
        val table = getTable(database)

        // Parse Fields
        val fields = table.getFields(fieldList)

        // Parse Conditions
        val conditions = table.parseConditions(conditionList)

        // TEMP DEBUG
        println(conditions)

        // Fetch Data
        val data = table.get(fields, conditionList)

        // Return Result
        return KraQLQueryResult(table, "Selected ${data.getRecordCount()} records!", data)
        // NOTE: records should not be pluralised if result.data.size == 1
    }

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf()
    }

}