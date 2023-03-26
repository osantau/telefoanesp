package oct.soft.model;

import java.io.Serializable;
import org.hibernate.validator.constraints.NotEmpty;

public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer iduser;
    @NotEmpty(message = "Utilizator este obligatoriu !")
    private String username;
    @NotEmpty(message = "Parola este obligatorie !")
    private String password;
    
    private int enabled = 1;

    public UserInfo() {
    }

    public UserInfo(Integer iduser,String username, String password, int enabled) {
        this.iduser = iduser;
        this.username = username;
        this.password = password;
        this.enabled=enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public Integer getIduser() {
        return iduser;
    }

    public void setIduser(Integer iduser) {
        this.iduser = iduser;
    }
    public boolean isNew(){
        return iduser==null;
    }
    
    @Override
    public String toString() {
        return "UserInfo{" + "iduser=" + iduser + ", username=" + username + ", password=" + password + ", enabled=" + enabled + '}';
    }
    

}
