package craicoverflow89.kraql.components

import craicoverflow89.kraql.queries.parser.KraQLQueryLexer
import craicoverflow89.kraql.queries.parser.KraQLQueryParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

class KraQLDatabase(val name: String, private val tableList: ArrayList<KraQLTable> = arrayListOf()) {

    fun addTable(name: String): KraQLTable {

        // Create Table
        val table = KraQLTable(this, name)

        // Add Table
        tableList.add(table)

        // Return Table
        return table
    }

    fun addTable(name: String, fieldList: ArrayList<KraQLTableField>): KraQLTable {

        // Create Table
        val table = KraQLTable(this, name, fieldList)

        // Add Table
        tableList.add(table)

        // Return Table
        return table
    }

    fun query(query: String) {

        // Parse Query
        val lexer = KraQLQueryLexer(ANTLRInputStream(query))
        val parser = KraQLQueryParser(CommonTokenStream(lexer))
        //parser.query().result;
    }

    fun toFile() = ArrayList<String>().apply {

        // Database Data
        add("$name:${tableList.size}")

        // NOTE: might want to add other settings to the above

        // Table Data
        tableList.forEach {
            addAll(it.toFile())
        }
    }

    override fun toString() = "{name: $name, tables: ${if(tableList.isEmpty()) "none" else "[" + tableList.joinToString {it.name} + "]"}}"

}