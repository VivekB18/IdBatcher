import io.vertx.core.Vertx
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

//import kotlinx.coroutines.channels.*


/**
 * Old ways to send to Mongo with warying runtimes, most were deleted in the development phase except the runner up'
 * This class relies on the GenerateUUID class
 */
class OldAlgorithimsForSending {
    val dummyBatch = GenerateUUID()
    val dummyUUIDBatch = uuidBatch(false, "", mutableListOf<UUID>())


    /**
     * The main method of the class that is used to test the program and see how long it takes to finsh
     */
    fun timeTest() {
        val time = measureTimeMillis { sendHoard() }
        println("total execution time in ms: $time")
    }

    /**
     * Dummy method to send one object to Mongo to see if mongo is up and running
     */

    fun sendOne() {
        println("Called sendOne")
        dummyBatch.fillBatch(dummyUUIDBatch)
        dummyBatch.sendBatch(dummyUUIDBatch)
    }

    /**
     * The runner up in runtime, the program is simple, takes a for loop and generates a batch,
     * once made, the batch is immediately sent and the cycle restarts
     * RUNTIME = 18.0903 minutes
     */

    fun sendHoard() {
        for (i in 0..800000) {
            dummyBatch.fillBatch(dummyUUIDBatch)
            dummyBatch.sendBatch(dummyUUIDBatch)
            println("current iteration: $i")
            //Thread.sleep(3)
        }
    }

}