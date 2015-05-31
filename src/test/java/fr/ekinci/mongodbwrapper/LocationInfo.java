package fr.ekinci.mongodbwrapper;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import fr.ekinci.mongodbwrapper.Collection;

/**
 * A mock class which represent a coordinate (with additional information)
 * 
 * @author Gokan EKINCI
 */
@Entity 
@Collection(name="LocationInfo")
public class LocationInfo 
    implements Comparable<LocationInfo> {
    
    @Id
    @Column(name="_id")
    private ObjectId id;
    
    private String carId;
    private Date timestamp; // instant T
    private double longitude;
    private double latitude;
    private double altitude;
    private float speed;

    @Override
    public String toString() {
        return "LocationInfo [id=" + id + ", carId=" + carId + ", timestamp="
                + timestamp + ", longitude=" + longitude + ", latitude="
                + latitude + ", altitude=" + altitude + ", speed=" + speed
                + "]";
    }
    
    public ObjectId getId() {
        return id;
    }

    public String getCarId() {
        return carId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
	public int compareTo(LocationInfo locationInfo) {
        return timestamp.compareTo(locationInfo.getTimestamp());	
    }

}
