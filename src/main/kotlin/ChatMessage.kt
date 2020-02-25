import kotlinx.serialization.Serializable
import java.time.*
import java.time.format.DateTimeFormatter

@Serializable
class ChatMessage(val username: String = "undefined",
                  val msg: String = "",
                  private val timestamp: String = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))) {
    override fun toString(): String {
        return ("$username: $msg [$timestamp]")
    }
}