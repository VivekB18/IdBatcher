import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.JsonArray
import io.vertx.kotlin.core.json.JsonObject
import io.vertx.kotlin.core.json.jsonObjectOf
import java.lang.RuntimeException

fun toQuery(vararg matcher: Pair<String, Any?>) = io.vertx.kotlin.core.json.JsonObject(*matcher)
fun configureMongoClientFromConfig(injectedConfig: JsonObject, databaseName: String, namespace: String): JsonObject {
    var mongoConfig = JsonObject()
    if (injectedConfig.getBoolean("MONGO_${namespace}_STANDALONE", true)) {
        mongoConfig.put("host", injectedConfig.getString("MONGO_${namespace}_HOST", "mongo"))
            .put("port", injectedConfig.getInteger("MONGO_${namespace}_PORT", 27017))
    } else {
        //set up as replica set
        var hosts = JsonArray()
        hosts.add(JsonObject(
            "host" to injectedConfig.getString("MONGO_${namespace}_RS_HOST_1"),
            "port" to injectedConfig.getInteger("MONGO_${namespace}_RS_PORT_1")
        ))
        hosts.add(JsonObject(
            "host" to injectedConfig.getString("MONGO_${namespace}_RS_HOST_2"),
            "port" to injectedConfig.getInteger("MONGO_${namespace}_RS_PORT_2")
        ))
        hosts.add(JsonObject(
            "host" to injectedConfig.getString("MONGO_${namespace}_RS_HOST_3"),
            "port" to injectedConfig.getInteger("MONGO_${namespace}_RS_PORT_3")
        ))
        mongoConfig = JsonObject(
            "hosts" to hosts
            // "replicaSet" to injectedConfig.getString("MONGO_${namespace}_RS_NAME")
        )
    }
    if (injectedConfig.getBoolean("MONGO_${namespace}_AUTH", false)) {
        mongoConfig.put("username", injectedConfig.getString("MONGO_${namespace}_AUTH_USERNAME"))
        mongoConfig.put("password", injectedConfig.getString("MONGO_${namespace}_AUTH_PASSWORD"))
        mongoConfig.put("authSource", injectedConfig.getString("MONGO_${namespace}_AUTH_DATABASE"))
        //NOTE_ We will be using the default for mongo at this time
        //@see https_//docs.mongodb.com/manual/core/security-scram/#authentication-scram
    }
    if (injectedConfig.getBoolean("MONGO_${namespace}_SSL", false)) {
        mongoConfig.put("ssl", true)
        mongoConfig.put("sslInvalidHostNameAllowed", false)
    }
    mongoConfig.put("db_name", databaseName)
    return mongoConfig
}
fun configureMongoClientFromConfig(mongoConfig: MongoConfig, databaseName: String): JsonObject {
    var config = JsonObject()
    if(mongoConfig.isStandAlone()){
        //we can assume not null
        config.put("host",   mongoConfig.standAlone!!.host)
            .put("port",   mongoConfig.standAlone.port)
    }
    else {
        //set up as replica set
        config = jsonObjectOf(
            //fully qualified name because we have kotlin extension imported
            "hosts" to io.vertx.core.json.JsonArray(mongoConfig.hosts!!.map { it.toJson() })
        )
        if(mongoConfig.replicaSet != null)
        {
            config.put("replicaSet", mongoConfig.replicaSet )
        }
    }
    if (mongoConfig.isAuthEnabled()) {
        config.put("username", mongoConfig.auth!!.username)
        config.put("password",  mongoConfig.auth!!.password)
        config.put("authSource",  mongoConfig.auth!!.authSource)
        val auth = mongoConfig.auth
        if(auth!!.authMechanism != null)
        {
            config.put("authMechanism",  auth!!.authMechanism)
        }
        //NOTE_ We will be using the default for mongo at this time
        //@see https_//docs.mongodb.com/manual/core/security-scram/#authentication-scram
    }
    config.put("db_name", databaseName)
    return config
}
private fun MongoConfig.isAuthEnabled(): Boolean {
    return this.auth != null
}
data class MongoConfig(
    // Single Cluster Settings
    val standAlone: Host?,
    // Multiple Cluster Settings
    val hosts: List<Host>?,
    val replicaSet: String?,
    var serverSelectionTimeoutMS: Long?,
    // Credentials / Auth
    var auth: Auth?,
    // Socket Settings
    var heartbeatSocket: HeartbeatSocket?,
    // Connection Pool Settings
    var pool: Pool?
)
fun mongoConfigOf(host: String? = "localhost", port: Int? = 27017, hosts: List<Pair<String, Int>>? = null, replicaSet: String? = null): MongoConfig {
    // check for replica set
    if(hosts != null)
    {
        val h = hosts.map { Host(it.first, it.second) }
        return MongoConfig(null, h, replicaSet, null, null, null, null)
    }
    // check for standalone
    if(host != null && port != null)
    {
        var standAlone = Host(host, port)
        return MongoConfig(standAlone, null, null, null, null, null, null)
    }
    throw RuntimeException()
}
fun MongoConfig.isStandAlone(): Boolean {
    return this.standAlone != null
}
data class Host(val host: String?, val port: Int?)
fun Host.toJson() = jsonObjectOf("host" to this.host, "port" to this.port)
data class HeartbeatSocket(val connectTimeoutMS: Int?, val socketTimeoutMS: Int?, val receiveBufferSize: Int?, val keepAlive: Boolean?)
data class Auth(val username: String, val password: String, val authSource: String, val authMechanism: String?,
                val gssapiServiceName: String?, val heartbeatFrequencyMS: Int?, val minHeartbeatFrequencyMS: Int?)
data class Pool(val maxPoolSize: Int?, val minPoolSize: Int?, val maxIdleTimeMS: Int?, val maxLifeTimeMS: Long?, val waitQueueMultiple: Int?,
                val waitQueueTimeoutMS: Long?,
                val maintenanceFrequencyMS: Long?,
                val maintenanceInitialDelayMS: Long?)




