package ansteph.com.beecab.model;

/**
 * Created by loicStephan on 12/07/16.
 */
public class DriverProfile {

    private String carModel;
    private String carNumberPlate;
    private String currentCity;
    private String profileRating;
    private String name;
    private String email;
    private String mobile;

    private String yearoflicense;
    private String license;

    public DriverProfile(String carModel, String carNumberPlate, String currentCity, String profileRating) {
        this.carModel = carModel;
        this.carNumberPlate = carNumberPlate;
        this.currentCity = currentCity;
        this.profileRating = profileRating;
    }


    public DriverProfile(String carModel, String carNumberPlate, String currentCity, String profileRating, String name, String email, String mobile) {
        this.carModel = carModel;
        this.carNumberPlate = carNumberPlate;
        this.currentCity = currentCity;
        this.profileRating = profileRating;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarNumberPlate() {
        return carNumberPlate;
    }

    public void setCarNumberPlate(String carNumberPlate) {
        this.carNumberPlate = carNumberPlate;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getProfileRating() {
        return profileRating;
    }

    public void setProfileRating(String profileRating) {
        this.profileRating = profileRating;
    }

    public String getYearoflicense() {
        return yearoflicense;
    }

    public void setYearoflicense(String yearoflicense) {
        this.yearoflicense = yearoflicense;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}

