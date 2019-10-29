package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLDatabase
import kotlin.system.exitProcess

fun main() {

    // Connected Database
    var db: KraQLDatabase? = null
    // NOTE: do not have means to connect to or use database

    // TEMP DATABASE
    db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
        setDebugSaveIgnore(true)
    }

    // Main Process
    while(true) {

        // User Input
        print("KraQL>")
        val input = readLine()

        // Terminate Shell
        if(input == null || input?.trim() == "exit") exitProcess(0)

        // Parse Input
        try {

            // Invoke Query
            val result = db.query(input)

            // Render Result
            println()
            println(result)
            println()
        }

        // Handle Exceptions
        catch(ex: Exception) {

            // Render Exception
            println()
            System.err.println(ex.message)
            println()
        }
    }

}