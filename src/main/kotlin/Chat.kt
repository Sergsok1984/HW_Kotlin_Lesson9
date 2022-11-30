data class Chat(
    val chatId: Int = 0,
    val userId1: String = "",
    val userId2: String = "",
    val messages: MutableList<Message> = mutableListOf()
) {
    override fun toString(): String {
        return "Id: $chatId"
    }
}
