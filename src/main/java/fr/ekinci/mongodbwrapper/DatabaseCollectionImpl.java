package fr.ekinci.mongodbwrapper;

import java.util.ArrayList;
import java.util.List;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import fr.ekinci.mongodbwrapper.DatabaseCollection;
import fr.ekinci.mongodbwrapper.NoSQLException;
import fr.ekinci.mongodbwrapper.StatNoSQL;


/** 
 * An implementation of DatabaseCollection<T> interface
 * 
 * @author Gokan EKINCI
 * @param <T>
 */
class DatabaseCollectionImpl<T> implements DatabaseCollection<T>{
    private Class<T> entityClass;
    private DBCollection mongoCollection;
    private StatNoSQL snl;
    
    
    public DatabaseCollectionImpl(Class<T> entityClass, String dbCollectionName, DB db){
        this.entityClass = entityClass;
        mongoCollection = db.getCollection(dbCollectionName);
        snl = new StatNoSQL();
    }
    
    
    @Override
    public void insert(T... entity) throws NoSQLException {
        snl.incrementCounterInsertQuery();
        
        try {
            // Convert T array to Entity<T> array 
            Entity<T>[] implEntities = new Entity[entity.length];
            for(int i = 0; i < entity.length; i++){
                implEntities[i] = new Entity<T>(entity[i]);
                implEntities[i].removeId();
            }  
            mongoCollection.insert(implEntities);  
        } finally {
            snl.decrementCounterInsertQuery(); // If an exception is launched, must decrement "snl" counter
        }
    }
    
    
    @Override
    public void insert(List<T> entity) throws NoSQLException {
        snl.incrementCounterInsertQuery();
        
        try {
            // Convert T list to Entity<T> array 
            final int SIZE = entity.size();
            Entity<T>[] implEntities = new Entity[SIZE];
            for(int i = 0; i < SIZE; i++){
                implEntities[i] = new Entity<T>(entity.get(i));
                implEntities[i].removeId();
            }
            
            mongoCollection.insert(implEntities);     
        } finally {
            snl.decrementCounterInsertQuery(); // If an exception is launched, must decrement "snl" counter
        }
    }
    
    
    @Override
    public T findOne(DBObject dbObjectQuery) throws NoSQLException{
        return findOne(dbObjectQuery, null);
    }
    
    
    @Override
    public List<T> find(DBObject dbObjectQuery) throws NoSQLException {
        return find(dbObjectQuery, null);
    }
    
    
    @Override
    public List<T> findAll() throws NoSQLException {
        return findAll(null);
    }
    
    
    @Override
    public T findOne(DBObject dbObjectQuery, DBObject orderBy) throws NoSQLException{
        snl.incrementCounterSelectQuery();
        T result = null;
        
        try {
            DBObject dbo = (orderBy == null) ? 
                mongoCollection.findOne(dbObjectQuery) : mongoCollection.findOne(dbObjectQuery, null, orderBy);  
            result = Entity.dbObjectToEntity(dbo, entityClass);
        } finally {    
            snl.decrementCounterSelectQuery(); // If an exception is launched, must decrement "snl" counter
        }
        
        return result;
    }
    
    
    @Override
    public List<T> find(DBObject dbObjectQuery, DBObject orderBy) throws NoSQLException {
        snl.incrementCounterSelectQuery();
        List<T> result = null;
        
        try {
            DBCursor cursor = (orderBy == null) ? 
                mongoCollection.find(dbObjectQuery) : mongoCollection.find(dbObjectQuery).sort(orderBy);
            result = getResultFromCursor(cursor);
        } finally {
            snl.decrementCounterSelectQuery(); // If an exception is launched, must decrement "snl" counter
        } 
        
        return result;
    }
    
    
    @Override
    public List<T> findAll(DBObject orderBy) throws NoSQLException {
        snl.incrementCounterSelectQuery();
        List<T> result = null;
        
        try {
            DBCursor cursor = (orderBy == null) ?
                mongoCollection.find() : mongoCollection.find().sort(orderBy);
            result = getResultFromCursor(cursor);
        } finally {
            snl.decrementCounterSelectQuery(); // If an exception is launched, must decrement "snl" counter
        }
        
        return result;
    }
    
    
    /**
     * A method which factorize find() and findAll() methods
     * @param cursor
     * @return
     * @throws NoSQLException
     */
    private List<T> getResultFromCursor(DBCursor cursor) throws NoSQLException {
        List<T> result = new ArrayList<T>();

        try {
            while(cursor.hasNext()) {
                result.add(
                    Entity.dbObjectToEntity(cursor.next(), entityClass)  
                );
            }
        } finally {
            cursor.close();
        }
        
        return result;
    }

    
    @Override
    public void remove(DBObject dbObjectQuery) {
        snl.incrementCounterDeleteQuery();
        
        try {
            mongoCollection.remove(dbObjectQuery);
        } finally {
            snl.decrementCounterDeleteQuery();  // If an exception is launched, must decrement "snl" counter
        }
    }
}
