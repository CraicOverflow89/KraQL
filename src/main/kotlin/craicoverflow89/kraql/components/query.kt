package craicoverflow89.kraql.components

abstract class KraQLQuery(val tableName: String) {

    //
}

class KraQLQueryInsert(tableName: String, private val fieldList: ArrayList<String>, private val recordList: ArrayList<String>): KraQLQuery(tableName) {

    // NOTE: so here we need to create a public val data HashMap of fields to records

    override fun toString() = "{table: $tableName, fieldList: $fieldList, recordList: $recordList}"
}

class KraQLQueryNotFoundException: Exception("Could not find a query at the location supplied!")

class KraQLQuerySelect(tableName: String): KraQLQuery(tableName) {

    //
}