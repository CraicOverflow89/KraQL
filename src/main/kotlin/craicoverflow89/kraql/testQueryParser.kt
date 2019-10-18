package craicoverflow89.kraql

fun main() {

    // Load Database
    val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld")

    // Execute Query
    db.query("""
        INSERT INTO test (name)
        VALUES ('James')
    """)

    // Debug Table
    // NOTE: need database get table for this

}