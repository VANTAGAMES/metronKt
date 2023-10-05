package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.uuid.*
import kotlinx.uuid.exposed.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object Users: KotlinxUUIDTable() {
    val username = varchar("username", 16)
    val email = varchar("email", 320)
    val locale = varchar("locale", 8)
    init { transaction { SchemaUtils.create(this@Users) } }
}

class User(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    companion object : KotlinxUUIDEntityClass<User>(Users)
    var username by Users.username
    var email by Users.email
    var locale by Users.locale
}

@Serializable
data class UserSession(val id: Int)

@Serializable
data class UserInfo(
    val id: String,
    val email: String,
    @SerialName("verified_email") val verifiedEmail: Boolean,
    val name: String,
    @SerialName("given_name") val username: String,
    @SerialName("family_name") val studentId: String,
    val picture: String,
    val locale: String,
    val hd: String? = null
)
