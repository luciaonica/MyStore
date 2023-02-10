var data;
var totalGrossSales;
var totalNetSales;
var totalItems;

$(document).ready(function() {
	
	google.charts.load('current', {'packages': ['table']});
	google.charts.setOnLoadCallback(loadSalesReportByProduct);
	
	$(".button-sales-by-product").on("click", function(){	
		
		$(".button-sales-by-product").each(function(e){
			$(this).removeClass('btn-primary').addClass('btn-light');
		});
		
		$(this).removeClass('btn-light').addClass('btn-primary');
			
		period = $(this).attr("period");	
	
		loadSalesReportByProduct(period);
	});
});

function loadSalesReportByProduct(period) {
	requestURL = contextPath + "reports/sales_by_product/" + period;
	
	
	$.get(requestURL, function(responseJSON) {
		drawTable(responseJSON, period);
		console.log(responseJSON);
	});
}

function drawTable(responseJSON, period) {
        data = new google.visualization.DataTable();
	data.addColumn('string', 'Product');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');
	data.addColumn('number', 'Total Products');	
        
	totalGrossSales = 0.0;
 	totalNetSales = 0.0;
 	totalItems = 0; 
	$.each(responseJSON, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales, reportItem.productsCount]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalItems += parseInt(reportItem.productsCount);
	});
        

        var table = new google.visualization.Table(document.getElementById('chart_sales_by_product'));

        table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});
	$("#textTotalGrossSalesByProduct").text("$" + $.number(totalGrossSales, 2));
	$("#textTotalNetSalesByProduct").text("$" + $.number(totalNetSales, 2));
	
	denominator = getDenominator(period);
	
	$("#textAvgGrossSalesByProduct").text("$" + $.number(totalGrossSales / denominator, 2));
	$("#textAvgNetSalesByProduct").text("$" + $.number(totalNetSales / denominator, 2));
	$("#textTotalItemsByProduct").text(totalItems);
	$("#titleSalesByProduct").text(getChartTitle(period));
}

function getDenominator(period) {
	if (period == "last_7_days") return 7;
	if (period == "last_28_days") return 28;
	if (period == "last_6_months") return 6;
	if (period == "last_year") return 12;
	
	return 7;
}
	
function getChartTitle(period) {
	if (period == "last_7_days") return "Sales in Last 7 Days";
	if (period == "last_28_days") return "Sales in Last 28 Days";
	if (period == "last_6_months") return "Sales in Last 6 Months";
	if (period == "last_year") return "Sales in Last Year";
	
	return "Sales in Last 7 Days";
}

