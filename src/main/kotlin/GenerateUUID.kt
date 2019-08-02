import io.vertx.kotlin.core.json.jsonObjectOf
import java.util.UUID
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.get
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.litote.kmongo.*


/**
 * Start with making one block
 *send that to mongo
 * then focus on creating multiple blocks and sending to mongo
 */




class GenerateUUID {
    /**
     * Creating one new batch
     */
    val client = KMongo.createClient()
    val database = client.getDatabase("testBase")
    val collection = database.getCollection<uuidBatch>("channeltest")


    fun fillBatch(batch: uuidBatch) {
       // println("called fill")
        val blockValues = mutableListOf<UUID>()
        for (i in 0..500)
        {
            blockValues.add(UUID.randomUUID())
        }
        batch.block = blockValues
    //    println("filled batch")
    }

    fun printBatch(batch: uuidBatch) {
        println(batch)
    }

    fun sendBatch(batch: uuidBatch) {
       // println( "called the insert method" )
        collection.insertOne(batch)
       // println("sent")
    }

    suspend fun sendManyBatches(channel: ReceiveChannel<uuidBatch>) {

        for (batch in channel) {
            fillBatch(batch)
           // printBatch(batch)
            sendBatch(batch)
        }

    }

    fun convertBatchToJson(batch: uuidBatch): JsonObject {
        println("Called batch to json")
        return jsonObjectOf(
            "used" to batch.used,
            "service" to batch.service,
            "block" to batch.block
        )
    }

    fun convertJsonToBatch(json: JsonObject): uuidBatch {
        return uuidBatch(used= json.get("used"), service= json.get("service"), block= json.get("block"))
    }
/*
    suspend fun retrieveBatch(batch: uuidBatch, service: String): uuidBatch? {
        val retrievalJson = collection.findOneAndUpdate(
            BSONObject(
                "used" to false,
                "service" to ""
            ),
            Bson(
                "used" to true,
                "service" to service
            )
        )

        return if (retrievalJson != null) {
                convertJsonToBatch(retrievalJson)
            } else {
                null
            }

    }
        */
}