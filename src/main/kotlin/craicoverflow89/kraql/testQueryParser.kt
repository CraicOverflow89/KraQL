package craicoverflow89.kraql

fun main() {

    //testInsert()
    // NOTE: encountering an NPE here due to inverted comma issues in the parser

    //testSelect()
    //testUpdate()
    testDeleteFrom()
    //testDeleteTable()

    // NOTE: need to check all of these different operations based on account permissions
}

fun testDeleteFrom() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld")

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

fun testDeleteTable() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld")

    // Debug Tables
    // NOTE: need to make it easy to select list of tables via query

    // Test Query
    db.query("""
        DELETE test
    """)

    // NOTE: need to check permissions here

    // Debug Data
    println(db.query("""
        SELECT *
        FROM test
    """).getData())
    // NOTE: expecting this to throw
}

fun testInsert() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld")

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

fun testSelect() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld")

    // Test Query
    println(db.query("""
        SELECT name, dob
        FROM test
        WHERE name LIKE 'J%'
        ORDER BY name DESC
    """).getData())
}

fun testUpdate() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld")

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