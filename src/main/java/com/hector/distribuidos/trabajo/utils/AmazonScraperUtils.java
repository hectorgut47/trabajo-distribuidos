package com.hector.distribuidos.trabajo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

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
		PriceList list = null;
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
					p -> !p.getId_prod().equals(id) || datediff(p.getDate(), new Date()) > REFRESH_TIME;
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

}
