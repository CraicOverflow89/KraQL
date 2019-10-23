package craicoverflow89.kraql

import craicoverflow89.kraql.queries.KraQLQueryLexer
import craicoverflow89.kraql.queries.KraQLQueryParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

fun main() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld")

    // Fetch Table
    val table = db.getTable("test")

    // Test Query
    val input = """
        INSERT INTO test (name, dob)
        VALUES ('James', '2019-10-19 08.32.00.000')
    """

    // Parse Query
    val lexer = KraQLQueryLexer(ANTLRInputStream(input))
    val parser = KraQLQueryParser(CommonTokenStream(lexer))
    val query = parser.query().result

    // Debug Query
    println(query)

    // Parse Data
    val data = query.toMap()

    // Debug Data
    println(data)

    // Add Record
    table.addRecord(data)

    // Debug Table
    println(table.get())

}