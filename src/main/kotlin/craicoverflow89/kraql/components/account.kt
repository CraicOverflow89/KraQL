package craicoverflow89.kraql.components

class KraQLAccount(val database: KraQLDatabase, val name: String, val password: String, val permissions: HashMap<KraQLAccountPermission, Boolean> = hashMapOf()) {

    init {

        // Default Permissions
        KraQLAccountPermission.toOrderedList().forEach {
            if(!permissions.containsKey(it)) permissions[it] = false
        }

    }

    fun hasPermission(permission: KraQLAccountPermission) = permissions[permission]

    fun permissionsToString() = ArrayList<String>().apply {
        KraQLAccountPermission.toOrderedList().forEach {
            add(if(permissions[it]!!) "+" else "-")
        }
    }.joinToString("")

    fun setPermission(permission: KraQLAccountPermission, value: Boolean) {
        permissions[permission] = value
    }

    fun toFile() = ""
    // NOTE: come back to this

    override fun toString() = "{name: $name, database: ${database.name}, permissions:${permissionsToString()}}"

    fun validatePassword(value: String): Boolean {
        return value == password
        // NOTE: update this to accommodate hashing
    }

}

enum class KraQLAccountPermission {
    DATABASE_CREATE, DATABASE_DELETE, TABLE_CREATE, TABLE_DELETE;

    companion object {

        fun toOrderedList() = ArrayList<KraQLAccountPermission>().apply {
            add(DATABASE_CREATE)
            add(DATABASE_DELETE)
            add(TABLE_CREATE)
            add(TABLE_DELETE)
        }

    }
}