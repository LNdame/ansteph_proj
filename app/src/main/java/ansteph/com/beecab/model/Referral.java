package ansteph.com.beecab.model;

/**
 * Created by loicStephan on 29/09/16.
 */
public class Referral {

    int id;

    String providedContact, senderID, dateSent;
    int status;


    public Referral(String providedContact, String senderID, String dateSent, int status) {
        this.providedContact = providedContact;
        this.senderID = senderID;
        this.dateSent = dateSent;
        this.status = status;
    }

    public Referral(int id, String providedContact, String senderID, String dateSent, int status) {
        this.id = id;
        this.providedContact = providedContact;
        this.senderID = senderID;
        this.dateSent = dateSent;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvidedContact() {
        return providedContact;
    }

    public void setProvidedContact(String providedContact) {
        this.providedContact = providedContact;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
