package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLTableNotFoundException
import org.junit.Assert
import org.junit.Test

class KraQLQueryDeleteTableTest {

    @Test(expected = IllegalArgumentException::class)
    fun invalidSyntax() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Delete Table
        db.query("""
            DELETE TABLE test2
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

        // Delete Table
        db.query("""
            DELETE TABLE test2
        """)
    }

    @Test
    fun valid() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Delete Table
        val result = db.query("""
            DELETE TABLE test
        """)

        // Delete Count
        Assert.assertEquals(1, result.getCount())
    }

}