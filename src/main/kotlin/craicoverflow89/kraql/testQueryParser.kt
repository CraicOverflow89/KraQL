package craicoverflow89.kraql

import craicoverflow89.kraql.components.KraQLDatabase

fun main() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
        setDebugSaveIgnore(true)
    }

    // NOTE: all conditions are currently unable to specify ID field
    // NOTE: there is currently no capability of OR in conditions or parens to determine groups
    // NOTE: not currently allowing for comments within the query string

    //testInsert(db)
    // NOTE: encountering an NPE here due to inverted comma issues in the parser

    //testSelect(db)
    //testUpdate(db)
    //testDeleteFrom(db)
    //testDeleteTable(db)
    //testCreateTable(db)

    // NOTE: need to check all of these different operations without WHERE clauses
    // NOTE: need to check all of these different operations based on account permissions
}

fun testCreateTable(db: KraQLDatabase) {

    // Test Query
    println(db.query("""
        CREATE TABLE test2 (
            name = STRING,
            age = INTEGER
        )
    """))

    // Debug Tables
    println(db.getTables())
}

fun testDeleteFrom(db: KraQLDatabase) {

    // Debug Data
    println(db.query("""
        SELECT *
        FROM test
    """).getData())

    // Test Query
    db.query("""
        DELETE FROM test
        WHERE name = 'Josh'
    """)

    // Debug Data
    println(db.query("""
        SELECT *
        FROM test
    """).getData())
}

fun testDeleteTable(db: KraQLDatabase) {

    // Debug Tables
    println(db.getTables())

    // Test Query
    db.query("""
        DELETE TABLE test
    """)

    // NOTE: need to check permissions here

    // Debug Tables
    println(db.getTables())
}

fun testInsert(db: KraQLDatabase) {

    // Test Query
    val result = db.query("""
        INSERT INTO test (name, dob)
        VALUES ('James', '2019-10-19 08.32.00.000')
    """)

    // Debug Result
    println(result)

    // Debug Table
    println(db.getTable("test").get())
}

fun testSelect(db: KraQLDatabase) {

    // Test Query
    println(db.query("""
        SELECT name, dob
        FROM test
        WHERE name LIKE 'J%'
        ORDER BY name DESC
    """).getData())
}

fun testUpdate(db: KraQLDatabase) {

    // Debug Data
    println(db.query("""
        SELECT *
        FROM test
    """).getData())

    // Test Query
    println(db.query("""
        UPDATE test
        SET name = 'JoshNew'
        WHERE name = 'Josh'
    """))

    // Debug Data
    println(db.query("""
        SELECT *
        FROM test
    """).getData())
}