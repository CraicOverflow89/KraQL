package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLTableFieldNotFoundException
import craicoverflow89.kraql.components.KraQLTableNotFoundException
import org.junit.Assert
import org.junit.Test

class KraQLQueryUpdateTest {

    @Test(expected = KraQLTableFieldNotFoundException::class)
    fun invalidField() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Update Records
        db.query("""
            UPDATE test
            SET name2 = 'Jacob'
        """)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidSyntax() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Update Records
        db.query("""
            UPDATE test
            SET name = 'Jacob'
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

        // Update Records
        db.query("""
            UPDATE test2
            SET name = 'Jacob'
        """)
    }

    @Test
    fun valid() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Update Records
        val result = db.query("""
            UPDATE test
            SET name = 'JoshNew'
            WHERE name = 'Josh'
        """)

        // Record Updated
        Assert.assertEquals("JoshNew", db.query("""
            SELECT name
            FROM test
            WHERE name LIKE 'Josh%'
        """).getData()!!.getRecords()[0].data["name"])

        // Delete Count
        Assert.assertEquals(1, result.getCount())
    }

}