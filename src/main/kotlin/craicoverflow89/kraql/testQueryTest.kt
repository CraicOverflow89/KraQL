package craicoverflow89.kraql

import craicoverflow89.kraql.queries.KraQLQueryLexer
import craicoverflow89.kraql.queries.KraQLQueryParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

fun main() {

    // Test Query
    val input = """
        INSERT INTO test (name)
        VALUES ('James')
    """

    // Parse Query
    val lexer = KraQLQueryLexer(ANTLRInputStream(input))
    val parser = KraQLQueryParser(CommonTokenStream(lexer))
    println(parser.query().result)
}