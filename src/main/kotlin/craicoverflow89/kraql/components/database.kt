package craicoverflow89.kraql.components

import craicoverflow89.kraql.KraQLApplication
import craicoverflow89.kraql.KraQLReservedCreateException
import craicoverflow89.kraql.queries.KraQLQueryLexer
import craicoverflow89.kraql.queries.KraQLQueryParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

class KraQLDatabase(val name: String, private val accountList: ArrayList<KraQLAccount> = arrayListOf(), private val tableList: ArrayList<KraQLTable> = arrayListOf()) {

    // Debug: IGNORE SAVES
    private var debugSaveIgnore = false

    fun addAccount(name: String, password: String, permissions: HashMap<KraQLAccountPermission, Boolean>? = null): KraQLAccount {

        // Reserved Name
        if(KraQLApplication.isReserved(name)) throw KraQLReservedCreateException(name)

        // Create Account
        val account = KraQLAccount(this, name, password, permissions ?: hashMapOf())

        // Add Account
        accountList.add(account)

        // Return Account
        return account
    }

    fun addTable(name: String): KraQLTable {

        // Reserved Name
        if(KraQLApplication.isReserved(name)) throw KraQLReservedCreateException(name)

        // Create Table
        val table = KraQLTable(this, name)

        // Add Table
        tableList.add(table)

        // Return Table
        return table
    }

    fun addTable(name: String, fieldList: ArrayList<KraQLTableField>): KraQLTable {

        // Reserved Name
        if(KraQLApplication.isReserved(name)) throw KraQLReservedCreateException(name)

        // Create Table
        val table = KraQLTable(this, name, fieldList)

        // Add Table
        tableList.add(table)

        // Return Table
        return table
    }

    fun deleteTable(tableName: String) {

        // Find Table
        val table = tableList.firstOrNull {
            it.name == tableName
        } ?: throw KraQLTableNotFoundException(tableName)

        // Delete Table
        tableList.remove(table)

        // Save Database
        save()
    }

    fun getAccount(name: String) = accountList.firstOrNull {
        it.name == name
    } ?: throw KraQLAccountNotFoundException(name)

    fun getDebugSaveIgnore() = debugSaveIgnore

    fun getTable(name: String) = tableList.firstOrNull {
        it.name == name
    } ?: throw KraQLTableNotFoundException(name)

    fun getTables() = tableList

    fun query(value: String): KraQLQueryResult {

        // Parse Query
        val lexer = KraQLQueryLexer(ANTLRInputStream(value))
        val parser = KraQLQueryParser(CommonTokenStream(lexer))
        val query = parser.query().result

        // Invoke Query
        return query.invoke(this)
    }

    fun save() {

        // Debug Ignore
        if(debugSaveIgnore) return

        // TEMP INVOKE
        KraQLApplication.saveDatabase(this, "C:/Users/jamie/Software/Kotlin/KraQL/data/")
    }

    fun setDebugSaveIgnore(value: Boolean) {
        debugSaveIgnore = value
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

class KraQLDatabaseNotFoundException: Exception("The path to the database was invalid!")