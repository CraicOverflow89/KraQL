package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLAccountPermission
import craicoverflow89.kraql.components.KraQLDatabase
import craicoverflow89.kraql.components.KraQLTableFieldType
import java.util.Date

fun main() {

    //testLoad()
    //testSave()
    testSaveNew()

}

fun testLoad() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld")

    // Debug Database
    println(db)

    // Debug Table
    println(db.getTable("test"))

    // Debug Data
    println(db.getTable("test").get())

}

fun testSave() {

    // Create Database
    val db = KraQLDatabase("test")

    // Debug Database
    println(db)

    // Create Account
    val account = db.addAccount("James", "password", hashMapOf(Pair(KraQLAccountPermission.DATABASE_CREATE, true)))

    // Debug Account
    println(account)

    // Create Table
    val table = db.addTable("test")

    // Add Fields
    table.addField("name", KraQLTableFieldType.STRING)
    table.addField("dob", KraQLTableFieldType.TIMESTAMP)

    // Debug Table
    println(table)

    // Add Records
    table.addRecord(hashMapOf(
        Pair("name", "James"),
        Pair("dob", Date())
    ))
    table.addRecord(hashMapOf(
        Pair("name", "Josh"),
        Pair("dob", Date())
    ))

    // Debug Table
    println(table.get())

    // Save Database
    KraQLApplication.saveDatabase(db, "C:/Users/jamie/Software/Kotlin/KraQL/data/")

    // Load Database
    val db2 = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/${db.name}.kqld")

    // Debug Database
    println("\nLoaded Database")
    println(db2)

    // Debug Table
    println(db2.getTable("test"))

    // Debug Data
    println(db2.getTable("test").get())

}

fun testSaveNew() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
        addAccount("James", "password")
    }

    // Save Database
    db.save()
}