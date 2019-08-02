# IdBatcher

Purpose: The IdBatcher program is designed to create 40 billion unique Id's everytime it is deployed. The ids are held onto in a Mongo database and then are later shelved in batches of 500. Batches are used in testing and distributing ids to microservices and are accessed through a CRUD Service. 

The batches are formatted as such: 

    {
      "_id":"block1",
      "used": true,
      "service":"tool service",
      "block": [
        "uuid1", 
        "uuid2", 
        "uuid3", 
        "uuid4",
        ...,
        "uuid500"
        ]
    }

The breakdown of the batch is as follows: 

    id_ = mongo id 
    used = determines if the current batch is used or not 
    service = if the batch is used then it is labeled with which service it was used in
    block = the 500 ids in an array

The ID Batcher is a work in progress and has a lot of dead code that might be usable later on. There a certain classes that are completely dead, they are: 
  
    Application Service (the API Logic that might come in handy later on when that part of development begins)
    
    BatchRouter (The actual API itself that sets up calls to the Application Service functions upon a successful request)
    
    test (a class that is left for testing impromptu methods, has no particular use and can be deleted) 
    
    MongoUtils (became invalid after the switch to KMongo, can also be deleted) 
    
    GenerateUUID (class method for the uuidBatch class, added methods to simplify and add functionality to the data class. Most of the    
      functions inside have been replaced or rewritten.)

Essential Classes: 

    Main (Deploys the program)
    
    uuidBatch (the data class for the object) 
    
    ChannelSend (The class that contains most of the functions such as generating the batches and sending them to Mongo)
    
    
 
   
