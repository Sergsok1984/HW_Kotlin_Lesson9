data class Message(
    val messageId: Int = 0,
    val fromId: String = "",
    val toId: String = "",
    var text: String = "",
    var isRead: Boolean = false
) {
    override fun toString(): String {
        return "id сообщения: $messageId; от: $fromId; кому: $toId; сообщение: $text"
    }
}