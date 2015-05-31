package fr.ekinci.mongodbwrapper;


/**
 * Get a DatabaseCollection from a DataBase
 * 
 * @author Gokan EKINCI
 */ 
public interface Database {
    /**
     * <p>Get a DatabaseCollection from a DataBase</p>
     * <p>The Class<T> must represent the collection of documents</p>
     * @param entityClass
     * @param dbCollectionName
     * @return
     */
    public <T> DatabaseCollection<T> getDatabaseCollection(Class<T> entityClass);
}
