package craicoverflow89.kraql.components

abstract class KraQLQuery(protected val tableName: String) {

    //
}

class KraQLQueryInsert(tableName: String, private val fieldList: ArrayList<String>, private val recordList: ArrayList<String>): KraQLQuery(tableName) {

    override fun toString() = "{table: $tableName, fieldList: $fieldList, recordList: $recordList}"
}

class KraQLQueryNotFoundException: Exception("Could not find a query at the location supplied!")

class KraQLQuerySelect(tableName: String): KraQLQuery(tableName) {

    //
}