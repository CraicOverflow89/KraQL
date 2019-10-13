package craicoverflow89.kraql.components

class KraQLResult(val data: ArrayList<KraQLTableRecord>) {

    override fun toString() = ArrayList<String>().apply {
        data.forEach {
            add(it.toString())
        }
    }.toString()

}