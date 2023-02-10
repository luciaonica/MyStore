package com.store.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.entity.Order;
import com.store.order.OrderRepository;

@Service
public class MasterOrderReportService {
	@Autowired private OrderRepository repo;
	
	private DateFormat dateFormatter;
	
	public List<ReportItem> getReportDataLast7Days() {
		return getReportDataLastXDays(7);		
	}
	
	public List<ReportItem> getReportDataLast28Days() {
		return getReportDataLastXDays(28);		
	}
	
	public List<ReportItem> getReportDataLastXDays(int days) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
		Date startTime = cal.getTime();
				
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		
		return getReportDataByDateRange(startTime, endTime, "days");		
	}

	private List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime, String period) {
		 List<Order> listOrders = repo.findByOrderTimeBetween(startTime, endTime);
		 
		 List<ReportItem> listReportItems = createReportData(startTime, endTime, period);
		 
		 calculateSalesForReportData(listOrders, listReportItems);
		 
		 return listReportItems;
	}
	
	private void calculateSalesForReportData(List<Order> listOrders, List<ReportItem> listReportItems) {
		for (Order order : listOrders) {
			String orderDateString = dateFormatter.format(order.getOrderTime());
			
			ReportItem reportItem = new ReportItem(orderDateString);
			
			int itemIndex = listReportItems.indexOf(reportItem);
			
			if (itemIndex >= 0) {
				reportItem = listReportItems.get(itemIndex);
				
				reportItem.addGrossSales(order.getTotal());
				reportItem.addNetSales(order.getTotal() - order.getCost());
				reportItem.increaseOrdersCount();				
			}
		}
	}
	
	private List<ReportItem>  createReportData(Date startTime, Date endTime, String period) {
		List<ReportItem> listReportItems = new ArrayList<>();
		
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(startTime);
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(endTime);
		
		Date currentDate = startDate.getTime();
		String dateString = dateFormatter.format(currentDate);
		
		listReportItems.add(new ReportItem(dateString));
		
		do {
			if (period.equals("days")) {
				startDate.add(Calendar.DAY_OF_MONTH, 1);
			} else if (period.equals("months")) {
				startDate.add(Calendar.MONTH, 1);
			}
			currentDate = startDate.getTime();
			dateString = dateFormatter.format(currentDate);
			
			listReportItems.add(new ReportItem(dateString));
		} while (startDate.before(endDate));
		
		return listReportItems;
	}


	public List<ReportItem> getReportDataLast6Months() {
		return getReportDataLastXMonths(6);
	}
	
	public List<ReportItem> getReportDataLastYear() {
		return getReportDataLastXMonths(12);
	}
	
	public List<ReportItem> getReportDataLastXMonths(int months) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -(months - 1));
		Date startTime = cal.getTime();
		
		dateFormatter = new SimpleDateFormat("yyyy-MM");
		
		return getReportDataByDateRange(startTime, endTime, "months");		
	}

	// ----By Product-------
	
	public List<ReportItem> getReportDataLast7DaysByProduct() {
		return getReportDataLastXDaysByProduct(7);
	}

	private List<ReportItem> getReportDataLastXDaysByProduct(int days) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
		Date startTime = cal.getTime();
		
		return getReportDataByDateRangeByProduct(startTime, endTime);		
	}
	
	private List<ReportItem> getReportDataByDateRangeByProduct(Date startTime, Date endTime) {
		 List<Order> listOrders = repo.findByProductAndTimeBetween(startTime, endTime);
		 
		// printRawData(listOrders);
		 
		 List<ReportItem> listReportItems = new ArrayList<>();
		 
		 for (Order order : listOrders) {
			String identifier = "";
			identifier = order.getProduct().getName();	
			ReportItem reportItem = new ReportItem(identifier);
			
			float grossSales = order.getTotal();
			float netSales = order.getTotal() - order.getCost();
						
			int itemIndex = listReportItems.indexOf(reportItem);
			
			if (itemIndex >= 0) {
				reportItem = listReportItems.get(itemIndex);
				
				reportItem.addGrossSales(grossSales);
				reportItem.addNetSales(netSales);
				
				reportItem.increaseProductsCount(order.getQuantity());				
			} else {
				listReportItems.add(new ReportItem(identifier, grossSales, netSales, order.getQuantity(), order.getProduct().getPrice()));
			}
		 }
		 
		 //printReportData(listReportItems);
		 
		 return listReportItems;
	}	
	
	private void printReportData(List<ReportItem> listReportItems) {
		for (ReportItem item : listReportItems) {
			System.out.printf("%-20s, %10.2f, %10.2f, %d \n",
					item.getIdentifier(), item.getGrossSales(), item.getNetSales(), item.getProductsCount());
		}
		
	}
	
	private void printRawData(List<Order> listOrders) {
		for (Order order : listOrders) {
			System.out.printf("%d, %-20s, %10.2f, %10.2f \n",
					order.getQuantity(), order.getProduct().getName(), 
					order.getTotal(), order.getCost());
		}
		
	}

	public List<ReportItem> getReportDataLast28DaysByProduct() {
		return getReportDataLastXDaysByProduct(28);
	}

	public List<ReportItem> getReportDataLast6MonthsByProduct() {
		return getReportDataLastXMonthsByProduct(6);
	}

	private List<ReportItem> getReportDataLastXMonthsByProduct(int months) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -(months - 1));
		Date startTime = cal.getTime();
		
		dateFormatter = new SimpleDateFormat("yyyy-MM");
		
		return getReportDataByDateRangeByProduct(startTime, endTime);	
	}

	public List<ReportItem> getReportDataLastYearByProduct() {
		return getReportDataLastXMonthsByProduct(12);
	}	
}