package ansteph.com.beecab.model;

/**
 * Created by loicStephan on 12/07/16.
 */
public class Driver {

    private String id;
    private String name;
    private String companyname;
    private String email;
    private String mobile;
    private String password;
    private String apikey;
    private int companyid;


    public Driver() {

    }

    public Driver(String id, String name, String companyname, String email, String mobile, int companyid) {
        this.id = id;
        this.name = name;
        this.companyname = companyname;
        this.email = email;
        this.mobile = mobile;
        this.companyid = companyid;
    }




    public Driver(String id, String name, String email, String mobile, String apikey) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.apikey = apikey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
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

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}