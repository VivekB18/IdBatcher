//import io.vertx.core.Vertx
//import kotlinx.coroutines.cancelChildren
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//import java.util.*
//import kotlinx.coroutines.*
//import kotlin.system.measureTimeMillis
//
////import kotlinx.coroutines.channels.*
//
//val dummyBatch = GenerateUUID()
//val dummyUUIDBatch = uuidBatch(false, "", mutableListOf<UUID>())
//    /*
//fun main() {
//    // val batchRouter = BatchRouter()
//    //sendOne()
//    val time = measureTimeMillis { sendHoard() }
//    println("total execution time in ms: $time")
//
//
////    val channel = Channel<uuidBatch>()
////    runBlocking {
////        println("entered runblocking")
////        launch {dummyBatch.sendManyBatches(channel, dummyUUIDBatch)}
////        launch {dummyBatch.sendManyBatches(channel, dummyUUIDBatch)}
////        launch {dummyBatch.sendManyBatches(channel, dummyUUIDBatch)}
////        launch {dummyBatch.sendManyBatches(channel, dummyUUIDBatch)}
////        coroutineContext.cancelChildren()
////        println("finished runblocking")
////    }
//
////
////    val channel = Channel<uuidBatch>()
////    launch {    }
////}
////
////suspend fun sendtoMongo(batch:uuidBatch, channel: Channel<uuidBatch>) {
////    for ()
////}
//
//
//}
//*/
//
////sampleStart
//data class Ball(var hits: Int)
//
////fun main() = runBlocking {
////    val table = Channel<Ball>() // a shared table
////    launch { player("ping", table) }
////    launch { player("pong", table) }
////    table.send(Ball(8)) // serve the ball
////    //delay(100) // delay 1 second
////    //coroutineContext.cancelChildren() // game over, cancel them
////}
////
////suspend fun player(name: String, table: Channel<Ball>) {
////    while (ball.hits < 14) { // receive the ball in a loop
////        ball.hits++
////        println("$name $ball")
////        delay(500) // wait a bit
////        table.send(ball) // send the ball back
////    }
////}
//
//fun main() = runBlocking {
//
//    val mongoDatabase = Channel<uuidBatch>()
//    launch {
//        for (i in 1..5) {
//            println("current iteration: $i")
//            mongoDatabase.send(dummyUUIDBatch) }
//        mongoDatabase.close()
//    }
//    val time = measureTimeMillis {
//        coroutineScope{
//            async{dummyBatch.sendManyBatches(mongoDatabase)}
//            async{dummyBatch.sendManyBatches(mongoDatabase)}
//            async{dummyBatch.sendManyBatches(mongoDatabase)}
//            async{dummyBatch.sendManyBatches(mongoDatabase)}
//        }
//    }
//    println("total execution time in ms: $time")
//}
///*
//
//fun sendOne() {
//    println("Called sendOne")
//    dummyBatch.fillBatch(dummyUUIDBatch)
//    dummyBatch.sendBatch(dummyUUIDBatch)
//}
//
//fun sendHoard() {
//    for (i in 0..800000) {
//        dummyBatch.fillBatch(dummyUUIDBatch)
//        dummyBatch.sendBatch(dummyUUIDBatch)
//        println("current iteration: $i")
//        //Thread.sleep(3)
//    }
//}
//*/
fun main() {
    val test = ChannelSend()
    test.main()
}