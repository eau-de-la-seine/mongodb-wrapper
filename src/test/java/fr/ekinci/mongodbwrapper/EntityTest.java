package fr.ekinci.mongodbwrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.mongodb.DBObject;
import fr.ekinci.mongodbwrapper.Entity;


/**
 * Test for Entity
 * 
 * @author Gokan EKINCI
 */ 
@RunWith(MockitoJUnitRunner.class)
public class EntityTest {
    private final String FIELD_ID = "_id";
    private final String FIELD_PATIENT_ID = "patientId";
    private final String FIELD_AVERAGE_HEART_RATE = "averageHeartRate";
    private final String FIELD_NOW = "timestamp"; 
    
    private final ObjectId VALUE_ID = new ObjectId();
    private final String   VALUE_PATIENT_ID = "YYY";
    private final double   VALUE_AVERAGE_HEART_RATE = 0d;
    private final Date     VALUE_NOW = new Date();
    
    @Mock
    private DBObject dbo;
    /**
     * <p>See if Entity(T entity) works good</p>
     */
    @Test
    public void testInstanciationAndToMap(){
        try {
           
            HeartRate hr = new HeartRate();
            hr.setId(VALUE_ID);
            hr.setPatientId(VALUE_PATIENT_ID);
            hr.setAverageHeartRate(VALUE_AVERAGE_HEART_RATE);
            hr.setTimestamp(VALUE_NOW);            
            
            Entity<HeartRate> e = new Entity<HeartRate>(hr); // Instanciation is here
            Map<String, Object> m = e.toMap(); // Method to test is here
            
            assertEquals(hr.getId(), m.get(FIELD_ID));
            assertEquals(hr.getPatientId(), m.get(FIELD_PATIENT_ID));
            assertEquals(hr.getAverageHeartRate(), m.get(FIELD_AVERAGE_HEART_RATE));
            assertEquals(hr.getTimestamp(), m.get(FIELD_NOW));  
        } catch (Exception e) {
            e.printStackTrace();
            fail("An exception has been launched");
        }
    }
    
    /**
     * <p>A simple unserializing test</p>
     */
    @Test
    public void testDbObjectToEntity(){
        try {    
            
            when(dbo.toMap()).thenReturn(
                new HashMap<String, Object>(){
                    // Anonymous block
                    {
                        put(FIELD_ID, VALUE_ID);
                        put(FIELD_PATIENT_ID, VALUE_PATIENT_ID);
                        put(FIELD_AVERAGE_HEART_RATE, VALUE_AVERAGE_HEART_RATE);
                        put(FIELD_NOW, VALUE_NOW);
                    }
                }
            );
            
            HeartRate hr = Entity.dbObjectToEntity(dbo, HeartRate.class); // Method to test is here
            
            // We must get same values than BasicDBObject
            assertEquals(hr.getId(), VALUE_ID);
            assertEquals(hr.getPatientId(), VALUE_PATIENT_ID);
            assertEquals(hr.getTimestamp(), VALUE_NOW);
        } catch (Exception e) {
            e.printStackTrace();
            fail("An exception has been launched");
        }
    }
    
    /**
     * <p>Test if @Column is working</p>
     */
    @Test
    public void testUnknownColumnForId(){
        try {
 
            when(dbo.toMap()).thenReturn(
                new HashMap<String, Object>(){
                    // Anonymous block
                    {
                        put("unknown", VALUE_ID); // Specific wrong "id" here
                        put(FIELD_PATIENT_ID, VALUE_PATIENT_ID);
                        put(FIELD_AVERAGE_HEART_RATE, VALUE_AVERAGE_HEART_RATE);
                        put(FIELD_NOW, VALUE_NOW);
                    }
                }
            );
            
            HeartRate hr = Entity.dbObjectToEntity(dbo, HeartRate.class);
            
            // "unknown" column was not attributed to _id, so it must return null
            assertNull(hr.getId());
        } catch (Exception e) {
            e.printStackTrace();
            fail("An exception has been launched");
        }
    }
    
    /**
     * <p>Test removeId() method</p>
     */
    @Test
    public void testRemoveId(){
        try {       
            HeartRate hr = new HeartRate();
            Entity<HeartRate> e = new Entity<HeartRate>(hr);
            Map<String, Object> m = e.toMap();
            assertEquals(hr.getId(), m.get(FIELD_ID));

            // "_id" must be null after removeId()
            e.removeId();
            m = e.toMap();
            assertNull(m.get(FIELD_ID)); 

        } catch (Exception e) {
            e.printStackTrace();
            fail("An exception has been launched");
        }
    }
}
