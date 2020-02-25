import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

/**
 * This file contains all singletons used in the server.
 *
 * ChatHistory is the Observable that sends all messages to observers,
 *
 * Users contains a list of all the users currently on the server, it deletes them after they've disconnected
 *
 * ChatConsole prints all the messages on the server console
 *
 * TopChatter keeps a record of messages sent and prints a list of users and their messagecount when asked to
 *
 * **/

object ChatHistory: Observable {
    var messages = mutableListOf<ChatMessage>()
    var connectors = mutableSetOf<ChatConnector>()

    override fun toString():String{
        //return the whole chat history as a nicely formatted string
        var i = 0
        var msgListFormatted = mutableListOf<String>()
        for(u in messages){
            var msg = messages.toList()[i]
            //var msgFormatted = ("$msg \n \r")
            var msgFormatted = ("$msg \n")
            msgListFormatted.add(msgFormatted)
            i++
        }
        return msgListFormatted.toString()
    }

    /*

    4.10 Noticed, can't send Strings ->
        Changed required data type to Json for connectors.messageUpdate(!---JSON HERE---!)

    5.10 Didn't

     */

    @UnstableDefault
    @Synchronized
    fun updateMessages(item: String){
        val msgParsed = Json.parse(ChatMessage.serializer(),item)

        messages.add(msgParsed)
        connectors.forEach{ it.messageUpdate(msgParsed)}
        TopChatter.messageUpdate(msgParsed)
    }

    override fun register(who: ChatConnector) {
        connectors.add(who)
    }

    override fun deregister(who: ChatConnector) {
        connectors.remove(who)
    }
}

object Users{
    var users = mutableSetOf<String>()

    override fun toString():String{
        return users.toString()
    }

    fun addUser(name: String):Boolean{
        return if(!users.contains(name)){
            users.add(name)
            true
        }else{
            false
        }
    }

    fun removeUser(name: String){
        users.remove(name)
    }

    fun getSize():Int{
        return users.size
    }
}

object ChatConsole:MessageObserver {
    override fun messageUpdate(item: ChatMessage) {
        println(item)
    }
}

object TopChatter:MessageObserver{

    var userMap = mutableMapOf<String, Int>()
    var totalMessages = 0

    override fun messageUpdate(item: ChatMessage) {
        var i = userMap[item.username]
        if(i!=null){
            i++
            userMap[item.username]=i
        }
        totalMessages++

        println("Messages: $totalMessages")
        println(item.username + ": " + userMap[item.username])
        println("Current users: $Users, ${Users.getSize()}")
    }

    fun userMapReg(name: String){
        userMap[name] = 0
    }

    override fun toString(): String{
        var results = userMap.toList().sortedBy{ (_, value) -> value}.reversed().toMap()
        var chatters = "Top chatters are: \n"
        //var chatters = "Top chatters are:"

        for (r in results){
            chatters += "${r.key}, Messages: ${r.value} \n"
            //chatters += "User: ${r.key}, Messages: ${r.value}"
        }

        return chatters
    }

}