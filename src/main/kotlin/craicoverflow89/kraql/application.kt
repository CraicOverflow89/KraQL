package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLDatabase
import craicoverflow89.kraql.components.KraQLTable
import craicoverflow89.kraql.components.KraQLTableFieldType
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess

class KraQLApplication {

    companion object {

        fun getVersion() = resourceLoad("version").readText()

        fun loadDatabase(path: String): KraQLDatabase {

            // Validate File
            val file = File(path)
            if(!file.exists()) {
                throw FileNotFoundException("The path to the database was invalid!")
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
                while(tableFieldPos < tableFieldCount) {

                    // Field Data
                    readNext().split(":").let {
                        table.addField(it[0], KraQLTableFieldType.valueOf(it[1]))
                    }

                    // Increment Position
                    tableFieldPos ++
                }

                // Read Records
                tableRecordPos = 0
                while(tableRecordPos < tableRecordCount) {

                    // Record Data
                    readNext().split(":").let {
                        //table.addRecord()
                        // NOTE: here we need to convert line of data into HashMap
                    }

                    // Increment Position
                    tableRecordPos ++
                }

                // Increment Position
                tablePos ++
            }

            // Return Database
            return database
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
            // NOTE: if directory doesn't exist, should we recursively create it?

            // File Path
            val path = directory + "/" + database.name + ".kqld"

            // Write Data
            File(path).writeText(ArrayList<String>().apply {

                // Application Data
                add("# KraQL Database")
                add("# Version ${getVersion()}")

                // Database Data
                addAll(database.toFile())

            }.joinToString("\n"))
        }

    }

}

class KraQLResourceException(path: String): Exception("Could not locate resource at $path!")