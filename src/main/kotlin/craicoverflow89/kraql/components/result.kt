package craicoverflow89.kraql.components

class KraQLResult(private val fieldList: List<KraQLTableField>, val data: List<KraQLTableRecord>) {

    override fun toString() = "[${data.map{ it.toString() }.joinToString(", ")}]"

}