package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLTableFieldNotFoundException
import craicoverflow89.kraql.components.KraQLTableNotFoundException
import org.junit.Assert
import org.junit.Test

class KraQLQuerySelectTest {

    @Test(expected = KraQLTableFieldNotFoundException::class)
    fun invalidField() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        db.query("""
            SELECT age
            FROM test
        """)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidSyntax() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        db.query("""
            SELECT name
            ORDER BY name
            FROM test
        """)
    }
    // NOTE: need to create custom exceptions for parsing syntax issues

    @Test(expected = KraQLTableNotFoundException::class)
    fun invalidTable() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        db.query("""
            SELECT name
            FROM test2
        """)
    }

    @Test
    fun valid() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        val result = db.query("""
            SELECT name
            FROM test
            WHERE name LIKE 'J%'
        """)

        // Name Values
        Assert.assertEquals("James", result.getData()!!.getRecords()[0].data["name"])
        Assert.assertEquals("Josh", result.getData()!!.getRecords()[1].data["name"])

        // Select Count
        Assert.assertEquals(2, result.getCount())
    }

}