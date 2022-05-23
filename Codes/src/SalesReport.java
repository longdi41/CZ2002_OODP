import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class SalesReport {
	/* Note: This class is Sales Revenue Report */
	/* Note Sales ==> Invoice (Black Diamond) */
	// Period
	private int day;
	private int month;
	private int year;
	private String item;
	private int quantity;
	private double revenue;
	
	public SalesReport(int day, int month, int year, String item, int quantity, double revenue) {
		this.day = day;
		this.month = month;
		this.year = year;
		this.item = item;
		this.quantity = quantity;
		this.revenue = revenue;
	}

	public SalesReport() {
		
	}
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	// Update Sale Revenue Report
	public void updateReport() {
		String FileName = "sales.txt";
		try 
	    {
			BufferedWriter out = new BufferedWriter(new FileWriter(FileName,true)); 
			out.write(this.day + "/");
			out.write(this.month + "/");
			out.write(this.year + "/");
			out.write(this.item + "/");
			out.write(this.quantity + "/");
			out.write(this.revenue + "/");
			out.newLine();
			out.close(); 
	    } 
	    catch (IOException e) 
	    {
	      System.out.println("exception occoured" + e); 
	    }
	}
	
	
	// Print Sale Revenue Report
	public void printReport() throws IOException  
	{
		// Retrieve Current Data
		String Sep = "/";
		String filename = "sales.txt";
		
		ArrayList salesStrArr = (ArrayList)read(filename);
		ArrayList<SalesReport> salesArrList = new ArrayList<SalesReport>();
				
		for (int i = 0 ; i < salesStrArr.size() ; i++) 
		{
			String st = (String)salesStrArr.get(i);
			StringTokenizer star = new StringTokenizer(st , Sep);
			
			int day = Integer.parseInt(star.nextToken().trim());
			int month = Integer.parseInt(star.nextToken().trim());
			int year = Integer.parseInt(star.nextToken().trim());
			String item = star.nextToken().trim();
			int quantity = Integer.parseInt(star.nextToken().trim());
			double revenue = Double.parseDouble(star.nextToken().trim());
					
			SalesReport s = new SalesReport(day, month, year, item, quantity, revenue);
			salesArrList.add(s);
		}
		
		// Set to today date
		Calendar calendar = new GregorianCalendar();
		int todayday = calendar.get(Calendar.DAY_OF_MONTH);
		int todaymonth = calendar.get(Calendar.MONTH) + 1;
		int todayyear = calendar.get(Calendar.YEAR);

		Scanner sc = new Scanner(System.in);
		System.out.println("View Report By: ");
		System.out.println("1. Day");
		System.out.println("2. Month");
		System.out.println("3. Year");
		int sort = sc.nextInt();
		
		switch (sort) {
		case 1:
			// Day
			int selectedDay = 0;
			int selectedMonth = 0;
			int selectedYear = 0;
			int validDate = 0;
			
			do {
				validDate = 1;
				System.out.println("\nSpecify the day: (Put -1 to quit) ");
				selectedDay = sc.nextInt();
				
				if (selectedDay == -1) {
					return;
				}
				
				System.out.println("\nNote: 1 - January, 2 - Febuary, 3 - March, etc.");
				System.out.println("Specify the month: (Put -1 to quit) ");
				selectedMonth = sc.nextInt();
				
				if (selectedMonth == -1) {
					return;
				}
				
				System.out.println("\nSpecify the year: (Put -1 to quit) ");
				selectedYear = sc.nextInt();
				
				if (selectedYear == -1) {
					return;
				}
				
				if (selectedYear > todayyear) {
					System.out.print("Error! The date has not been reached! Please try again.\n");
					validDate = 0;
				}
				else if (selectedYear == todayyear) {
					if (selectedMonth > todaymonth) {
						System.out.print("Error! The date has not been reached! Please try again.\n");
						validDate = 0;
					}
					else if (selectedMonth == todaymonth) {
						if (selectedDay > todayday) {
							System.out.print("Error! The date has not been reached! Please try again.\n");
							validDate = 0;
						}
					}
				}
			} while (validDate != 1);
			
			ArrayList<SalesReport> salesReportDay = new ArrayList<SalesReport>();
			
			for (int i = 0; i < salesArrList.size(); i++) {
				int targetDay = salesArrList.get(i).getDay();
				int targetMonth = salesArrList.get(i).getMonth();
				int targetYear = salesArrList.get(i).getYear();
				
				if (targetDay == selectedDay && targetMonth == selectedMonth && targetYear == selectedYear) {
					int validate = 0;
					for (int j = 0; j < salesReportDay.size(); j++) {
						if (salesReportDay.get(j).getItem().equals(salesArrList.get(i).getItem())) {
							int updatedQuantity = salesReportDay.get(j).getQuantity();
							updatedQuantity += salesArrList.get(i).getQuantity();
							
							double updatedCost = salesReportDay.get(j).getRevenue();
							updatedCost += salesArrList.get(i).getRevenue();
							
							SalesReport updated = new SalesReport(targetDay, targetMonth, targetYear, salesArrList.get(i).getItem(), updatedQuantity, updatedCost);
							salesReportDay.set(j, updated);
							validate = 1;
							break;
						}
					}
					if (validate == 0) {
						salesReportDay.add(salesArrList.get(i));
					}
				}
			}
			
			// Generate Report
			System.out.println("\nSales Revenue Report on " + selectedDay + "/" + selectedMonth + "/" + selectedYear);
			System.out.println("---------------------------------------------------------------------------");
			
			double totalRevenue = 0.0;
			
			for (int k = 0; k < salesReportDay.size(); k++) 
			{
				System.out.printf("%-5s %25s %15.2f", salesReportDay.get(k).getQuantity(), salesReportDay.get(k).getItem(), salesReportDay.get(k).getRevenue());
				System.out.print("\n");
				totalRevenue += salesReportDay.get(k).getRevenue();
			}
			System.out.println("---------------------------------------------------------------------------");
			System.out.printf("%-15s %30.2f", "Total Revenue: ", totalRevenue);
			System.out.print("\n\n");
			break;
			
		case 2:
			// Month
			int selectedMonth2 = 0;
			int selectedYear2 = 0;
			int validDate2 = 0;
			
			do {
				validDate2 = 1;
				System.out.println("\nNote: 1 - January, 2 - Febuary, 3 - March, etc.");
				System.out.println("Specify the month: (Put -1 to quit) ");
				selectedMonth2 = sc.nextInt();
				
				if (selectedMonth2 == -1) {
					return;
				}
				
				System.out.println("\nSpecify the year: (Put -1 to quit) ");
				selectedYear2 = sc.nextInt();
				
				if (selectedYear2 == -1) {
					return;
				}
				
				if (selectedYear2 > todayyear) {
					System.out.print("Error! The date has not been reached! Please try again.\n");
					validDate2 = 0;
				}
				else if (selectedYear2 == todayyear) {
					if (selectedMonth2 > todaymonth) {
						System.out.print("Error! The date has not been reached! Please try again.\n");
						validDate2 = 0;
					}
				}
			} while (validDate2 != 1);
			
			ArrayList<SalesReport> salesReportMonth = new ArrayList<SalesReport>();
			
			for (int i = 0; i < salesArrList.size(); i++) {
				int targetMonth = salesArrList.get(i).getMonth();
				int targetYear = salesArrList.get(i).getYear();
							
				if (targetMonth == selectedMonth2 && targetYear == selectedYear2) {
					int validate = 0;
					
					for (int j = 0; j < salesReportMonth.size(); j++) {
						if (salesReportMonth.get(j).getItem().equals(salesArrList.get(i).getItem())) {
							int updatedQuantity = salesReportMonth.get(j).getQuantity();
							updatedQuantity += salesArrList.get(i).getQuantity();
										
							double updatedCost = salesReportMonth.get(j).getRevenue();
							updatedCost += salesArrList.get(i).getRevenue();
										
							SalesReport updated = new SalesReport(salesArrList.get(i).getDay(), targetMonth, targetYear, salesArrList.get(i).getItem(), updatedQuantity, updatedCost);
							salesReportMonth.set(j, updated);
							validate = 1;
							break;
						}
					}
					
					if (validate == 0) {
						salesReportMonth.add(salesArrList.get(i));
					}
				}
			}
						
			// Generate Report
			String monthstr = "";
			switch (selectedMonth2) {
			case 1:
				monthstr = "January";
				break;
			case 2:
				monthstr = "Febuary";
				break;
			case 3:
				monthstr = "March";
				break;
			case 4:
				monthstr = "April";
				break;
			case 5:
				monthstr = "May";
				break;
			case 6:
				monthstr = "June";
				break;
			case 7:
				monthstr = "July";
				break;
			case 8:
				monthstr = "August";
				break;
			case 9:
				monthstr = "September";
				break;
			case 10:
				monthstr = "October";
				break;
			case 11:
				monthstr = "November";
				break;
			case 12:
				monthstr = "December";
				break;
			default:
				monthstr = "";
				break;
			}
			
			
			System.out.println("\nSales Revenue Report in " + monthstr + " " + selectedYear2);
			System.out.println("---------------------------------------------------------------------------");
						
			double totalMonthRevenue = 0.0;
						
			for (int k = 0; k < salesReportMonth.size(); k++) 
			{
				System.out.printf("%-5s %25s %15.2f", salesReportMonth.get(k).getQuantity(), salesReportMonth.get(k).getItem(), salesReportMonth.get(k).getRevenue());
				System.out.print("\n");
				totalMonthRevenue += salesReportMonth.get(k).getRevenue();
			}
			System.out.println("---------------------------------------------------------------------------");
			System.out.printf("%-15s %30.2f", "Total Revenue: ", totalMonthRevenue);
			System.out.print("\n\n");
			break;
			
		case 3:
			// Year
			int selectedYear3 = 0;
			
			do {
				System.out.println("\nSpecify the year: (Put -1 to quit) ");
				selectedYear3 = sc.nextInt();
				
				if (selectedYear3 == -1) {
					return;
				}
				
				if (selectedYear3 > todayyear) {
					System.out.print("Error! The date has not been reached! Please try again.\n");
				}
			} while (selectedYear3 > todayyear);
			
			
			ArrayList<SalesReport> salesReportYear = new ArrayList<SalesReport>();
			
			for (int i = 0; i < salesArrList.size(); i++) {
				int targetYear = salesArrList.get(i).getYear();
							
				if (targetYear == selectedYear3) {
					int validate = 0;
					
					for (int j = 0; j < salesReportYear.size(); j++) {
						if (salesReportYear.get(j).getItem().equals(salesArrList.get(i).getItem())) {
							int updatedQuantity = salesReportYear.get(j).getQuantity();
							updatedQuantity += salesArrList.get(i).getQuantity();
										
							double updatedCost = salesReportYear.get(j).getRevenue();
							updatedCost += salesArrList.get(i).getRevenue();
										
							SalesReport updated = new SalesReport(salesArrList.get(i).getDay(), salesArrList.get(i).getMonth(), targetYear, salesArrList.get(i).getItem(), updatedQuantity, updatedCost);
							salesReportYear.set(j, updated);
							validate = 1;
							break;
						}
					}
					if (validate == 0) {
						salesReportYear.add(salesArrList.get(i));
					}
				}
			}
						
			// Generate Report
			System.out.println("\nSales Revenue Report in the year " + selectedYear3);
			System.out.println("---------------------------------------------------------------------------");
						
			double totalYearRevenue = 0.0;
						
			for (int k = 0; k < salesReportYear.size(); k++) 
			{
				System.out.printf("%-5s %25s %15.2f", salesReportYear.get(k).getQuantity(), salesReportYear.get(k).getItem(), salesReportYear.get(k).getRevenue());
				System.out.print("\n");
				totalYearRevenue += salesReportYear.get(k).getRevenue();			
			}
			System.out.println("---------------------------------------------------------------------------");
			System.out.printf("%-15s %30.2f", "Total Revenue: ", totalYearRevenue);
			System.out.print("\n\n");
			break;
			
		default:
			System.out.println("Error! Please try again!");
			break;
		}
	}
	
	 public static List read(String fileName) throws IOException {
			ArrayList<String> data = new ArrayList<String>() ;
			String line;
			BufferedReader scan = new BufferedReader(new FileReader(fileName));
		    try {
		      while ((line = scan.readLine()) != null){
		        data.add(line);
		      }
		    }
		    finally{
		      scan.close();
		    }
		    return data;
		  }
	 

}
