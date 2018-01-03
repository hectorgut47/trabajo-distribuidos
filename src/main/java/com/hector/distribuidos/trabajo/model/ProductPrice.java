package com.hector.distribuidos.trabajo.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ProductPrice implements Serializable {
	
	private String id_prod;
	private float price;
	private Date date;
	
	public ProductPrice () {}

	public ProductPrice(String id_prod, float price, Date date) {
		this.id_prod = id_prod;
		this.price = price;
		this.date = date;
	}
	
	@XmlAttribute
	public String getId_prod() {
		return id_prod;
	}

	public void setId_prod(String id_prod) {
		this.id_prod = id_prod;
	}
	
	@XmlElement(name = "price")
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@XmlElement(name = "date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
