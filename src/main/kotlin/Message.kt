data class Message(
    var messageId: Int = 0,
    var text: String = "",
    val fromId: String = "",
    val toId: String = "",
    var isRead: Boolean = false
) {
    override fun toString(): String {
        return "id сообщения: $messageId; от: $fromId; кому: $toId; сообщение: $text"
    }
}