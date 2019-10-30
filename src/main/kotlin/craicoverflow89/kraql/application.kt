package craicoverflow89.kraql

import craicoverflow89.kraql.components.*
import craicoverflow89.kraql.queries.KraQLQueryLexer
import craicoverflow89.kraql.queries.KraQLQueryParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess

class KraQLApplication {

    companion object {

        private val reservedList = resourceLoad("database/reserved").readLines()
        private val version = resourceLoad("database/version").readText()

        fun getVersion() = version

        fun isReserved(value: String) = reservedList.contains(value)

        fun loadDatabase(path: String): KraQLDatabase {

            // Validate File
            val file = File(path)
            if(!file.exists()) {
                throw KraQLDatabaseNotFoundException()
                exitProcess(-1)
            }

            // File Reader
            val reader = file.bufferedReader()

            // Read Logic
            val readNext = fun(): String {
                var line: String
                while(true) {
                    line = reader.readLine()
                    //if(line == null) throw?
                    if(line.startsWith("#")) continue
                    return line
                }
            }

            // Database Data
            var databaseName: String
            var tableCount: Int
            readNext().split(":").let {
                databaseName = it[0]
                tableCount = Integer.parseInt(it[1])
            }

            // Create Database
            val database = KraQLDatabase(databaseName)

            // Read Tables
            var table: KraQLTable
            var tablePos = 0
            var tableName: String
            var tableFieldCount: Int
            var tableFieldPos: Int
            var tableFieldList: ArrayList<String>
            var tableRecordCount: Int
            var tableRecordPos: Int
            while(tablePos < tableCount) {

                // Table Data
                readNext().split(":").let {
                    tableName = it[0]
                    tableFieldCount = Integer.parseInt(it[1])
                    tableRecordCount = Integer.parseInt(it[2])
                }

                // Create Table
                table = database.addTable(tableName)

                // Read Fields
                tableFieldPos = 0
                tableFieldList = ArrayList<String>()
                while(tableFieldPos < tableFieldCount) {

                    // Field Data
                    readNext().split(":").let {
                        table.addField(it[0], KraQLTableFieldType.valueOf(it[1]))
                        tableFieldList.add(it[0])
                    }

                    // Increment Position
                    tableFieldPos ++
                }

                // Read Records
                tableRecordPos = 0
                while(tableRecordPos < tableRecordCount) {

                    // Record Data
                    readNext().split("|").let {
                        table.addRecord(HashMap<String, Any>().apply {
                            tableFieldList.forEachIndexed {fieldPos, fieldName ->
                                put(fieldName, it[fieldPos])
                            }
                        })
                    }
                    // NOTE: need to make sure we split on | but not \| parts

                    // Increment Position
                    tableRecordPos ++
                }

                // Increment Position
                tablePos ++
            }

            // Return Database
            return database
        }

        fun loadQuery(path: String) {

            // Validate Exists
            val file = File(path)
            if(!file.exists()) throw KraQLQueryNotFoundException()

            // NOTE: need to see about passing the file.readText() value to current database
            //       that method is currently loading from file instead of taking query string and params
        }

        fun resourceLoad(path: String) = (object {}.javaClass.getResource("/$path")).let {

            // Validate Resource
            it ?: throw KraQLResourceException(path)

            // Return File
            File(it.toURI())
        }

        fun saveDatabase(database: KraQLDatabase, directory: String) {

            // Validate Directory
            //if(!File(directory).exists()) throw
            // NOTE: throw if directory doesn't exist?

            // Archive Directory
            val archiveDir = directory + (if(directory.endsWith("/")) "" else "/") + database.name

            // Write Archive
            File(archiveDir).mkdir()

            // Write Accounts
            File("$archiveDir/accounts").mkdir()
            database.getAccounts().forEach {

                // Write Data
                File("$archiveDir/accounts/${it.name}.kqld").writeText(it.toFile())
            }

            // Write Tables
            File("$archiveDir/tables").mkdir()
            database.getTables().forEach {table ->

                // Create Directory
                File("$archiveDir/tables/${table.name}").mkdir()

                // Create Data
                File("$archiveDir/tables/${table.name}/data").mkdir()
                table.getRecords().forEach {record ->
                    File("$archiveDir/tables/${table.name}/data/${record.id}.kqld").writeText(record.toFile())
                }

                // Create Indexes
                File("$archiveDir/tables/${table.name}/indexes").mkdir()
                table.getIndexes().forEach {index ->
                    File("$archiveDir/tables/${table.name}/indexes/${index.getName()}.kqld").writeText(index.toFile())
                }
            }
        }

    }

}

class KraQLReservedCreateException(term: String): Exception("Could not create an object with the reserved term $term!")

class KraQLResourceException(path: String): Exception("Could not locate resource at $path!")