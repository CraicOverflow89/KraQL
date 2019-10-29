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
    fun orderByNameAscending() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        val result = db.query("""
            SELECT *
            FROM test
            ORDER BY name ASC
        """)

        // Name Values
        result.getData()!!.getRecords().let {
            Assert.assertEquals("James", it[0].data["name"])
            Assert.assertEquals("Josh", it[1].data["name"])
        }

        // Select Count
        Assert.assertEquals(2, result.getCount())
    }

    @Test
    fun orderByNameDescending() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        val result = db.query("""
            SELECT *
            FROM test
            ORDER BY name DESC
        """)

        // Name Values
        result.getData()!!.getRecords().let {
            Assert.assertEquals("Josh", it[0].data["name"])
            Assert.assertEquals("James", it[1].data["name"])
        }

        // Select Count
        Assert.assertEquals(2, result.getCount())
    }

    @Test
    fun selectAll() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        val result = db.query("""
            SELECT *
            FROM test
        """)

        // Data Fields
        result.getData()!!.getRecords()[0].data.apply {
            Assert.assertEquals(true, this.containsKey("name"))
            Assert.assertEquals(true, this.containsKey("dob"))
        }
        // NOTE: would be even better to check table fieldList all exist in data keys
    }

    @Test
    fun selectMultiple() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        val result = db.query("""
            SELECT name, dob
            FROM test
        """)

        // Data Fields
        result.getData()!!.getRecords()[0].data.apply {
            Assert.assertEquals(true, this.containsKey("name"))
            Assert.assertEquals(true, this.containsKey("dob"))
        }
    }

    @Test
    fun selectName() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        val result = db.query("""
            SELECT name
            FROM test
        """)

        // Data Fields
        result.getData()!!.getRecords()[0].data.apply {
            Assert.assertEquals(true, this.containsKey("name"))
            Assert.assertEquals(false, this.containsKey("dob"))
        }
    }

    @Test
    fun whereEquals() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        val result = db.query("""
            SELECT *
            FROM test
            WHERE name = 'James'
        """)

        // Name Values
        result.getData()!!.getRecords().let {
            Assert.assertEquals("James", it[0].data["name"])
        }

        // Select Count
        Assert.assertEquals(1, result.getCount())
    }

    @Test
    fun whereLike() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        val result = db.query("""
            SELECT *
            FROM test
            WHERE name LIKE 'J%'
        """)

        // Name Values
        result.getData()!!.getRecords().let {
            Assert.assertEquals("James", it[0].data["name"])
            Assert.assertEquals("Josh", it[1].data["name"])
        }

        // Select Count
        Assert.assertEquals(2, result.getCount())
    }

    @Test
    fun whereNone() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Select Records
        val result = db.query("""
            SELECT *
            FROM test
        """)

        // Name Values
        result.getData()!!.getRecords().let {
            Assert.assertEquals("James", it[0].data["name"])
            Assert.assertEquals("Josh", it[1].data["name"])
        }

        // Select Count
        Assert.assertEquals(2, result.getCount())
    }

}