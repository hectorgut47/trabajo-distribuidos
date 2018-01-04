package com.hector.distribuidos.trabajo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.hector.distribuidos.trabajo.model.PriceList;
import com.hector.distribuidos.trabajo.utils.AmazonScraperUtils;

public class AmazonPriceListServer {

	public static void main(String[] args) {
		
		Socket client;
		
		try (ServerSocket ss = new ServerSocket(12000)) {
			while (true) {
				client = ss.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String idProd;
				List<String> idList = new ArrayList<>();
				while ((idProd = reader.readLine()) != null && idProd.length() > 0) {
					idList.add(idProd);
				}
				PriceList originalList = AmazonScraperUtils.unmarshalProducts();
				List<String> filteredList = AmazonScraperUtils.mustCheckPrice(originalList, idList);
				PriceList list = new PriceList();
				if (filteredList.size() > 0) {
					AmazonScraper scraper = new AmazonScraper(filteredList);
					list = scraper.getPrices();
					AmazonScraperUtils.marshalProducts(list, originalList);
				}
				if (idList.size() > 0) {
					try {
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
						writer.write(AmazonScraperUtils.generateReport(idList));
						writer.newLine();
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (client != null) {
					try {
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
