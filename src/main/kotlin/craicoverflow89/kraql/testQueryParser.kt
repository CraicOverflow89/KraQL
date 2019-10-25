package craicoverflow89.kraql

fun main() {

    //testInsert()
    testSelect()
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
    val result = db.query("""
        SELECT name, dob
        FROM test
        WHERE name = 'James'
    """)

    // Debug Result
    println(result)
}