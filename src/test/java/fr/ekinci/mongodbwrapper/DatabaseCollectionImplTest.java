package fr.ekinci.mongodbwrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import fr.ekinci.mongodbwrapper.HeartRate;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DB;
import fr.ekinci.mongodbwrapper.DatabaseCollectionImpl;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


/**
 * Test for DatabaseCollectionImpl
 * 
 * @author Gokan EKINCI
 */
@RunWith(MockitoJUnitRunner.class)
public class DatabaseCollectionImplTest {
    
    @Mock
    DB db;
    
    @Mock 
    DBCursor cursor;
    
    
    @Test
    public void testGetResultFromCursor() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
        DatabaseCollectionImpl dci = new DatabaseCollectionImpl(HeartRate.class, "HeartRate", db);
        
        when(cursor.hasNext())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false); // For stoping the iteration
        
        when(cursor.next())
            .thenReturn(new BasicDBObject("averageHeartRate", 11d).append("patientId", "Hello 1"))
            .thenReturn(new BasicDBObject("averageHeartRate", 12d).append("patientId", "Hello 2"))
            .thenReturn(new BasicDBObject("averageHeartRate", 13d).append("patientId", "Hello 3"));

        
        // Use of Reflection API because of private visibility of getResultFromCuror method
        Method method = DatabaseCollectionImpl.class.getDeclaredMethod("getResultFromCursor", DBCursor.class);
        method.setAccessible(true);
        List<HeartRate> list = (List<HeartRate>) method.invoke(dci, cursor);
        
        // Test :
        HeartRate mt1 = list.get(0);
        assertEquals(mt1.getAverageHeartRate(), 11d, 0d);
        assertEquals(mt1.getPatientId(), "Hello 1");
        
        HeartRate mt2 = list.get(1);
        assertEquals(mt2.getAverageHeartRate(), 12d, 0d);
        assertEquals(mt2.getPatientId(), "Hello 2");
        
        HeartRate mt3 = list.get(2);
        assertEquals(mt3.getAverageHeartRate(), 13d, 0d);
        assertEquals(mt3.getPatientId(), "Hello 3");
    }
} 
