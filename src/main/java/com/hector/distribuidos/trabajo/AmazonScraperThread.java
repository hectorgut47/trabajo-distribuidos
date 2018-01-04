package com.hector.distribuidos.trabajo;

import java.io.DataInputStream;
import java.io.IOException;
<<<<<<< HEAD
=======
import java.io.InputStream;
>>>>>>> fix/secure-connection
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.hector.distribuidos.trabajo.model.ProductPrice;

public class AmazonScraperThread implements Runnable {
	
	private String id_prod;
	private CyclicBarrier cb;
	private Date exec_time;
	private ProductPrice prodprice = null;
	private Exception e = null;
	
	public AmazonScraperThread(String id_prod, CyclicBarrier cb, Date exec_time) {
		super();
		this.id_prod = id_prod;
		this.cb = cb;
		this.exec_time = exec_time;
	}

	public ProductPrice getResult () throws Exception {
		if (e != null) 
			throw e;
		else return prodprice;
	}

	public void run () {
		try {
			URL url = new URL("https://www.amazon.es/gp/product/" + id_prod);
			HttpURLConnection con = (HttpURLConnection) url.openConnection ();
			con.setRequestMethod("GET");
			con.connect();
			int code = con.getResponseCode();
			if (code < 400) {
				DataInputStream html = new DataInputStream((InputStream)con.getContent());
				String line;
				while ((line = html.readLine()) != null) {
					if (line.contains("id=\"priceblock_ourprice\""))
						break;
				}
				String price = line.substring(line.indexOf("EUR")+4, line.indexOf("</span>"));
				NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es","ES"));
				this.prodprice = new ProductPrice(id_prod,nf.parse(price).floatValue(),exec_time);
			}
			cb.await();
		} catch (MalformedURLException e) {
			this.e = e;
			e.printStackTrace();
		} catch (IOException e) {
			this.e = e;
			e.printStackTrace();
		} catch (ParseException e) {
			this.e = e;
			e.printStackTrace();
		} catch (InterruptedException e) {
			this.e = e;
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			this.e = e;
			e.printStackTrace();
		}
	}

}
