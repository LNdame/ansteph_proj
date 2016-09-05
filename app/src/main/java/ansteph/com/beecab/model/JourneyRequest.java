package ansteph.com.beecab.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by loicStephan on 12/07/16.
 */
public class JourneyRequest implements Serializable{



    private int id ;
    private String pickupAddr;
    private String destinationAddr;
    private String pickupTime;
    private String proposedFare;
    private boolean callAllowed;
    private String pickupCoord;
    private String destinationCoord;
    private String  clientID;
    private Date timeCreated;
    private String  city;
    private boolean isShared;

      public JourneyRequest()
      {}

    public JourneyRequest(int id, String pickupAddr, String destinationAddr, String pickupTime, String proposedFare,
                          boolean callAllowed, String pickupCoord, String destinationCoord,  String clientID, boolean isShared) {
        this.id = id;
        this.pickupAddr = pickupAddr;
        this.destinationAddr = destinationAddr;
        this.pickupTime = pickupTime;
        this.proposedFare = proposedFare;
        this.callAllowed = callAllowed;
        this.pickupCoord = pickupCoord;
        this.destinationCoord = destinationCoord;
        this.clientID = clientID;
        this.isShared = isShared;
    }




    public JourneyRequest(int id, String pickupAddr, String destinationAddr, String pickupTime, String proposedFare,
                          String pickupCoord, String destinationCoord,  String clientID) {
        this.id = id;
        this.pickupAddr = pickupAddr;
        this.destinationAddr = destinationAddr;
        this.pickupTime = pickupTime;
        this.proposedFare = proposedFare;

        this.pickupCoord = pickupCoord;
        this.destinationCoord = destinationCoord;
        this.clientID = clientID;

    }


    public JourneyRequest( String pickupAddr, String destinationAddr, String proposedFare,String pickupTime) {
        this.proposedFare = proposedFare;
        this.pickupAddr = pickupAddr;
        this.destinationAddr = destinationAddr;
        this.pickupTime = pickupTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPickupAddr() {
        return pickupAddr;
    }

    public void setPickupAddr(String pickupAddr) {
        this.pickupAddr = pickupAddr;
    }

    public String getDestinationAddr() {
        return destinationAddr;
    }

    public void setDestinationAddr(String destinationAddr) {
        this.destinationAddr = destinationAddr;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getProposedFare() {
        return proposedFare;
    }

    public void setProposedFare(String proposedFare) {
        this.proposedFare = proposedFare;
    }

    public boolean isCallAllowed() {
        return callAllowed;
    }

    public void setCallAllowed(boolean callAllowed) {
        this.callAllowed = callAllowed;
    }

    public String getPickupCoord() {
        return pickupCoord;
    }

    public void setPickupCoord(String pickupCoord) {
        this.pickupCoord = pickupCoord;
    }

    public String getDestinationCoord() {
        return destinationCoord;
    }

    public void setDestinationCoord(String destinationCoord) {
        this.destinationCoord = destinationCoord;
    }

    public  String getClientID() {
        return clientID;
    }

    public void setClientID( String clientID) {
        this.clientID = clientID;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }


}
