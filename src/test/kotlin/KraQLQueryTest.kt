package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLTableFieldType
import org.junit.Assert
import org.junit.Test

class KraQLQueryTest {

    @Test
    fun createTable() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Table Count
        val tableCount = db.getTables().size

        // Create Table
        db.query("""
            CREATE TABLE test2 (
                name = STRING,
                age = INTEGER
            )
        """)

        // Table Created
        Assert.assertEquals(tableCount + 1, db.getTables().size)
        val newTable = db.getTable("test2")

        // Name Field
        Assert.assertTrue(newTable.getFields(listOf("name")).size == 1)
        Assert.assertTrue(newTable.getField("name").type == KraQLTableFieldType.STRING)

        // Age Field
        Assert.assertTrue(newTable.getFields(listOf("age")).size == 1)
        Assert.assertTrue(newTable.getField("age").type == KraQLTableFieldType.INTEGER)
    }

    /*@Test(expected = IllegalArgumentException::class)
    fun createTableInvalidSyntax() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Create Table
        db.query("""
            CREATE TABLE test2 (
                tokens
            )
        """)
    }*/
    // NOTE: need to create custom exceptions for parsing issues

    /*@Test(expected = KraQLTableFieldTypeParseException::class)
    fun createTableInvalidType() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Table Count
        val tableCount = db.getTables().size

        // Create Table
        db.query("""
            CREATE TABLE test2 (
                name = BOOGIE,
                age = INTEGER
            )
        """)
    }*/
    // NOTE: need to handle custom exceptions for type parsing issues

    @Test(expected = KraQLReservedCreateException::class)
    fun createTableReservedName() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Create Table
        db.query("""
            CREATE TABLE DELETE (
                name = STRING
            )
        """)
    }

}