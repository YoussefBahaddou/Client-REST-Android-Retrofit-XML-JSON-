package com.youssef.android.restclient.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import javax.xml.bind.annotation.XmlElement;

@Root(name = "item", strict = false)
public class Account {
    @Element(name = "id")
    private Long id;

    @Element(name = "solde")
    private double solde;

    @Element(name = "type")
    private String type;

    @Element(name = "dateCreation")
    private String dateCreation;

    public Account() {}

    public Account(Long id, double solde, String type, String dateCreation) {
        this.id = id;
        this.solde = solde;
        this.type = type;
        this.dateCreation = dateCreation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }


    @XmlElement(name = "id")
    public Long getId() { return id; }

    @XmlElement(name = "solde")
    public double getSolde() { return solde; }

    @XmlElement(name = "type")
    public String getType() { return type; }

    @XmlElement(name = "dateCreation")
    public String getDateCreation() { return dateCreation; }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", solde=" + solde +
                ", type='" + type + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                '}';
    }
}
