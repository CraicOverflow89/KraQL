package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLDatabaseNotFoundException
import craicoverflow89.kraql.components.KraQLTableFieldType
import org.junit.Assert
import org.junit.Test

class KraQLDatabaseLoadTest {

    @Test(expected = KraQLDatabaseNotFoundException::class)
    fun notFound() {

        // Load Database
        KraQLApplication.loadDatabase("/missing.kqld")
    }

    @Test
    fun valid() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Database Name
        Assert.assertEquals("test", db.name)

        // Fetch Tables
        db.getTables().apply {

            // Table Count
            Assert.assertEquals(1, size)

            // Test Table
            this[0].apply {

                // Table Name
                Assert.assertEquals("test", name)

                // Test Fields
                getFields().apply {

                    // Name Field
                    Assert.assertEquals("name", this[0].name)
                    Assert.assertEquals(KraQLTableFieldType.STRING, this[0].type)

                    // DOB Field
                    Assert.assertEquals("dob", this[1].name)
                    Assert.assertEquals(KraQLTableFieldType.TIMESTAMP, this[1].type)
                }
            }
        }

        // Test Select
        db.query("""
            SELECT name
            FROM test
            WHERE name LIKE 'J%'
        """).getData()!!.apply {

            // Record Count
            Assert.assertEquals(2, getRecordCount())

            // Test Records
            this.getRecords().apply {
                Assert.assertEquals("James", this[0].data["name"])
                Assert.assertEquals("Josh", this[1].data["name"])
            }
        }
    }

}