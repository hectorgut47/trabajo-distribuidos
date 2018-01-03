package com.hector.distribuidos.trabajo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "priceList")
public class PriceList implements Serializable {
	
	private List<ProductPrice> prices = new ArrayList<>();
	
	public PriceList () {}
	
	public PriceList (List<ProductPrice> prices) {
		this.prices = prices;
	}
	
	@XmlElement(name = "productPrice")
	public List<ProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<ProductPrice> prices) {
		this.prices = prices;
	}

}
