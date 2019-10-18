package craicoverflow89.kraql.components

class KraQLResult(val data: ArrayList<KraQLTableRecord>) {

    override fun toString() = "[${data.map{ it.toString() }.joinToString(", ")}]"

}