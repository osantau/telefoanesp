package oct.soft.dao.beans;

import java.io.Serializable;

public class BirouBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private int idoffice;
    private String birou;
    private String numere;
    private String number;
    private String branch;
    private String name;

    public BirouBean() {
        this.birou = "";
        this.numere = "";
    }

    public String getBirou() {
        return birou;
    }

    public void setBirou(String birou) {
        this.birou = birou;
    }

    public String getNumere() {
        return numere;
    }

    public void setNumere(String numere) {
        this.numere = numere;
    }

    public int getIdoffice() {
        return idoffice;
    }

    public void setIdoffice(int idoffice) {
        this.idoffice = idoffice;
    }

    public String getNumber() {
        if (number != null || !number.isEmpty()) {
            if (number.length() > 3) {
                number = number.substring(0, 4) + " " + number.substring(4, number.length());
            }
        }

        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BirouBean [idoffice=" + idoffice + ", birou=" + birou + ", numere=" + numere + ", number=" + number
                + ", branch=" + branch + ", name=" + name + "]";
    }

}
