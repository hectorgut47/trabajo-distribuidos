package com.hector.distribuidos.trabajo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.hector.distribuidos.trabajo.model.PriceList;
import com.hector.distribuidos.trabajo.model.ProductPrice;

public class AmazonScraperUtils {
	
	private final static long REFRESH_TIME = 5;
	
	public static void marshalProducts (PriceList prods, PriceList originalProds) {
		try {
			List<ProductPrice> prodList = prods.getPrices();
			prodList.addAll(originalProds.getPrices());
			PriceList allProds = new PriceList(prodList);
			JAXBContext ctxt = JAXBContext.newInstance(PriceList.class);
			Marshaller m = ctxt.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(allProds, new File("products.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static PriceList unmarshalProducts () {
		PriceList list = new PriceList();
		File f = new File ("products.xml");
		if (f.exists() && f.isFile()) {
			try {
				JAXBContext ctxt = JAXBContext.newInstance(PriceList.class);
				Unmarshaller um = ctxt.createUnmarshaller();
				FileReader file = new FileReader("products.xml");
				list = (PriceList) um.unmarshal(file);
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static List<String> mustCheckPrice (PriceList list, List<String> idList) {
		if (list != null) {
			List<String> filteredList = new ArrayList<>();
			for (String id: idList) {
				Predicate<ProductPrice> pred = 
					p -> p.getId_prod().equals(id) && datediff(p.getDate(), new Date()) < REFRESH_TIME;
				if (!list.getPrices().stream().anyMatch(pred)) 
					filteredList.add(id);
			}
			return filteredList;
		} else return idList;
	}
	
	private static long datediff (Date d1, Date d2) {
	    long diff = d2.getTime() - d1.getTime();
	    return (TimeUnit.MINUTES).convert(diff,TimeUnit.MILLISECONDS);
	}
	
	public static String generateReport(List<String> idList) {
		PriceList list = unmarshalProducts();
		StringBuffer sb = new StringBuffer();
		sb.append("---------------" + System.lineSeparator());
		sb.append("-------" + "PRICES REPORT" + "-------" + System.lineSeparator());
		sb.append("---------------" + System.lineSeparator());
		idList.forEach(id -> {
			String report = generateIndividualReport(id,list);
			if (report.length() > 0) {
				sb.append(report);
				sb.append("---------------" + System.lineSeparator());
			}
		});
		return sb.toString();
	}
	
	private static String generateIndividualReport(String id, PriceList list) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
		Map<Date,Float> map = new LinkedHashMap<>();
		StringBuffer sb = new StringBuffer();
		List<ProductPrice> filteredList = list.getPrices().stream()
			.filter(p -> p.getId_prod().equals(id))
			.collect(Collectors.toList());
		if (filteredList.size() > 0) {
			filteredList.forEach(p -> map.put(p.getDate(), p.getPrice()));
			List<Date> dates = map.keySet().stream().sorted((d1,d2) -> d1.compareTo(d2)).collect(Collectors.toList());
			sb.append("Product id: " + id + System.lineSeparator());
			sb.append("  Original price: " + map.get(dates.get(0)) + " EUR at " + sdf.format(dates.get(0)) + System.lineSeparator());
			dates.remove(0);
			dates.forEach(d -> {
				sb.append("  - Price at " + sdf.format(d) + " : " + map.get(d) + " EUR" + System.lineSeparator());
			});
		}
		return sb.toString();
	}

}
