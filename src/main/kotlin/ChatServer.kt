import java.io.PrintWriter
import java.net.ServerSocket
import java.util.*

class ChatServer() {

    fun serve() {
        val ss = ServerSocket(34300, 10)

        while(true) {
            println("accepting")
            val s = ss.accept()
            println("accepted")

            val out = PrintWriter(s.getOutputStream(), true)
            val ins = Scanner(s.getInputStream())

            Thread(ChatConnector(ins, out)).start()}
    }
}