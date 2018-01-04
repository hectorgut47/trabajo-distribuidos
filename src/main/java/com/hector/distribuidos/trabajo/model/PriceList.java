package com.hector.distribuidos.trabajo.model;

import com.hector.distribuidos.trabajo.model.ProductPrice;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
