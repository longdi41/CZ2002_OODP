import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;

public class Invoice {
	static HashMap<String, HashMap<Integer, ArrayList<Integer>>> dateWiseTables = new HashMap<String, HashMap<Integer, ArrayList<Integer>>>();
	private int tableNo;
	private int orderID;
	
	private int i=0;
	public Invoice() 
	{
		
	}
	
	public Invoice(int tableNo, int orderID, int day, int month, int year) {
		this.tableNo = tableNo;
		this.orderID = orderID;
	}

	public int getTableNo() {
		return tableNo;
	}

	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	// Print Bill Invoice
	public void printBill(ArrayList<Orders> orderArr) 
	{
		Scanner sc = new Scanner(System.in);

		// Delete Table
		Table t = new Table();
		try 
		{
			t.deleteTable(orderArr.get(i).getTableId());
		}
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Table ID successfully found! Printing bill...");
		
		double totalPrice = 0.0;
		
		System.out.println("\nNanyang Eating House");
		System.out.println("**********************");
		System.out.println("50 Nanyang Avenue #01-16/17/18");
		System.out.println("Telephone: 123-456-7890");
		System.out.println();
		
		System.out.println("Order ID: " + orderArr.get(0).getOrderId());
		System.out.println("Order Date: " + orderArr.get(0).getTimestamp());
		System.out.println("Table ID: " + orderArr.get(0).getTableId());
		System.out.println("-------------------------------------------------------------------");
		
		for (int i=0; i<orderArr.size(); i++)
		{
			System.out.print(orderArr.get(i).getQuantity() + " " + orderArr.get(i).getProdName() + "\t\t");
			System.out.printf("%.2f", orderArr.get(i).getPrice());
			System.out.print("\n");
			totalPrice += orderArr.get(i).getPrice();
		}
		
		
		System.out.println("-------------------------------------------------------------------");
		System.out.print("SubTotal \t\t");
		System.out.printf("%.2f", totalPrice);
		System.out.print("\n");
		System.out.print("7% GST \t\t\t");
		double gst = totalPrice * 0.07;
		System.out.printf("%.2f", gst);
		System.out.print("\n");
		totalPrice += gst;
		System.out.print("Total \t\t\t");
		System.out.printf("%.2f", totalPrice);
		System.out.print("\n");
		
		System.out.println("\nThank you for dining with us!\n");
		
		// Update to sales report
		// Update Sales Revenue Report
		// Set to today date
		Calendar calendar = new GregorianCalendar();
		int cday = calendar.get(Calendar.DAY_OF_MONTH);
		int cmonth = calendar.get(Calendar.MONTH) + 1;
		int cyear = calendar.get(Calendar.YEAR);
		
		SalesReport s = new SalesReport(cday, cmonth, cyear, orderArr.get(0).getProdName(), orderArr.get(0).getQuantity(), orderArr.get(0).getPrice());
		s.updateReport();	
		
		// Delete Order
		Orders or = new Orders();
		try
		{
			or.deleteOrders(orderArr.get(0).getOrderId());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getCurrentDateTime() {
		final DateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		String currDate = sdf.format(new Date());
		final DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String currTime = timeFormat.format(new Date());
		int time = Integer.parseInt(currTime.substring(0, 2)+ currTime.substring(3));
		currDate += (time<1430) ? "AM" : "PM";
		return currDate;
	}
	
}
