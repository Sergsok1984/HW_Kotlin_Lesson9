class ChatNotFoundException(message: String) : RuntimeException(message)
class MessageNotFoundException(message: String) : RuntimeException(message)

class ChatService {
    private val chats = mutableListOf<Chat>()

    private var currentUserId = ""
    fun login(userId: String) {
        currentUserId = userId
    }

    fun logoff() {
        currentUserId = ""
    }

    fun getCurrentUserId(): String {
        return currentUserId
    }

    fun getChatPresentation(chat: Chat): String {
        return "id чата: ${chat.chatId}; с ${
            if (!chat.userId1.equals(currentUserId, true)) chat.userId1 else chat.userId2
        }; последнее сообщение: <${chat.messages.lastOrNull() ?: "нет сообщений"}>"
    }

    private fun getChatWithPeer(peerId: String): Chat {
        return chats
            .singleOrNull {
                (it.userId1.equals(peerId, true) && it.userId2.equals(currentUserId, true))
                        || (it.userId1.equals(currentUserId, true) && it.userId2.equals(peerId, true))
            }
            ?: run {
                val newChatId = (chats.lastOrNull()?.chatId ?: 0) + 1
                chats.add(Chat(chatId = newChatId, userId1 = currentUserId, userId2 = peerId))
                chats.last()
            }

    }

    fun getChatsByUserId(userId: String): List<Chat> {
        return chats
            .filter { (it.userId1.equals(userId, true) || it.userId2.equals(userId, true)) }
    }

    fun getUnreadChatsCount(userId: String): Int {
        return chats
            .filter {
                (it.userId1.equals(userId, true) || it.userId2.equals(userId, true))
                        && (it.messages.any { it.toId.equals(userId, true) && !it.isRead })
            }
            .size
    }

    fun deleteChat(chatId: Int) {
        chats.removeIf { it.chatId == chatId }
    }

    fun sendMessage(toId: String, text: String) {
        val chat = getChatWithPeer(peerId = toId)
        val newMessageId = (chat.messages.lastOrNull()?.messageId ?: 0) + 1
        chat.messages.add(Message(messageId = newMessageId, fromId = currentUserId, toId = toId, text = text))
    }

    fun editMessage(chatId: Int, messageId: Int, text: String) {
        chats.singleOrNull { it.chatId == chatId }
            .let { it?.messages ?: throw ChatNotFoundException("Чат id = $chatId не найден!") }
            .singleOrNull { it.messageId == messageId }
            .let {
                it ?: throw MessageNotFoundException("Сообщение id = $messageId не найдено!")
                it.text = text
            }
    }

    fun getMessageByMessageId(chatId: Int, messageId: Int): Message =
        chats.singleOrNull { it.chatId == chatId }
            .let { it?.messages ?: throw ChatNotFoundException("Чат id = $chatId не найден!") }
            .singleOrNull { it.messageId == messageId }
            ?: throw MessageNotFoundException("Сообщение id = $messageId не найдено!")

    fun getMessages(chatId: Int, messageId: Int, count: Int): List<Message> {
        return chats.singleOrNull { it.chatId == chatId }
            .let { it?.messages ?: throw ChatNotFoundException("Чат id = $chatId не найден!") }
            .takeWhile { messageId != it.messageId }
            .takeLast(count)
            .apply {
                filter { it.toId.equals(currentUserId, true) }
                    .forEach { it.isRead = true }
            }
    }

        fun deleteMessage(chatId: Int, messageId: Int) {
            chats.singleOrNull { it.chatId == chatId }
                .apply {
                    this?.messages ?: throw ChatNotFoundException("Чат id = $chatId не найден!")
                    this.messages.removeIf { it.messageId == messageId }
                    chats.removeIf { this.messages.isEmpty() }
                }
        }
    }

