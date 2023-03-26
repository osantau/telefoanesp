/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oct.soft.model;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author osantau
 */

public class Office implements Serializable {
        private static final long serialVersionUID = 1L;
	private Integer idoffice;        	
        @NotNull(message = "Introduceti o denumire !")
	@NotEmpty(message = "Campul Denumire este obligatoriu !")
	@Length(min = 3, max = 45, message = "Campul Denumire trebuie sa fie intre 3 si  45 caractere !")
	private String name;
	private int isbranch;
	private int parent;
	private Office branch;

	public Office() {
		this.idoffice = null;
	}

	public Office(String name, int isbranch, int parent) {
		this.idoffice = null;
		this.name = name;
		this.isbranch = isbranch;
		this.parent = parent;
	}

	public Integer getIdoffice() {
		return idoffice;
	}

	public void setIdoffice(Integer idoffice) {
		this.idoffice = idoffice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIsbranch() {
		return isbranch;
	}

	public void setIsbranch(int isbranch) {
		this.isbranch = isbranch;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public boolean isNew() {
		return this.idoffice == null ? true : false;
	}

	public void setBranch(Office branch) {
		this.branch = branch;
	}

	public Office getBranch() {
		return branch;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idoffice == null) ? 0 : idoffice.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Office other = (Office) obj;
		if (idoffice == null) {
			if (other.idoffice != null)
				return false;
		} else if (!idoffice.equals(other.idoffice))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Office{" + "idoffice=" + idoffice + ", name=" + name + ", isbranch=" + isbranch + ", parent=" + parent
				+ '}';
	}

}
