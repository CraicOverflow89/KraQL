package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLTableFieldNotFoundException
import craicoverflow89.kraql.components.KraQLTableNotFoundException
import org.junit.Assert
import org.junit.Test

class KraQLQueryDeleteFromTest {

    @Test(expected = KraQLTableFieldNotFoundException::class)
    fun invalidField() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Delete Records
        db.query("""
            DELETE FROM test
            WHERE name2 = 'Jacob'
        """)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidSyntax() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Delete Records
        db.query("""
            DELETE FROM test
            WHERE name = 'Jacob'
            ORDER BY name
        """)
    }
    // NOTE: need to create custom exceptions for parsing syntax issues

    @Test(expected = KraQLTableNotFoundException::class)
    fun invalidTable() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Delete Records
        db.query("""
            DELETE FROM test2
            WHERE name = 'Jacob'
        """)
    }

    @Test
    fun valid() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Delete Records
        val result = db.query("""
            DELETE FROM test
            WHERE name LIKE 'Josh%'
        """)

        // Record Updated
        Assert.assertEquals(0, db.query("""
            SELECT name
            FROM test
            WHERE name LIKE 'Josh%'
        """).getData()!!.getRecordCount())

        // Delete Count
        Assert.assertEquals(1, result.getCount())
    }

}