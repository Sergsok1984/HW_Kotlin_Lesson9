fun readString(title: String): String {
    print(title)
    return readln()
}

fun readInt(title: String): Int? {
    print(title)
    return readln().toIntOrNull()
}

fun main() {

    val chatService = ChatService()

    chatService.login("Сергей")

    chatService.sendMessage("Олег", "Привет, Олег!")
    chatService.sendMessage("Петр", "Как дела, Петр?")

    val listOfCommands = "Список команд:\n" +
            "1 - ОТПРАВИТЬ СООБЩЕНИЕ\n" +
            "2 - РЕДАКТИРОВАТЬ СООБЩЕНИЕ\n" +
            "3 - УДАЛИТЬ СООБЩЕНИЕ\n" +
            "4 - ПРОСМОТР ЧАТА\n" +
            "5 - УДАЛИТЬ ЧАТ\n" +
            "6 - LOGIN\n" +
            "7 - LOGOFF\n" +
            "8 - ВЫЙТИ"

    println(listOfCommands)

    while (true) {

        if (chatService.getCurrentUserId() == "") {
            chatService.login(readString("Введите Ваше имя: "))
            continue
        }

        println(
            "\nЧат с ${chatService.getCurrentUserId()} " +
                    "(непрочитано: ${chatService.getUnreadChatsCount(chatService.getCurrentUserId())}):"
        )

        chatService.getChatsByUserId(chatService.getCurrentUserId())
            .forEach { println(chatService.getChatPresentation(it)) }

        when (readln()) {
            "1" -> {
                val toId = readString("id получателя: ")
                val text = readString("Сообщение: ")
                chatService.sendMessage(toId, text)
            }
            "2" -> {
                val chatId = readInt("id чата: ") ?: 0
                val messageId = readInt("id сообщения: ") ?: 0
                val text = readString("Новое сообщение: ")
                chatService.editMessage(chatId, messageId, text)
            }
            "3" -> {
                val chatId = readInt("id чата: ") ?: 0
                val messageId = readInt("id сообщения: ") ?: 0
                chatService.deleteMessage(chatId, messageId)
            }
            "4" -> {
                val chatId = readInt("id чата: ") ?: 0
                val messageId = readInt("id последнего сообщения: ") ?: 0
                val count = readInt("Количество сообщений: ") ?: 0
                println("Список сообщений чата $chatId:")
                chatService.getMessages(chatId, messageId, count).forEach { println(it) }
            }
            "5" -> {
                val chatId = readInt("id чата: ") ?: 0
                chatService.deleteChat(chatId)
            }
            "6" -> chatService.login(readString("Введите Ваше имя: "))
            "7" -> chatService.logoff()
            "8", "Q" -> break
            else -> print("Некорректная команда!\n")
        }
        println(listOfCommands)
    }
}