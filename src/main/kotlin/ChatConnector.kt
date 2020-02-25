import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.*
import kotlinx.serialization.stringify
import java.io.PrintWriter
import java.util.*
import kotlin.NoSuchElementException

class ChatConnector(val ins: Scanner, val out: PrintWriter):Runnable, MessageObserver {

    /**
        Implement a ChatConnector that reads user input and creates an object of ChatMessage type.
        Your application should have a main() method where you create an instance of the ChatConnector and execute its run method.
    **/

    @UnstableDefault
    override fun run(){

        ChatHistory.register(this)

        //out.println("Please enter your username: ")
        //out.flush()
        var userName = ins.nextLine()

    while(true){
        if(Users.addUser(userName) && userName!=""){
            TopChatter.userMapReg(userName)
            //out.println("Hello $userName nice to meet you \n \r " +
            //       "${Users.getSize()} People currently online: ${Users.toString()}")
            break
        }else{
            //out.println("Username already in use, please enter a valid username \n \r")
            userName = ins.nextLine()
        }
    }

    while(true){
        try{
        val line = ins.nextLine().toString()

        if(line==""){
            continue
        }
        else if(line == "/history") {
            out.println(ChatHistory.toString())
        }
        else if(line=="/users"){
            out.println("Current users: ${Users.toString()}")
        }
        else if(line == "/top"){
            out.println(TopChatter.toString())
        }
        else if(line == "/quit"){
            out.println("See you later $userName")
            Users.removeUser(userName)
            break
        }
        else{
            sendMessage(userName,line)
        }
    }
    catch(e: NoSuchElementException){
    println("User $userName left with error $e, removing user")
        Users.removeUser(userName)
        ChatHistory.deregister(this)
        break }
    }
        ChatHistory.deregister(this)
    }

    override fun messageUpdate(item: ChatMessage) {
        out.println(item)
    }

    @UnstableDefault
    @Synchronized
    private fun sendMessage(name: String, line: String){

        val message=ChatMessage(name, line)
        val msgAsJson= Json.stringify(ChatMessage.serializer(),message)
        println("JSON: $msgAsJson")
        println("String $message")

        ChatHistory.updateMessages(msgAsJson)
        ChatConsole.messageUpdate(message)
    }
}