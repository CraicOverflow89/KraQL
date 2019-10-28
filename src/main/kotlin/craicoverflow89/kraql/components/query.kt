package craicoverflow89.kraql.components

abstract class KraQLQuery(protected val tableName: String) {

    abstract fun invoke(database: KraQLDatabase): KraQLQueryResult

    protected fun getTable(database: KraQLDatabase): KraQLTable {
        return database.getTable(tableName)
    }

    abstract fun toMap(): HashMap<String, Any>

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

        return input.matches(Regex("^" + value.replace("%", ".*") + "$"))
        // VERY TEMPORARY
    }

}

class KraQLQueryCreateTable(tableName: String, private val fieldList: List<KraQLQueryCreateTableField>): KraQLQuery(tableName) {

    override fun invoke(database: KraQLDatabase): KraQLQueryResult {

        // Create Table
        val table = database.addTable(tableName).apply {

            // Iterate Fields
            fieldList.forEach {

                // Create Field
                this.addField(it.name, KraQLTableFieldType.valueOf(it.type))
            }
        }

        // Save Database
        database.save()

        // Return Result
        return KraQLQueryResult(database, table, "Created the $tableName table!", 1)
    }

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf()
    }
    // NOTE: this isn't correct - where is it being called?

}

class KraQLQueryCreateTableField(val name: String, val type: String) {

    override fun toString() = "{name: $name, type: $type}"

}

class KraQLQueryDeleteFrom(tableName: String, private val conditionList: List<KraQLQueryCondition>): KraQLQuery(tableName) {

    override fun invoke(database: KraQLDatabase): KraQLQueryResult {

        // Fetch Table
        val table = getTable(database)

        // Delete Data
        val deleteCount = table.deleteRecords(conditionList)

        // Save Table
        table.save()

        // Return Result
        return KraQLQueryResult(database, table, "Deleted $deleteCount records!", deleteCount)
        // NOTE: records should not be pluralised if deleteCount == 1
    }

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf()
    }
    // NOTE: this isn't correct - where is it being called?

}

class KraQLQueryDeleteTable(tableName: String): KraQLQuery(tableName) {

    override fun invoke(database: KraQLDatabase): KraQLQueryResult {

        // Delete Table
        database.deleteTable(tableName)

        // Return Result
        return KraQLQueryResult(database, null, "Deleted $tableName table!", 1)
    }

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf()
    }
    // NOTE: this isn't correct - where is it being called?

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
        return KraQLQueryResult(database, table, "Inserted 1 record!", 1)
        // NOTE: come back to this when multiple records can be inserted at once
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

class KraQLQueryOrder(field: String, direction: KraQLQueryOrderDirection)

enum class KraQLQueryOrderDirection {
    ASCENDING, DESCENDING
}

class KraQLQueryResult(private val database: KraQLDatabase, private val table: KraQLTable?, private val description: String, private val count: Int, private val data: KraQLResult? = null) {
    // NOTE: come back to property visibility later on

    fun getData() = data

    override fun toString() = "{database: ${database.name}, table: ${table?.name}, description: $description, count: $count, data: $data}"

}

class KraQLQuerySelect(tableName: String, private val fieldList: ArrayList<String>, private val conditionList: List<KraQLQueryCondition>, private val orderList: List<KraQLQueryOrder>): KraQLQuery(tableName) {

    override fun invoke(database: KraQLDatabase): KraQLQueryResult {

        // Fetch Table
        val table = getTable(database)

        // Parse Fields
        val fields = table.getFields(fieldList)

        // Parse Conditions
        val conditions = table.parseConditions(conditionList)

        // Fetch Data
        val data = table.get(fields, conditions, orderList)

        // Return Result
        return KraQLQueryResult(database, table, "Selected ${data.getRecordCount()} records!", data.getRecordCount(), data)
        // NOTE: records should not be pluralised if result.data.size == 1
    }

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf()
    }
    // NOTE: this isn't correct - where is it being called?

}

class KraQLQueryUpdate(tableName: String, private val updateList: List<KraQLQueryUpdatePair>, private val conditionList: List<KraQLQueryCondition>): KraQLQuery(tableName) {

    override fun invoke(database: KraQLDatabase): KraQLQueryResult {

        // Fetch Table
        val table = getTable(database)

        // Update Data
        val updateCount = table.update(updateList, conditionList)

        // Save Table
        table.save()

        // Return Result
        return KraQLQueryResult(database, table, "Updated $updateCount records!", updateCount)
        // NOTE: records should not be pluralised if updateCount == 1
    }

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf()
    }
    // NOTE: this isn't correct - where is it being called?

}

class KraQLQueryUpdatePair(val field: String, val value: String) {

    override fun toString() = "{field: $field, value: $value}"

}