import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.jsonObjectOf
import org.litote.kmongo.*

class test {

    val client = KMongo.createClient()
    val database = client.getDatabase("testBase")
    val collection = database.getCollection<uuidBatch>("test")

    fun insertItem(batch: uuidBatch) {
        collection.insertOne(batch)
    }


//    fun createCollection() {
//        client.createCollection("testCollection", {})
//        println("called")
//    }

//    suspend fun addItem(event: RoutingContext) {
//        client.insert("test", jsonObjectOf(
//        "key1" to "value1"), {})
//        event.response().setStatusCode(200).end("sent")
//    }
//
//    fun addItem() {
//        client.insert("testCollection", jsonObjectOf(
//            "key1" to "value1"), {})
//    }

}