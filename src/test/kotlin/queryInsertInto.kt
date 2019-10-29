package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLTableInsertFieldException
import org.junit.Assert
import org.junit.Test

class KraQLQueryInsertIntoTest {

    @Test(expected = IllegalArgumentException::class)
    fun invalidSyntax() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Insert Record
        db.query("""
            INSERT INTO test (name, dob)
            VALUES ('Jacob')
        """)
    }
    // NOTE: need to create custom exceptions for parsing syntax issues

    @Test(expected = IllegalArgumentException::class)
    fun invalidType() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Insert Record
        db.query("""
            INSERT INTO test (name, dob)
            VALUES ('Jacob', 'OOGIE')
        """)
    }
    // NOTE: need to handle custom exceptions for type parsing issues

    @Test(expected = KraQLTableInsertFieldException::class)
    fun missingField() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld").apply {
            setDebugSaveIgnore(true)
        }

        // Insert Record
        db.query("""
            INSERT INTO test (name)
            VALUES ('Jacob')
        """)
    }

    @Test
    fun valid() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Record Count
        val table = db.getTable("test")
        val recordCount = table.get().getRecordCount()

        // Insert Record
        db.query("""
            INSERT INTO test (name, dob)
            VALUES ('Jacob', '2019-10-19 08.32.00.000')
        """)

        // Record Added
        Assert.assertEquals(recordCount + 1, table.get().getRecordCount())
        val record = table.get().getRecords().let {
            it[it.size - 1]
        }

        // Name Value
        Assert.assertEquals("Jacob", record.data["name"]!!.toString())

        // DOB Value
        //Assert.assertEquals("", record.data["dob"]!!.toString())
        // NOTE: update this when there is a fixed value for DOB default dats
    }

}