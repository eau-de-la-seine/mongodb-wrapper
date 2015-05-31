package fr.ekinci.mongodbwrapper;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bson.BSONObject;
import com.mongodb.DBObject;
import fr.ekinci.mongodbwrapper.NoSQLException;

/** 
 * A decorator pattern class for MongoDB data use
 * 
 * @author Gokan EKINCI
 * @param <T> 
 */
class Entity<T> implements DBObject {
    private Map<String, Object> map = new HashMap<String, Object>();

    // Attribute which is specific to MongoDB
    private boolean partialObject;

    
    /**
     * Creates a new Entity from a JavaBean which is annotated with @Entity
     * @param entity
     * @throws NoSQLException
     */
    public Entity(T entity) throws NoSQLException {
        try {
            Field[] attributes = entity.getClass().getDeclaredFields();
            for(Field f : attributes){
                f.setAccessible(true);
                // Test if it's not Transient :
                if(!f.isAnnotationPresent(javax.persistence.Transient.class)){
                    // Get column name, if not exists get attribute name
                    javax.persistence.Column column = f.getAnnotation(javax.persistence.Column.class);
                    String columnName = (column != null) ? column.name() : f.getName();
                    map.put(columnName, f.get(entity));
                }
            }
        } catch(IllegalArgumentException | IllegalAccessException e){
            throw new NoSQLException(e);
        }
    }

    /**
     * Remove the "_id" field for insert() or entityQuery for find()
     */
    public void removeId(){
        map.remove("_id");
    }

    /**
     * <p>This static method converts a DBObject object to Entity</p>
     * @param dbObject
     * @param clazz
     * @return
     * @throws NoSQLException
     */
    public static <T> T dbObjectToEntity(DBObject dbObject, Class<T> clazz) throws NoSQLException {      
        T entity = null;
        try { 
            entity = clazz.newInstance();
            Map<String, Object> m = dbObject.toMap();
            
            Field[] attributes = entity.getClass().getDeclaredFields(); 
            for(Field f : attributes){
                f.setAccessible(true);
                if(f.isAnnotationPresent(javax.persistence.Column.class)){
                    f.set(entity, m.get(f.getAnnotation(javax.persistence.Column.class).name()));
                } else {
                    f.set(entity, m.get(f.getName()));
                }
            }
        } catch(InstantiationException | IllegalAccessException e){
            throw new NoSQLException(e);
        }

        return entity;
    }

    
    @Override
    public boolean containsField(String field) {
        return map.containsKey(field);
    }

    
    @Override
    public boolean containsKey(String field) {
        return map.containsKey(field);
    }

    
    @Override
    public Object get(String field) {
        return map.get(field);
    }

    
    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    
    @Override
    public Object put(String field, Object value) {
        return map.put(field, value);
    }

    
    @Override
    public void putAll(BSONObject b) {
        map.putAll(b.toMap());
    }

    
    @Override
    public void putAll(Map m) {
        map.putAll(m);
    }

    
    @Override
    public Object removeField(String field) {
        return map.remove(field);
    }

    
    @Override
    public Map<String, Object> toMap() {
        return map;
    }

    
    @Override
    public boolean isPartialObject() {
        return partialObject;
    }

    
    @Override
    public void markAsPartialObject() {
        partialObject = true;
    }
}
