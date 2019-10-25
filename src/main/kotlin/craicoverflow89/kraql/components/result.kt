package craicoverflow89.kraql.components

class KraQLResult(private val fieldList: List<KraQLTableField>, private val data: List<KraQLTableRecord>) {

    fun getFields() = fieldList

    fun getRecordCount() = data.size

    fun getRecords() = data

    override fun toString() = "[${data.map{ it.toString() }.joinToString(", ")}]"

}