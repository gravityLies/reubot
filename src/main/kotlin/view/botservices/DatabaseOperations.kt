package view.botservices

import model.data.entity.*
import model.service.*
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.slf4j.LoggerFactory

class DatabaseOperations(private val sessionFactory: SessionFactory) {
    private val clientService = ClientService(sessionFactory)
    private val platformService = PlatformService(sessionFactory)
    private val propertyService = PropertyService(sessionFactory)
    private val platformPropertyService = PlatformPropertyService(sessionFactory)

    private val logger = LoggerFactory.getLogger(DatabaseOperations::class.java)

    fun getClientsDiscordProperties(clientChatId: Long): HashMap<String, String> {
        val configList = HashMap<String, String>()
        val client = clientService.getByChatId(clientChatId)
        val config = propertyService.getPropertiesValues(client.id!!).toMutableList()

        if (client.id == 0L || config.isEmpty()) {
            config.apply {
                clear()
                add(hashMapOf("error" to "list are empty"))
            }
        }

        config.forEach { map ->
            val configRow = mutableListOf<String>()
            map.forEach { settingMap ->
                configRow.add(settingMap.value)
            }

            configList[configRow[0]] = configRow[1]
        }

        return configList
    }

    fun addNewClient(chatId: Long) {
        val client = Client().apply {
            clientChatId = chatId
        }

        clientService.create(client)
        logger.info("Created new client [${client.clientChatId}]")

        // base configuration for new client
        propertyService.apply {
            create(Property().apply {
                clientId = client
                platformPropId = platformPropertyService.getByName("prefix")
                value = "-"
            })
            create(Property().apply {
                clientId = client
                platformPropId = platformPropertyService.getByName("channel_id")
                value = "none"
            })
            create(Property().apply {
                clientId = client
                platformPropId = platformPropertyService.getByName("group")
                value = "none"
            })
        }
    }

    fun clientIsExists(chatId: Long): Boolean =
        clientService.exists("client_chat_id", chatId)
}

fun main() { //TODO remove main here
    DatabaseOperations(Configuration().configure().buildSessionFactory()).addNewClient(620682596564598797); println("1")
    DatabaseOperations(Configuration().configure().buildSessionFactory()).addNewClient(671379555897966630); println("2")
}