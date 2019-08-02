# IdBatcher

The ID Batcher is a work in progress and has a lot of dead code that might be usable later on. There a certain classes that are completely dead, they are: 
  
    Application Service (the CRUD API Logic that might come in handy later on when that part of development begins)
    BatchRouter (The actual CRUD API itself that sets up calls to the Application Service functions upon a successful request)
    test (a class that is left for testing impromptu methods, has no particular use and can be deleted) 
    MongoUtils (became invalid after the switch to KMongo, can also be deleted) 
    GenerateUUID (class method for the uuidBatch class, added methods to simplify and add functionality to the data class. Most of the    
      functions inside have been replaced or rewritten.)

Essential Classes: 

    Main (Deploys the program)
    uuidBatch (the data class for the object) 
    ChannelSend (The class that contains most of the functions such as generating the batches and sending them to Mongo)
    
    
 
   
