package model.data.entity

import javax.persistence.*

@Entity
@Table(name = "client")
open class Client {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "client_chat_id")
    open var clientChatId: Long? = null


}

@Entity
@Table(name = "platform")
open class Platform {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "name")
    open var name: String? = null


}

@Entity
@Table(name = "platform_property")
open class PlatformProperty {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @JoinColumn(name = "platform_id")
    @OneToOne
    open var platformId: Platform? = null

    @Column(name = "property_name")
    open var propertyName: String? = null
}

@Entity
@Table(name = "property")
open class Property {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @JoinColumn(name = "client_id")
    @OneToOne
    open var clientId: Client? = null

    @JoinColumn(name = "platform_prop_id")
    @OneToOne
    open var platformPropId: PlatformProperty? = null

    @Column(name = "value")
    open var value: String? = null


}