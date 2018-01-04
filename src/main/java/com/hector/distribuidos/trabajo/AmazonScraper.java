package com.hector.distribuidos.trabajo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hector.distribuidos.trabajo.model.PriceList;
import com.hector.distribuidos.trabajo.model.ProductPrice;

public class AmazonScraper {

	private List<String> id_list;
	private Date exec_time;
	
	public AmazonScraper (List<String> id_list) {
		this.id_list = id_list;
		this.exec_time = new Date();
	}
	
	public PriceList getPrices () {
		List<ProductPrice> list_prices = new ArrayList<>();
		final CyclicBarrier cb = new CyclicBarrier(id_list.size() + 1);	
		ExecutorService pool = Executors.newFixedThreadPool(id_list.size());
		try {
			List<AmazonScraperThread> threads = new ArrayList<>();
			id_list.forEach(id_prod -> 
				threads.add(new AmazonScraperThread(id_prod,cb,exec_time))
			);
			threads.forEach(th ->
				pool.execute(th)
			);
			cb.await();
			for (AmazonScraperThread th: threads) {
				ProductPrice pp = th.getResult();
				if (pp != null) 
					list_prices.add(pp);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.shutdown();
		}
		return new PriceList(list_prices);
	}
	
}
