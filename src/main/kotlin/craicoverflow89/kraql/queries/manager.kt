package craicoverflow89.kraql.queries

import java.io.File

class KraQLQueryLoader {

    companion object {

        fun loadFile(path: String) {

            // Validate Exists
            val file = File(path)
            //if(!file.exists()) ??

            // Read Query
            val query = file.readText()

            // Parse Query
            // NOTE: invoke the query parser
        }

    }

}