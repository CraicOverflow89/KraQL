package craicoverflow89.kraql.queries.parser.components

abstract class KraQLQuery(protected val tableName: String) {

    //
}

class KraQLQueryInsert(tableName: String): KraQLQuery(tableName) {

    //
}

class KraQLQuerySelect(tableName: String): KraQLQuery(tableName) {

    //
}