import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import java.util.*
import kotlin.system.measureTimeMillis


/**
 * Class created to test the production of ids with producer channels - Not Active
 */
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
 * Plan:
 * Make multiple producers
 * have them feed to one method and then send all uuids to 1 one channel that is read by multiple batcher
 **/

/**
 * Active Class
 *
 * The init container that should run one time
 * The class has producers that generate a batch of ids instead of a single id at a time
 * The program generates 800,000 batches which is equivalent to 40,000,000,000 uuids
 */
class ChannelSend {

    /**
     *  Necessary variables required for the class,the batch is an empty batch that gets continually filled and emptied and sent to the database
     *  The rest of the variables are required to send to Mongo
     */
    val dummyUUIDBatch = uuidBatch(false, "", mutableListOf<UUID>())
    val client = KMongo.createClient()
    val database = client.getDatabase("testBase")
    val collection = database.getCollection<uuidBatch>("testCollection")
    var counter = 0 //used for multiple producers

    /**
     * the main function, determines what runs when it is called in Main.kt
     */

    fun main() {
        val time = measureTimeMillis { oneProducer() }
        println(time)
    }

    /**
     * the oneProducer is a functional test to see how fast a single producer sends to the database
     *  RUNTIME = 15.954 minutes
     */

    fun oneProducer() = runBlocking {
        val numberOfIds = 800000
        val batchChannel = produceBatches(dummyUUIDBatch, numberOfIds)
        sendToMongo(batchChannel = batchChannel)
    }

    /**
     * Similar to oneProducer() except it has varying number of producers, currently has 5
     *  RUNTIME = 19.0638 minutes
     */

    fun multipleProducers() = runBlocking {
        val numberOfIds = 160000
        val batchChannel1 = produceBatches(dummyUUIDBatch, numberOfIds)
        val batchChannel2 = produceBatches(dummyUUIDBatch, numberOfIds)
        val batchChannel3 = produceBatches(dummyUUIDBatch, numberOfIds)
        val batchChannel4 = produceBatches(dummyUUIDBatch, numberOfIds)
        val batchChannel5 = produceBatches(dummyUUIDBatch, numberOfIds)
        sendToMongo(batchChannel = batchChannel1)
        sendToMongo(batchChannel = batchChannel2)
        sendToMongo(batchChannel = batchChannel3)
        sendToMongo(batchChannel = batchChannel4)
        sendToMongo(batchChannel = batchChannel5)
    }

    /**
     * The method to produce a single batch by taking an empty batch and calling the fillBatch,
     * from there the method sends the complete batch to a channel which holds all the ids and passes them off to the next method
     */

    fun CoroutineScope.produceBatches(dummyUUIDBatch: uuidBatch, numberOfIds: Int): ReceiveChannel<uuidBatch> = produce {
        for (i in 1..numberOfIds) {
            //counter++
            //println(counter)
            send(fillBatch(dummyUUIDBatch))
        }
        close()
    }

    /**
     *  Generates 500 ids and fills the block parameter for the uuidBatch data class and then returns the inputted uuidBatch
     */

    fun fillBatch(dummyUUIDBatch: uuidBatch): uuidBatch {
        val blockValues = mutableListOf<UUID>()
        for (i in 1..500)
        {
            blockValues.add(UUID.randomUUID())
        }
        dummyUUIDBatch.block = blockValues
        return dummyUUIDBatch
    }

    /**
     * takes the channel with all the ids on it and sends it onto mongo
     */

    suspend fun sendToMongo(batchChannel: ReceiveChannel<uuidBatch>) {
        for (batch in batchChannel) {
            collection.insertOne(batch)
        }
    }
}