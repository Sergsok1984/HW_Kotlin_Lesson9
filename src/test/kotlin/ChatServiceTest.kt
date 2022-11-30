import org.junit.Test

import org.junit.Assert.*

class ChatServiceTest {

    @Test
    fun getUnreadChatsCount() {
        val chatService = ChatService()

        chatService.login("Сергей")

        chatService.sendMessage("Олег", "Привет, Олег!")
        chatService.sendMessage("Петр", "Как дела, Петр")

        val result0 = chatService.getUnreadChatsCount(chatService.getCurrentUserId())

        chatService.login("Олег")
        val result1 = chatService.getUnreadChatsCount(chatService.getCurrentUserId())

        assertEquals(0, result0)
        assertEquals(1, result1)
    }

    @Test
    fun getMessages() {
        val chatService = ChatService()

        chatService.login("Сергей")

        chatService.sendMessage("Олег", "Привет, Олег!")
        chatService.sendMessage("Олег", "Еще раз привет, Олег!")
        chatService.sendMessage("Петр", "Как дела, Петр?")
        chatService.sendMessage("Петр", "Еще раз как дела, Петр?")

        val chatId = 1
        val messageId = 1
        val count = 1

        val messages = chatService.getMessages(chatId, messageId, count)
        val result = messages[0].text

        assertEquals("Еще раз привет, Олег!", result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessage() {
        val chatService = ChatService()

        chatService.login("Сергей")

        chatService.sendMessage("Олег", "Привет, Олег!")
        chatService.sendMessage("Олег", "Еще раз привет, Олег!")
        chatService.sendMessage("Петр", "Как дела, Петр?")
        chatService.sendMessage("Петр", "Еще раз как дела, Петр?")

        val chatId = 1
        val messageId = 1

        val messageToDelete = chatService.getMessageByMessageId(chatId, messageId)
        val result = messageToDelete.text

        assertEquals("Привет, Олег!", result)

        chatService.deleteMessage(chatId, messageId)
        val messageAfterDelete = chatService.getMessageByMessageId(chatId, messageId)
    }

    @Test
    fun deleteChat() {

        val chatService = ChatService()

        chatService.login("Сергей")

        chatService.sendMessage("Олег", "Привет, Олег!")
        chatService.sendMessage("Олег", "Еще раз привет, Олег!")
        chatService.sendMessage("Петр", "Как дела, Петр?")
        chatService.sendMessage("Петр", "Еще раз как дела, Петр?")

        val chatId = 1
        val messageId = 1

        val result2 = chatService.getChatsByUserId(chatService.getCurrentUserId()).size
        chatService.deleteChat(chatId)
        val result1 = chatService.getChatsByUserId(chatService.getCurrentUserId()).size

        assertEquals(2, result2)
        assertEquals(1, result1)
    }

    @Test
    fun editMessage() {
        val chatService = ChatService()

        chatService.login("Сергей")

        chatService.sendMessage("Олег", "Привет, Олег!")
        chatService.sendMessage("Петр", "Как дела, Петр?")

        val chatId = 1
        val messageId = 1
        val text = "Новое сообщение"

        chatService.editMessage(chatId, messageId, text)

        val message = chatService.getMessageByMessageId(chatId, messageId)
        val result = message.text

        assertEquals("Новое сообщение", result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun editMessage_ChatNotFoundException() {
        val chatService = ChatService()

        chatService.login("Сергей")

        chatService.sendMessage("Олег", "Привет, Олег!")
        chatService.sendMessage("Петр", "Как дела, Петр?")

        val chatId = 1 + 2
        val messageId = 1
        val text = "Новое сообщение"

        chatService.editMessage(chatId, messageId, text)
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessage_MessageNotFoundException() {
        val chatService = ChatService()

        chatService.login("Сергей")

        chatService.sendMessage("Олег", "Привет, Олег!")
        chatService.sendMessage("Петр", "Как дела, Петр?")

        val chatId = 1
        val messageId = 1 + 2
        val text = "Новое сообщение"

        chatService.editMessage(chatId, messageId, text)
    }

    @Test
    fun chatPresentation() {
        val chatService = ChatService()

        chatService.login("Сергей")

        chatService.sendMessage("Олег", "Привет, Олег!")
        chatService.sendMessage("Петр", "Как дела, Петр?")

        val chat = chatService.getChatsByUserId(chatService.getCurrentUserId())[0]

        val chatPresentation = chatService.getChatPresentation(chat)

        assertEquals(
            "id чата: 1; с Олег; последнее сообщение: id сообщения: 1; от: Сергей; кому: Олег; сообщение: Привет, Олег!",
            chatPresentation
        )
    }
}