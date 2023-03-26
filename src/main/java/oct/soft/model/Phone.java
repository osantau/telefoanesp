package oct.soft.model;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class Phone {

    private Integer idphone;
    @NotNull(message = "Numarul de telefon este obligatoriu!")
    @NotEmpty(message = "Completati numarul de telefon!")
    private String number;
    private int fix=0;
    private int interior=0;
    private int mobil=0;
    private int serv=0;
    private int idoffice=0;
    private int idperson=0;

    public Phone() {

    }

    public int getIdphone() {
        return idphone;
    }

    public void setIdphone(int idphone) {
        this.idphone = idphone;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getFix() {
        return fix;
    }

    public void setFix(int fix) {
        this.fix = fix;
    }

    public int getInterior() {
        return interior;
    }

    public void setInterior(int interior) {
        this.interior = interior;
    }

    public int getMobil() {
        return mobil;
    }

    public void setMobil(int mobil) {
        this.mobil = mobil;
    }

    public int getServ() {
        return serv;
    }

    public void setServ(int serv) {
        this.serv = serv;
    }

    public int getIdoffice() {
        return idoffice;
    }

    public void setIdoffice(int idoffice) {
        this.idoffice = idoffice;
    }

    public int getIdperson() {
        return idperson;
    }

    public void setIdperson(int idperson) {
        this.idperson = idperson;
    }

    public String getTypeLabel() {
        if (getFix() == 1) {
            return "Fix";
        } else if (getMobil() == 1) {
            return "Mobil";
        } else if (getServ() == 1) {
            return "Serviciu";
        } else if (getInterior() == 1) {
            return "Interior";
        }
        return "";
    }

    public boolean isNew() {
        return idphone == null ? true : false;
    }

}
