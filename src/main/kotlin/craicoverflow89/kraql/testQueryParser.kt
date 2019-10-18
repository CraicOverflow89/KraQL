package craicoverflow89.kraql

import craicoverflow89.kraql.queries.KraQLQueryLexer
import craicoverflow89.kraql.queries.KraQLQueryParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

fun main() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld")

    // Test Query
    val input = """
        INSERT INTO test (name)
        VALUES ('James')
    """

    // Parse Query
    val lexer = KraQLQueryLexer(ANTLRInputStream(input))
    val parser = KraQLQueryParser(CommonTokenStream(lexer))
    val query = parser.query().result)

    // Add Record
    db.getTable(query.tableName).addRecord(query.data)

    // Debug Table
    println(db.getTable(query.tableName))

}