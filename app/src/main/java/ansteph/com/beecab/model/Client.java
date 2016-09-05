package ansteph.com.beecab.model;

/**
 * Created by loicStephan on 12/07/16.
 */
public class Client {


  private String  id;
  private String  name;
    private String email;
    private String mobile;
    private String password;
private String apikey;
    private byte propic;

    public Client(){}

    public Client(String id, String name, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;


    }

    public Client(String id, String name, String email, String mobile, String apikey) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getPropic() {
        return propic;
    }

    public void setPropic(byte propic) {
        this.propic = propic;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}
