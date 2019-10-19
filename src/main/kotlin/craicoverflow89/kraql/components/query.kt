package craicoverflow89.kraql.components

abstract class KraQLQuery(val tableName: String) {

    abstract fun toMap(): HashMap<String, Any>
}

class KraQLQueryInsert(tableName: String, private val fieldList: ArrayList<String>, private val recordList: ArrayList<String>): KraQLQuery(tableName) {

    override fun toMap(): HashMap<String, Any> {

        // Create Result
        val data = HashMap<String, Any>()

        // Invalid Sizes
        //if(fieldList.size != recordList.size) throw ?

        // Iterate Fields
        var pos = 0
        while(pos < fieldList.size) {
            data[fieldList[pos]] = recordList[pos]
            pos ++
        }

        // Return Data
        return data
    }

    override fun toString() = "{table: $tableName, fieldList: $fieldList, recordList: $recordList}"

}

class KraQLQueryNotFoundException: Exception("Could not find a query at the location supplied!")

class KraQLQuerySelect(tableName: String): KraQLQuery(tableName) {

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf()
    }

}