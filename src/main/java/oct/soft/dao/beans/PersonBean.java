package oct.soft.dao.beans;

import java.io.Serializable;

public class PersonBean implements Serializable{
 private static final long serialVersionUID = 1L;
	public PersonBean() {
		
		this.nume = "";
		this.prenume = "";
		this.numere = "";
		this.numar_asociat = "";
	}
	private String nume;
	private String prenume;
	private String numere;
	private String numar_asociat;
	
	//for search
	private String fname;
	private String lname;
	private String name;
	private String branch;
	private String number;
	private Integer idperson;
	private String telserv;
	private String telfix;
	private String telmobil;
	private Integer idoffice;
	
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public String getPrenume() {
		return prenume;
	}
	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}
	public String getNumere() {
		return numere;
	}
	public void setNumere(String numere) {
		this.numere = numere;
	}
	public String getNumar_asociat() {
		return numar_asociat;
	}
	public void setNumar_asociat(String numar_asociat) {
		this.numar_asociat = numar_asociat;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Integer getIdperson() {
		return idperson;
	}
	public void setIdperson(Integer idperson) {
		this.idperson = idperson;
	}
	public String getTelserv() {
		return telserv;
	}
	public void setTelserv(String telserv) {
		this.telserv = telserv;
	}
	public String getTelfix() {
		return telfix;
	}
	public void setTelfix(String telfix) {
		this.telfix = telfix;
	}
	public String getTelmobil() {
		return telmobil;
	}
	public void setTelmobil(String telmobil) {
		this.telmobil = telmobil;
	}
	
	
	public Integer getIdoffice() {
		return idoffice;
	}
	public void setIdoffice(Integer idoffice) {
		this.idoffice = idoffice;
	}

	public int numePrenumeLength() {
		return (getFname()+" "+getLname()).length();
	}
	@Override
	public String toString() {
		return "PersonBean [nume=" + nume + ", prenume=" + prenume + ", numere=" + numere + ", numar_asociat="
				+ numar_asociat + ", fname=" + fname + ", lname=" + lname + ", name=" + name + ", branch=" + branch
				+ ", number=" + number + ", idperson=" + idperson + ", telserv=" + telserv + ", telfix=" + telfix
				+ ", telmobil=" + telmobil + ", idoffice=" + idoffice + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idperson == null) ? 0 : idperson.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonBean other = (PersonBean) obj;
		if (idperson == null) {
			if (other.idperson != null)
				return false;
		} else if (!idperson.equals(other.idperson))
			return false;
		return true;
	}
	
	
	
	
	
}
