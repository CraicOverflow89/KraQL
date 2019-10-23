package craicoverflow89.kraql.components

class KraQLResult(private val fieldList: ArrayList<KraQLTableField>, val data: ArrayList<KraQLTableRecord>) {

    override fun toString() = "[${data.map{ it.toString() }.joinToString(", ")}]"

}