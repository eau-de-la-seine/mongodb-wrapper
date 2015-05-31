package fr.ekinci.mongodbwrapper;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import fr.ekinci.mongodbwrapper.Collection;


/**
 * A mock class which represent a heart rate
 * 
 * @author Gokan EKINCI
 */
@Entity 
@Collection(name="HeartRate")
public class HeartRate {
    
    @Id
    @Column(name="_id")
    private ObjectId id;
    private double averageHeartRate;
    private String patientId;
    private Date timestamp; // instant T
    
    @Override
    public String toString() {
        return "HeartRate [id=" + id + ", averageHeartRate=" + averageHeartRate
                + ", patientId=" + patientId + ", timestamp=" + timestamp + "]";
    }

    public ObjectId getId() {
        return id;
    }

    public double getAverageHeartRate() {
        return averageHeartRate;
    }

    public String getPatientId() {
        return patientId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setAverageHeartRate(double averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
        
}
