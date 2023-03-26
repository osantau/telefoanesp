package oct.soft.model;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idperson;

    @NotNull(message = "Introduceti un nume !")
    @NotEmpty(message = "Campul Nume este obligatoriu !")
    @Length(min = 3, max = 45, message = "Campul Nume trebuie sa fie intre 3 si  45 caractere !")
    private String fname;

    @NotNull(message = "Introduceti un Prenume !")
    @NotEmpty(message = "Campul Prenume este obligatoriu !")
    @Length(min = 3, max = 45, message = "Campul Prenume trebuie sa fie intre 3 si  45 caractere !")
    private String lname;
    private String nickname;
    private int telint_id;
    private Office office;
    private String birou;
    private String filiala;

    public Person() {

    }

    public Integer getIdperson() {
        return idperson;
    }

    public void setIdperson(Integer idperson) {
        this.idperson = idperson;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getTelint_id() {
        return telint_id;
    }

    public void setTelint_id(int telint_id) {
        this.telint_id = telint_id;
    }

    public boolean isNew() {
        return this.idperson == null;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getBirou() {
        return birou;
    }

    public void setBirou(String birou) {
        this.birou = birou;
    }

    public String getFiliala() {
        return filiala;
    }

    public void setFiliala(String filiala) {
        this.filiala = filiala;
    }

    @Override
    public String toString() {
        return "Person [idperson=" + idperson + ", fname=" + fname + ", lname=" + lname + ", nickname=" + nickname
                + ", telint_id=" + telint_id + ", office=" + office + ", birou=" + birou + ", filiala=" + filiala + "]";
    }

}
