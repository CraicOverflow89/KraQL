package craicoverflow89.kraql.components

abstract class KraQLQuery(protected val tableName: String) {

    //
}

class KraQLQueryInsert(tableName: String): KraQLQuery(tableName) {

    //
}

class KraQLQuerySelect(tableName: String): KraQLQuery(tableName) {

    //
}

class KraQLQueryNotFoundException: Exception("Could not find a query at the location supplied!")