package com.hector.distribuidos.trabajo;

import java.util.ArrayList;
import java.util.List;

import com.hector.distribuidos.trabajo.model.ProductPrice;

public class AmazonPriceList {

	public static void main(String[] args) {
		
		List<String> lista = new ArrayList<>();
		lista.add("B00WKB2MQW");
		AmazonScraper scraper = new AmazonScraper(lista);
		List<ProductPrice> list = scraper.getPrices();
		for(ProductPrice pp: list) {
			System.out.println(pp.getId_prod() + " " + pp.getPrice() + " " + pp.getDate());
		}
		
	}

}
