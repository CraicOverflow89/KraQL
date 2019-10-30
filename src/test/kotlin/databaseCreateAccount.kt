package craicoverflow89.kraql

import org.junit.Assert
import org.junit.Test

class KraQLDatabaseCreateAccountTest {

    @Test(expected = IllegalArgumentException::class)
    fun withPermissionsInvalid() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Create Account
        db.query("""
            CREATE ACCOUNT test
            WITH PERMISSIONS (
                TABLE_DANCE = true
            )
        """)
    }
    // NOTE: need to create custom exceptions for permission parsing exceptions

    @Test
    fun withPermissionsValid() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Create Account
        db.query("""
            CREATE ACCOUNT test
            WITH PERMISSIONS (
                TABLE_CREATE = true
            )
        """)

        // NOTE: assert that account with correct name and permissions was created (see the standard test)
    }

    /*@Test(expected = IllegalArgumentException::class)
    fun invalidSyntax() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Create Account
        db.query("""
            CREATE ACCOUNT test
            ORDER BY name
        """)
    }*/
    // NOTE: need to create custom exceptions for parsing syntax issues

    @Test
    fun standard() {

        // Load Database
        val db = KraQLApplication.loadDatabase("C:/Users/jamie/Software/Kotlin/KraQL/data/test.kqld"). apply {
            setDebugSaveIgnore(true)
        }

        // Create Account
        db.query("""
            CREATE ACCOUNT test2
        """)

        // Test Account
        db.getAccount("test2").apply {

            // Account Name
            Assert.assertEquals("test2", name)

            // Account Permissions
            // NOTE: come back for these
        }
    }

}