package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLDatabase
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

            // Read Data
            val data = file.readLines()
            // NOTE: this is where we parse the database data and create a KraQLDatabase object

            // TEMP RETURN
            return KraQLDatabase("")
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