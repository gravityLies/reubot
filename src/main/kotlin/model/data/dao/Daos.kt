package model.data.dao

import abstracthibernate.AbstractHibernateDao
import model.data.entity.*
import org.hibernate.SessionFactory
import javax.persistence.NoResultException

class ClientDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<Client, Long>(sessionFactory, Client()) {

    fun getByChatId(chatId: Long): Client {
        try {
            sessionFactory.openSession().use { session ->
                val sql = "SELECT * FROM client WHERE client_chat_id = :id"
                val query = session.createNativeQuery(sql)
                    .addEntity(Client::class.java)
                    .setParameter("id", chatId)

                val client = query.singleResult as Client
                session.close()
                return client
            }
        } catch (e: NoResultException) {
            return Client().apply { clientChatId = 0L }
        }
    }
}

class PlatformDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<Platform, Long>(sessionFactory, Platform()) {

    fun getByName(name: String): Platform {
        try {
            sessionFactory.openSession().use { session ->
                val hql = "from Platform p where p.name = '$name'"
                val query = session.createQuery(hql)

                val platform = query.singleResult as Platform
                session.close()
                return platform
            }
        } catch (e: NoResultException) {
           return Platform().apply { id = 0L }
        }
    }
}

class PropertyDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<Property, Long>(sessionFactory, Property()) {

    fun getPropertiesValues(clientId: Long): List<HashMap<String, String>> {
        try {
            sessionFactory.openSession().use { session ->
                val hql = "select new map (pp.propertyName, p.value) " +
                        "from Property p " +
                        "join PlatformProperty pp on p.platformPropId = pp.id " +
                        "and p.clientId = $clientId"
                val query = session.createQuery(hql)

                return query.resultList as List<HashMap<String, String>>
            }
        } catch (e: NoResultException) {
            return emptyList()
        }
    }

    fun getValueByClientAndPlatformPropId(
        clientId: Long,
        platformPropId: Long
    ): String {

        val property = Property()
        try {
            sessionFactory.openSession().use { session ->
                val hql = "from Property p " +
                        "where p.clientId = $clientId and p.platformPropId = $platformPropId"
                val query = session.createQuery(hql)

                property.value = (query.singleResult as Property).value
            }
        } catch (e: NoResultException) {
            property.value = "none"
        }

        return property.value!!
    }

    fun updateValueByClientAndPlatformPropId(
        clientId: Long,
        platformPropId: Long,
        newValue: String
    ) {
        try {
            sessionFactory.openSession().use { session ->
                session.beginTransaction()
                val hql = "update Property p set p.value = '$newValue' " +
                        "where p.clientId = $clientId and p.platformPropId = $platformPropId"

                session.createQuery(hql).executeUpdate()
                session.transaction.commit()
            }
        } catch (e: NoResultException) {
            return
        }
    }
}

class PlatformPropertyDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<PlatformProperty, Long>(sessionFactory, PlatformProperty()) {

    fun getByName(name: String): PlatformProperty {
        try {
            sessionFactory.openSession().use { session ->
                val sql = "SELECT * FROM platform_property WHERE property_name = :name"
                val query = session.createNativeQuery(sql)
                    .addEntity(PlatformProperty::class.java)
                    .setParameter("name", name)

                val platformProperty = query.singleResult as PlatformProperty
                session.close()
                return platformProperty
            }
        } catch (e: NoResultException) {
            return PlatformProperty().apply { propertyName = "none" }
        }
    }
}

class SubscribeDao(sessionFactory: SessionFactory) :
    AbstractHibernateDao<Subscribe, Long>(sessionFactory, Subscribe()) {

    fun getRecord(
        clientId: Long,
        platformId: Long,
        groupName: String
    ): Subscribe {
        try {
            sessionFactory.openSession().use { session ->
                val hql = "from Subscribe s " +
                        "where s.clientId = $clientId and s.platformId = $platformId and s.group = '$groupName'"
                val query = session.createQuery(hql)

                return query.singleResult as Subscribe
            }
        } catch (e: NoResultException) {
            return Subscribe().apply { group = "none" }
        }
    }

    fun getClientSubRecords(clientId: Long, platformId: Long): List<Subscribe> {
        try {
            sessionFactory.openSession().use { session ->
                val hql = "from Subscribe s " +
                        "where s.clientId = $clientId and s.platformId = $platformId"
                val query = session.createQuery(hql)

                return query.resultList as List<Subscribe>
            }
        } catch (e: NoResultException) {
            return listOf()
        }
    }
}