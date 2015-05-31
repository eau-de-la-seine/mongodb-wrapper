package fr.ekinci.mongodbwrapper;

import java.util.List;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import fr.ekinci.mongodbwrapper.DatabaseImpl;


/**
 * A class which create a link between the program and MongoDB
 * 
 * @author Gokan EKINCI
 */ 
public class Cluster {
    private MongoClient mc;

    /**
     * <p>Constructor which create the connection with MongoDB</p>
     * @param listOfNodes
     */
    public Cluster(List<ServerAddress> listOfNodes){
        mc = new MongoClient(listOfNodes);
    }

    public Database getDatabaseByName(String databaseName){
        DB db = mc.getDB(databaseName);
        return new DatabaseImpl(db);
    }
}
