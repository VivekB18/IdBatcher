import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import java.util.*
import kotlin.system.measureTimeMillis
/*
class ChannelSend {
    val client = KMongo.createClient()
    val database = client.getDatabase("testBase")
    val collection = database.getCollection<uuidBatch>("ThreadTestWithNumberedChannels")

//    fun main() = runBlocking {
//        val time = measureTimeMillis {
//            val idProducer = produceBatches()
//            val mongoChannel = Channel<uuidBatch>()
//            batchMaker(idProducer = idProducer, mongoChannel = mongoChannel)
//            batchMaker(idProducer = idProducer, mongoChannel = mongoChannel)
//            batchMaker(idProducer = idProducer, mongoChannel = mongoChannel)
//            batchMaker(idProducer = idProducer, mongoChannel = mongoChannel)
//            batchMaker(idProducer = idProducer, mongoChannel = mongoChannel)
//            sendToMongo(mongoChannel)
//            sendToMongo(mongoChannel)
//            sendToMongo(mongoChannel)
//        }
//        println("time elapsed: $time")
//    }


    fun main() = runBlocking {
        val time = measureTimeMillis {

            val mongoChannel = Channel<uuidBatch>()
            val uuidChannel = Channel<UUID>()
            produceBatches(uuidChannel)
            produceBatches(uuidChannel)
            produceBatches(uuidChannel)
            produceBatches(uuidChannel)
            produceBatches(uuidChannel)

            repeat(5) {
                async { batchMaker(idProducer = uuidChannel, mongoChannel = mongoChannel) }
            }
            for (batch in mongoChannel) {
                //println("reached inside send")
                //println(batch)
                collection.insertOne(batch)
            }

//            sendToMongo(batchProducer1)
//            sendToMongo(batchProducer2)
//            sendToMongo(batchProducer3)
//            sendToMongo(batchProducer4)
//            sendToMongo(batchProducer5)
        }
        println("time elapsed: $time")
    }



    suspend fun CoroutineScope.produceBatches(uuidChannel: Channel<UUID>): ReceiveChannel<UUID> = produce {

            for (i in 1..80000000) {
                println(i)
                uuidChannel.send(UUID.randomUUID())
            }
        close()
    }

    suspend fun sendIdsToBatchMaker(idSender: Channel<UUID>) {

    }

    suspend fun batchMaker(idProducer: ReceiveChannel<UUID>, mongoChannel: Channel<uuidBatch>) {
        val block = mutableListOf<UUID>()
        for (id in idProducer)
        {
            block.add(id)
            if (block.size >= 500) {
                //println(block)
                mongoChannel.send(uuidBatch(used = false, service = "", block = block))
              //  println("sent to Mongo function")
            }
        }


    }

//    suspend fun sendToMongo(mongoChannel: Channel<uuidBatch>) {
//
//
//    }
}
*/

/**
 *
 * Make multiple producers
 * have them feed to one method and then send all uuids to 1 one channel that is read by multiple batcher
 **/

class ChannelSend {
    val dummyUUIDBatch = uuidBatch(false, "", mutableListOf<UUID>())
    val client = KMongo.createClient()
    val database = client.getDatabase("testBase")
    val collection = database.getCollection<uuidBatch>("ThreadTestWithNumberedChannels")
    //val batchChannel = Channel<uuidBatch>()

    fun main() {
        oneProducer()
    }

    fun oneProducer() = runBlocking {
        val numberOfIds = 800000
        val batchChannel = produceBatches(dummyUUIDBatch, numberOfIds)
        sendToMongo(batchChannel = batchChannel)
    }

    fun CoroutineScope.produceBatches(dummyUUIDBatch: uuidBatch, numberOfIds: Int): ReceiveChannel<uuidBatch> = produce {
        for (i in 1..numberOfIds) {
            println(i)
            send(createOneBatch(dummyUUIDBatch))
        }
        close()
    }

    fun createOneBatch(dummyUUIDBatch: uuidBatch): uuidBatch {
        val blockValues = mutableListOf<UUID>()
        for (i in 1..500)
        {
            blockValues.add(UUID.randomUUID())
        }
        dummyUUIDBatch.block = blockValues
        return dummyUUIDBatch
    }

    suspend fun sendToMongo(batchChannel: ReceiveChannel<uuidBatch>) {
        for (batch in batchChannel) {
            collection.insertOne(batch)
        }
    }
}