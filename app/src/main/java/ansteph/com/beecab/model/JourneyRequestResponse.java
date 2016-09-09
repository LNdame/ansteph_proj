package ansteph.com.beecab.model;

import java.util.Date;

/**
 * Created by loicStephan on 08/09/16.
 */
public class JourneyRequestResponse {
    private int id ;
    private String TaxiID;
    private int JorID;
    private String name;
    private String coName;
    private String email;
    private String mobile;
    private int proposedFare;
    private int counterOffer;
    private String  car_picture_url;
    private String  image_tag;


    public JourneyRequestResponse(int id, String taxiID, int jorID, String name, String coName,
                                  String email, String mobile, int proposedFare, int counterOffer, String car_picture_url, String image_tag) {
        this.id = id;
        TaxiID = taxiID;
        JorID = jorID;
        this.name = name;
        this.coName = coName;
        this.email = email;
        this.mobile = mobile;
        this.proposedFare = proposedFare;
        this.counterOffer = counterOffer;
        this.car_picture_url = car_picture_url;
        this.image_tag = image_tag;
    }

    public JourneyRequestResponse(String taxiID, int jorID, String name, String coName,
                                  String email, String mobile, int proposedFare, int counterOffer, String car_picture_url, String image_tag) {
        TaxiID = taxiID;
        JorID = jorID;
        this.name = name;
        this.coName = coName;
        this.email = email;
        this.mobile = mobile;
        this.proposedFare = proposedFare;
        this.counterOffer = counterOffer;
        this.car_picture_url = car_picture_url;
        this.image_tag = image_tag;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaxiID() {
        return TaxiID;
    }

    public void setTaxiID(String taxiID) {
        TaxiID = taxiID;
    }

    public int getJorID() {
        return JorID;
    }

    public void setJorID(int jorID) {
        JorID = jorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoName() {
        return coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getProposedFare() {
        return proposedFare;
    }

    public void setProposedFare(int proposedFare) {
        this.proposedFare = proposedFare;
    }

    public int getCounterOffer() {
        return counterOffer;
    }

    public void setCounterOffer(int counterOffer) {
        this.counterOffer = counterOffer;
    }

    public String getCar_picture_url() {
        return car_picture_url;
    }

    public void setCar_picture_url(String car_picture_url) {
        this.car_picture_url = car_picture_url;
    }

    public String getImage_tag() {
        return image_tag;
    }

    public void setImage_tag(String image_tag) {
        this.image_tag = image_tag;
    }
}
/*SELECT `id`, `TaxiID`, `JorID`, `jre_proposed_fare`, `jre_counter_offer`, `
td_name`, , `td_email`, `td_mobile`, `co_name`,
 `car_picture_url`, `image_tag` FROM `journey_response` W*/