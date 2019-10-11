package craicoverflow89.kraql.components

class KraQLDatabase(val name: String, private val tableList: ArrayList<KraQLTable> = arrayListOf()) {

    fun addTable(name: String): KraQLTable {

        // Create Table
        val table = KraQLTable(this, name)

        // Add Table
        tableList.add(table)

        // Return Table
        return table
    }

    fun toFile() = ArrayList<String>().apply {

        // Database Info
        add("db:$name")
        // NOTE: might want to add settings like "[setting1,setting2]"

        // Table Info
        tableList.forEach {
            addAll(it.toFile())
        }
    }

    override fun toString() = "{name: $name}"

}