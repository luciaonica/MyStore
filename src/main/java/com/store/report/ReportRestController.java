package com.store.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportRestController {
	@Autowired private MasterOrderReportService masterOrderReportService;
	
	@GetMapping("/reports/sales_by_date/{period}")
	public List<ReportItem> getReportDataByDatePeriod(@PathVariable("period") String period) {
		
		switch (period) {
			case "last_7_days":
				return masterOrderReportService.getReportDataLast7Days();
				
			case "last_28_days":
				return masterOrderReportService.getReportDataLast28Days();
				
			case "last_6_months":
				return masterOrderReportService.getReportDataLast6Months();
					
			case "last_year":
				return masterOrderReportService.getReportDataLastYear();
				
			default:
				return masterOrderReportService.getReportDataLast7Days();
		}	
		
	}
	
	@GetMapping("/reports/sales_by_product/{period}")
	public List<ReportItem> getReportDataByProductPeriod(@PathVariable("period") String period) {
		
		switch (period) {
			case "last_7_days":
				return masterOrderReportService.getReportDataLast7DaysByProduct();
				
			case "last_28_days":
				return masterOrderReportService.getReportDataLast28DaysByProduct();
				
			case "last_6_months":
				return masterOrderReportService.getReportDataLast6MonthsByProduct();
					
			case "last_year":
				return masterOrderReportService.getReportDataLastYearByProduct();
				
			default:
				return masterOrderReportService.getReportDataLast7DaysByProduct();
		}	
		
	}
	
}
