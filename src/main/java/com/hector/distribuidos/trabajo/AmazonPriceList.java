package com.hector.distribuidos.trabajo;

import java.util.ArrayList;
import java.util.List;

import com.hector.distribuidos.trabajo.model.PriceList;
import com.hector.distribuidos.trabajo.utils.AmazonScraperUtils;

public class AmazonPriceList {

	public static void main(String[] args) {
		
		List<String> lista = new ArrayList<>();
		lista.add("B00WKB2MQW");
		lista.add("B0098DXQ5Q");
		PriceList originalList = AmazonScraperUtils.unmarshalProducts();
		List<String> filteredList = AmazonScraperUtils.mustCheckPrice(originalList, lista);
		if (filteredList.size() > 0) {
			AmazonScraper scraper = new AmazonScraper(filteredList);
			PriceList list = scraper.getPrices();
			AmazonScraperUtils.marshalProducts(list);
		}
		PriceList list2 = AmazonScraperUtils.unmarshalProducts();
		list2.getPrices().forEach(p -> System.out.println(p.getId_prod() + " " + p.getPrice() + " " + p.getDate()));
	
	}

}
