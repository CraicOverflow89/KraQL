package craicoverflow89.kraql.server

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import kotlin.system.exitProcess
import java.net.BindException
import java.net.InetSocketAddress

class KraQLServer(private val port: Int) {

    private var running = false
    private var server: HttpServer? = null

    inner class RequestHandler: HttpHandler {

        override fun handle(ex: HttpExchange) {
            response(ex, "Hello from KraQL Server!")
        }

    }

    private fun response(ex: HttpExchange, response: String, code: Int = 200) = with(ex) {

        // Response Headers
        sendResponseHeaders(code, response.length.toLong())

        // Response Body
        with(responseBody) {
            write(response.toByteArray())
            close()
        }
    }

    fun start() {

        // Already Running
        if(running) return

        // Create Server
        try {server = HttpServer.create(InetSocketAddress(port), 0)}
        catch(ex: BindException) {
            System.err.println("Port $port is already in use!")
            exitProcess(-1)
        }

        // Create Context
        val context = server!!.createContext("/", RequestHandler())

        // Apply Filter
        //context.filters.add(ParameterFilter())
        // NOTE: filters can provide easy access to parameters depending on requirements

        // Set Executor
        server!!.executor = null

        // Start Server
        server!!.start()
        running = true
    }

    fun stop() {

        // Not Running
        if(!running) return

        // Stop Server
        server!!.stop(0)
        running = false
    }

}