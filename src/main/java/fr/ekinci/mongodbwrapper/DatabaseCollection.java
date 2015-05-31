package fr.ekinci.mongodbwrapper;

import java.util.List;
import com.mongodb.DBObject;


/** 
 * An interface which represents all the actions allowed with a MongoDB collection
 * 
 * @author Gokan EKINCI
 * @param <T>
 */
public interface DatabaseCollection<T> {
    
    /**
     * <p>Insert one, several, or table of entity into the collection</p>
     * @param entity
     * @throws NoSQLException
     */
    public void insert(T... entity) throws NoSQLException;

    /**
     * <p>Insert a list of entity into the collection</p>
     * @param entity
     * @throws NoSQLException
     */
    public void insert(List<T> entity) throws NoSQLException;
    
    /**
     * <p>find only one object from the collection</p>
     * @param dbObjectQuery
     * @return
     */
    public T findOne(DBObject dbObjectQuery) throws NoSQLException;
    
    /**
     * <p>find a list of object from query</p>
     * @param dbObjectQuery
     * @return
     * @throws NoSQLException
     */
    public List<T> find(DBObject dbObjectQuery) throws NoSQLException;
    
    /**
     * <p>find a list of all the objects from the collection</p>
     * @return
     * @throws NoSQLException
     */
    public List<T> findAll() throws NoSQLException;
    
    /**
     * <p>find only one object from the collection with order by</p>
     * @param dbObjectQuery
     * @return
     */
    public T findOne(DBObject dbObjectQuery, DBObject orderBy) throws NoSQLException;
    
    /**
     * <p>find a list of object from query with order by</p>
     * @param dbObjectQuery
     * @return
     * @throws NoSQLException
     */
    public List<T> find(DBObject dbObjectQuery, DBObject orderBy) throws NoSQLException;
    
    /**
     * <p>find a list of all the objects from the collection with order by</p>
     * @return
     * @throws NoSQLException
     */
    public List<T> findAll(DBObject orderBy) throws NoSQLException;
    
    /**
     * <p>remove documents from query</p>
     * @param dbObjectQuery
     */
    public void remove(DBObject dbObjectQuery);
}
