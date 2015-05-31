package fr.ekinci.mongodbwrapper;

import java.lang.reflect.Field;
import com.mongodb.DB;
import fr.ekinci.mongodbwrapper.Database;
import fr.ekinci.mongodbwrapper.DatabaseCollection;


/**  
 * An implementation of Database interface
 * 
 * @author Gokan EKINCI
 */
class DatabaseImpl implements Database {
    private DB db;
    
    public DatabaseImpl(DB db){
        this.db = db;
    }
    
    @Override
    public <T> DatabaseCollection<T> getDatabaseCollection(Class<T> entityClass){
        // Test if it's annotated with @Entity
        boolean isEntityPresent = 
            entityClass.isAnnotationPresent(javax.persistence.Entity.class);
        if(!isEntityPresent) {
            throw new IllegalArgumentException(entityClass.getName() + " is not annotated with @Entity"); 
        }
        
        // Test if it's annotated with @Collection
        String dbCollectionName = null;
        fr.ekinci.mongodbwrapper.Collection eCollection = 
             entityClass.getAnnotation(fr.ekinci.mongodbwrapper.Collection.class);
        if(eCollection == null){
            dbCollectionName = entityClass.getSimpleName();
        } else {
            dbCollectionName = eCollection.name();
            if(!dbCollectionName.matches("[a-zA-Z]{1,}")) {
                throw new IllegalArgumentException(entityClass.getName() + "'s @Collection name does not match with \"[a-zA-Z]{1,}\""); 
            }        
        }
        
        
        // Test if the @Entity class contains an @Id Field with the "_id" name
        boolean isIdPresent = false;
        for(Field f : entityClass.getDeclaredFields()){
            f.setAccessible(true);
            if(f.isAnnotationPresent(javax.persistence.Id.class)){
                isIdPresent = true;
                // Verify if @Id is org.bson.types.ObjectId
                if(!f.getType().equals(org.bson.types.ObjectId.class)) {
                    throw new IllegalArgumentException("The _id must be an org.bson.types.ObjectId");
                }
                
                if(f.isAnnotationPresent(javax.persistence.Column.class)){
                    String columnName = f.getAnnotation(javax.persistence.Column.class).name();
                    if("_id".equals(columnName)){
                        break; // OK, @Id name is _id
                    } else {
                        throw new IllegalArgumentException(entityClass.getName() + "'s @Id field @Column name must be \"_id\"");
                    }
                } else { // if the @Column annotation is absent, use field name
                    if("_id".equals(f.getName())){
                        break; // OK, @Id name is _id
                    } else { 
                        throw new IllegalArgumentException(entityClass.getName() + "'s @Id field name must be \"_id\"");
                    }
                }
            }
        }
        
        if(!isIdPresent) {
            throw new IllegalArgumentException(entityClass.getName() + " must have an \"_id\" column");
        }
        
        return new DatabaseCollectionImpl<T>(entityClass, dbCollectionName, db);
    } 
}
