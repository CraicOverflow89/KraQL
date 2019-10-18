package craicoverflow89.kraql.components

class KraQLAccount(val database: KraQLDatabase, val name: String, val password: String, val permissions: HashMap<KraQLAccountPermission, Boolean> = hashMapOf()) {

    init {
        permissions[KraQLAccountPermission.DATABASE_CREATE] = false
        permissions[KraQLAccountPermission.DATABASE_DELETE] = false
        permissions[KraQLAccountPermission.TABLE_CREATE] = false
        permissions[KraQLAccountPermission.TABLE_DELETE] = false
    }

    fun hasPermission(permission: KraQLAccountPermission) = permissions[permission]

    fun setPermission(permission: KraQLAccountPermission, value: Boolean) {
        permissions[permission] = value
    }

    fun toFile() = ""
    // NOTE: come back to this

    override fun toString() = "{name: $name, database: ${database.name}, permissions:${permissions.map {
        if(it.value) "+" else "-"
    }.joinToString("")}}"

    fun validatePassword(value: String): Boolean {
        return value == password
        // NOTE: update this to accommodate hashing
    }

}

enum class KraQLAccountPermission {
    DATABASE_CREATE, DATABASE_DELETE, TABLE_CREATE, TABLE_DELETE
}