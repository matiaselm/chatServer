interface Observable {
    fun register(who: ChatConnector)
    fun deregister(who: ChatConnector)
}

interface MessageObserver{
    fun messageUpdate(item: ChatMessage)
}