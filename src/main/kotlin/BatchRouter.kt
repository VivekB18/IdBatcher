//import io.vertx.ext.web.Route
//import io.vertx.ext.web.Router
//import io.vertx.ext.web.RoutingContext
//import io.vertx.ext.web.handler.BodyHandler
//import io.vertx.kotlin.coroutines.CoroutineVerticle
//import io.vertx.kotlin.coroutines.dispatcher
//import kotlinx.coroutines.launch
//import io.vertx.core.Vertx
//
//class BatchRouter: CoroutineVerticle() {
//    val server = Vertx.vertx().createHttpServer()
//    val router = Router.router(Vertx.vertx())
//    val applicationService = ApplicationService()
//    val testClass = test()
//    override suspend fun start() {
//   // router.route().handler(LoggerHandler.create())
//    //router.post().handler(BodyHandler.create())
//    //router.get().handler()
//    //router.post("/internal/v1/topic").coroutineHandler(applicationService::postToTopic)
//    //router.post("/internal/v1/topics").coroutineHandler(applicationService::postToTopics)
//    router.get("/getOneBatch").coroutineHandler(applicationService::receiveBlock)
//    //router.post("/sendSomething").coroutineHandler(testClass::addItem)
//    server.requestHandler(router).listen(8080)
//    }
//
//    fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
//        handler { ctx ->
//            launch(ctx.vertx().dispatcher()) {
//                try {
//                    fn(ctx)
//                } catch (e: Exception) {
//                    ctx.fail(e)
//                }
//            }
//        }
//    }
//}