package fr.ekinci.mongodbwrapper;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import fr.ekinci.mongodbwrapper.HeartRate;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.ServerAddress;
import fr.ekinci.mongodbwrapper.Cluster;
import fr.ekinci.mongodbwrapper.Collection;
import fr.ekinci.mongodbwrapper.Database;
import fr.ekinci.mongodbwrapper.DatabaseCollection;
import fr.ekinci.mongodbwrapper.DatabaseImpl;
import static org.junit.Assert.*;


/** 
 * Test for DatabaseImpl
 * 
 * @author Gokan EKINCI
 */
@RunWith(MockitoJUnitRunner.class)
public class DatabaseImplTest {
    
    @Mock
    DB db;
    
    @Test(expected=IllegalArgumentException.class)
    public void mustBeEntity(){
        // Not annotated with @Entity
        class MyTest{}
        
        DatabaseImpl dbi = new DatabaseImpl(db);
        dbi.getDatabaseCollection(MyTest.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void wrongCollectionName(){

        @Entity
        @Collection(name="1234") // Should be : [a-zA-Z]{1,}
        class MyTest{}
        
        DatabaseImpl dbi = new DatabaseImpl(db);
        dbi.getDatabaseCollection(MyTest.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void mustContainIdField(){
        @Entity
        @Collection(name="MyTest")
        class MyTest{
            // @Id field is absent
        }
        
        DatabaseImpl dbi = new DatabaseImpl(db);
        dbi.getDatabaseCollection(MyTest.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void idFieldMustBeObjectIdType(){
        @Entity
        @Collection(name="MyTest")
        class MyTest{
            @Id
            private int id; // Should be org.bson.types.ObjectId
        }
        
        DatabaseImpl dbi = new DatabaseImpl(db);
        dbi.getDatabaseCollection(MyTest.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void idFieldColumnNameMustBe_id(){
        @Entity
        @Collection(name="MyTest")
        class MyTest{
            @Id
            @Column(name="wrongColumnName") // Should be "_id"
            private ObjectId id;
        }
        
        DatabaseImpl dbi = new DatabaseImpl(db);
        dbi.getDatabaseCollection(MyTest.class);
    }
    
    /**
     * <p>Use database in order to show that it work correctly</p>
     */
    @Test
    public void testDatabaseCollection() {
        final String HOST = "192.168.20.7";
        final int PORT = 12_000;
        final String DATABASE_NAME = "test_heartratedb";
        
        try {
            Cluster c = new Cluster(Arrays.asList(new ServerAddress(HOST, PORT)));
            Database d = c.getDatabaseByName(DATABASE_NAME);
            DatabaseCollection<HeartRate> hrColl = d.getDatabaseCollection(HeartRate.class);
            
            
            final String FIELD_PATIENT_ID = "patientId";
            final String FIELD_TIMESTAMP = "timestamp";
            final String VALUE_PATIENT_ID = "YYYYYYY";
            final Date VALUE_NOW = new Date();
            System.out.println("NOW : " + VALUE_NOW);
            
            HeartRate hr = new HeartRate();
            hr.setPatientId(VALUE_PATIENT_ID);
            hr.setTimestamp(VALUE_NOW);
            
            try {
                hrColl.insert(hr);

                List<HeartRate> lhr = hrColl.find(
                    new BasicDBObject(FIELD_PATIENT_ID, VALUE_PATIENT_ID)
                    .append(FIELD_TIMESTAMP, VALUE_NOW));
                
                for(HeartRate elem : lhr) {
                    System.out.println(elem);
                }
                
                assertTrue(lhr.size() == 1); 
                
                try{Thread.sleep(1000);}catch(InterruptedException ie){}
                HeartRate hr1 = hrColl.findOne(
                    null,
                    new BasicDBObject(FIELD_TIMESTAMP, -1)
                    );
                System.out.println(hr1);
                
            } catch (Exception e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
