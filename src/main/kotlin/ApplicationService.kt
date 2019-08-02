/*
import io.vertx.ext.web.RoutingContext

class ApplicationService  {

    val generateUUID = GenerateUUID()
    suspend fun receiveBlock(event: RoutingContext) {
        val returnBatch = generateUUID.retrieveBatch()
        if (returnBatch != null) {
            event.response().setStatusCode(200).end(generateUUID.convertBatchToJson(returnBatch).encodePrettily())
        } else {
            event.response().setStatusCode(400).end("There are no more unused blocks left")
        }
    }
}*/