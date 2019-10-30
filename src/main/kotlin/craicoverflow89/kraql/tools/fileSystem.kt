package craicoverflow89.kraql.tools

import java.io.File

abstract class KraQLArchive(protected val parent: KraQLArchiveDirectory, protected val name: String) {

    abstract fun getPath(): String

    abstract fun write()

}

class KraQLArchiveDirectory(parent: KraQLArchiveDirectory, name: String): KraQLArchive(parent, name) {

    private val children = ArrayList<KraQLArchive>()

    fun addDirectory(name: String): KraQLArchiveDirectory {

        // Create Directory
        val directory = KraQLArchiveDirectory(this, name)

        // Append Directory
        children.add(directory)

        // Return Directory
        return directory
    }

    fun addFile(name: String, data: String): KraQLArchiveFile {

        // Create File
        val file = KraQLArchiveFile(this, name, data)

        // Append File
        children.add(file)

        // Return File
        return file
    }

    override fun getPath() = /*parent?.getPath() + "/" + name*/ ""
    // NOTE: come back to this

    override fun write() {

        // Create Directory
        File("").mkdir()

        // Write Children
        children.forEach {
            it.write()
        }
    }

}

class KraQLArchiveFile(parent: KraQLArchiveDirectory, name: String, private val data: String): KraQLArchive(parent, name) {

    override fun getPath() = parent?.getPath() + "/" + name
    // NOTE: come back to this

    override fun write() {

        // Write Data
        // NOTE: save file at this dir location called data with data string

    }

}