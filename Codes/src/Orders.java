import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Orders {

	private int orderId;
	private int staffId;
	private int tableId;
	private int quantity;
	private double price;
	private String prodName;
	private String timestamp;
	
	String tmp[];
	Scanner sc=new Scanner(System.in);
	
	// Getter and setter
	public int getOrderId() 
	{
		return orderId;
	}

	public void setOrderId(int orderId) 
	{
		this.orderId = orderId;
	}

	public int getStaffId() 
	{
		return staffId;
	}

	public void setStaffId(int staffId) 
	{
		this.staffId = staffId;
	}

	public int getTableId() 
	{
		return tableId;
	}

	public void setTableId(int tableId) 
	{
		this.tableId = tableId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdName() {
		return prodName;
	}

	public Orders(int orderId, int staffId, int tableId, int quantity, double price, String prodName,
			String timestamp) {

		this.orderId = orderId;
		this.staffId = staffId;
		this.tableId = tableId;
		this.quantity = quantity;
		this.price = price;
		this.prodName = prodName;
		this.timestamp = timestamp;
	}
	
	public Orders() 
	{
		// TODO Auto-generated constructor stub
		
	}
	public Orders(int orderId)
	{
		this.orderId=orderId;
	}

//	public Orders createOrder(MenuItem m)
//	{
//		Orders o = new Orders();
//		o.setProdName(m.getName());
//		o.setPrice(m.getPrice());
//		o.se
//	}
	public ArrayList<Orders> UpdateOrders(int id)
	{
		ArrayList<Orders> update=new ArrayList<Orders>();
		ArrayList<Orders> temp=new ArrayList<Orders>();
		Orders view= new Orders();
		String FILENAME = "orderDetails.txt";
		BufferedReader read =null;
		try 
		{
			read = new BufferedReader(new FileReader(FILENAME));
			String line=read.readLine();
			while(line!=null)
			{
				String tmp[]=line.split("/");
				view = new Orders(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]),Integer.parseInt(tmp[3]), Double.parseDouble(tmp[4]),tmp[5],tmp[6]);
				temp.add(view);
			
			}
			for(int i=0; i<temp.size();i++)
			{
				if(temp.get(i).getOrderId() == id)
				{
					update.add(temp.get(i));
				}
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
        {
            try
            {
                //Closing the resources 
            	read.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
		return update;
	}
	public void readOrder(ArrayList<Orders> a) 
	{
		int k=0;
		double total=0;
		ArrayList<Orders> detail=a;
		
		Staff viewStaff = new Staff();
		String name = null;
		try {
			name = viewStaff.checkStaffName(detail.get(0).getStaffId());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("----------ABC Restaurant--------------");
		System.out.println("Served by: "+name);
		System.out.println("Order ID: " + detail.get(0).getOrderId());
		System.out.println("Table No. "+detail.get(0).getTableId());
		System.out.print("Orders last updated: " );
		System.out.println(detail.get(0).getTimestamp());
		
		System.out.println();
		System.out.println("------------Order Details-------------");
		for(k=0;k<detail.size();k++)
		{
			System.out.printf( "%-15s %15s %n", detail.get(k).getQuantity()+" "+ detail.get(k).getProdName(), (detail.get(k).getPrice()*detail.get(k).getQuantity()));
			total+=(detail.get(k).getQuantity() * detail.get(k).getPrice());
		}
		System.out.println("--------------------------------------");
		System.out.printf( "%-15s %15.2s %n", "Total:", total);
		System.out.println("");
	
	}
	
	public ArrayList<Orders> RetrieveOrder(int Id)
	{
		ArrayList<Orders> retrieve=new ArrayList<Orders>();
		ArrayList<Orders> temp=new ArrayList<Orders>();
		String FILENAME = "orderDetails.txt";
		String line=null;
		Orders view;
		try
		{
			BufferedReader out = new BufferedReader(new FileReader(FILENAME));
			
			while((line=out.readLine()) !=null)
			{
				
				String tmp1[]=line.split("/");
				view = new Orders(Integer.parseInt(tmp1[0]),Integer.parseInt(tmp1[1]), Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]), Double.parseDouble(tmp1[4]),tmp1[5],tmp1[6]);
				temp.add(view);
			}
			out.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		for(int i=0;i<temp.size();i++)
		{
			if(temp.get(i).getOrderId()==Id)
			{
				retrieve.add(temp.get(i));
			}
		}
		
		return retrieve;
	}
	
	public ArrayList<Orders> RetrieveOrderbyTable(int tableID)
	{
		ArrayList<Orders> retrieve=new ArrayList<Orders>();
		ArrayList<Orders> temp=new ArrayList<Orders>();
		String FILENAME = "orderDetails.txt";
		String line;
		Orders view;
		
		try
		{
			BufferedReader out = new BufferedReader(new FileReader(FILENAME));
			
			while((line=out.readLine()) !=null)
			{
				
				String tmp1[]=line.split("/");
				view = new Orders(Integer.parseInt(tmp1[0]),Integer.parseInt(tmp1[1]), Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]), Double.parseDouble(tmp1[4]),tmp1[5],tmp1[6]);
				temp.add(view);
			}
			out.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		for(int i=0;i<temp.size();i++)
		{
			if(temp.get(i).getTableId()==tableID)
			{
				retrieve.add(temp.get(i));
			}
		}
		
		return retrieve;
	}
	public ArrayList<Orders> retrieveOtherDetails(int id)//to retreive all other Orders except for the selected orderid
	{
		ArrayList<Orders> temp=new ArrayList<Orders>();
		ArrayList<Orders> rArray=new ArrayList<Orders>();
		String FILENAME = "orderDetails.txt";
		String line;
		Orders view;
		try
		{
			BufferedReader out = new BufferedReader(new FileReader(FILENAME));
			while((line=out.readLine()) !=null)
			{
				
				String tmp1[]=line.split("/");
				view = new Orders(Integer.parseInt(tmp1[0]),Integer.parseInt(tmp1[1]), Integer.parseInt(tmp1[2]),Integer.parseInt(tmp1[3]), Double.parseDouble(tmp1[4]),tmp1[5],tmp1[6]);
				temp.add(view);
			}
			out.close();
		}catch (IOException e)
		{
			e.printStackTrace();
		}
		for(int i=0;i<temp.size();i++)
		{
			if(temp.get(i).getOrderId()!=id)
			{
				rArray.add(temp.get(i));
			}
		}
		return rArray;
	}
	
	public void deleteOrders(ArrayList<Orders> o)
	{
		String FILENAME = "orderDetails.txt";
		//remove everyting
		PrintWriter writer = null;
		try 
		{
			writer = new PrintWriter(FILENAME);
		} catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		writer.print("");
		writer.close();
		// insert
		try 
	    {
	      BufferedWriter create = new BufferedWriter(new FileWriter(FILENAME, true)); 
	      for(int j=0;j<o.size();j++)
	    	  {
	    	  create.write(o.get(j).getOrderId()+ "/"); 
	    	  create.write(o.get(j).getStaffId()+ "/");
	    	  create.write(o.get(j).getTableId()+ "/");
	    	  create.write(o.get(j).getQuantity()+ "/");
	    	  create.write(o.get(j).getPrice()+ "/");
	    	  create.write(o.get(j).getProdName()+"/");
	    	  create.write(o.get(j).getTimestamp()+"/");
	    	  create.newLine();
	    	  }
	      create.close(); 
	    } 
	    catch (IOException e) 
	    { 
	      System.out.println("exception occoured" + e); 
	    } 
		
	}
	public void deleteOrders(int orderID) throws IOException {
		// Find the position
		String Sep = "/";
		String filename = "orderDetails.txt";
				
		ArrayList orderStrArr = (ArrayList)read(filename);
		ArrayList<Orders> orderArrList = new ArrayList<Orders>();
				
		for (int i = 0; i < orderStrArr.size(); i++) {
			String st = (String)orderStrArr.get(i);
			StringTokenizer star = new StringTokenizer(st , Sep);
			
			int orderIDtxt = Integer.parseInt(star.nextToken().trim());
			int staffIDtxt = Integer.parseInt(star.nextToken().trim());
			int tableIDtxt = Integer.parseInt(star.nextToken().trim());
			int quantitytxt = Integer.parseInt(star.nextToken().trim());
			double pricetxt = Double.parseDouble(star.nextToken().trim());
			String prodNametxt = star.nextToken().trim();
			String timeStamptxt = star.nextToken().trim();
					
			Orders o = new Orders(orderIDtxt, staffIDtxt, tableIDtxt, quantitytxt, pricetxt, prodNametxt, timeStamptxt);
			orderArrList.add(o);
		}
		
		// Clear All
		FileWriter ordertxt = new FileWriter("orderDetails.txt"); 
		PrintWriter clear = new PrintWriter(ordertxt);
		
		clear.flush();
		clear.close();
				
		for (int j = 0; j < orderArrList.size(); j++) {
			if (orderID != orderArrList.get(j).getOrderId()) {
				// Delete Table
				String FileName = "orderDetails.txt";
				try 
			    {
					BufferedWriter out = new BufferedWriter(new FileWriter(FileName,true)); 
					out.write(orderArrList.get(j).getOrderId() + "/");
					out.write(orderArrList.get(j).getStaffId() + "/");
					out.write(orderArrList.get(j).getTableId() + "/");
					out.write(orderArrList.get(j).getQuantity() + "/");
					out.write(orderArrList.get(j).getPrice() + "/");
					out.write(orderArrList.get(j).getProdName() + "/");
					out.write(orderArrList.get(j).getTimestamp() + "/");
					out.newLine();
					out.close(); 
			    } 
			    catch (IOException e) 
			    {
			      System.out.println("exception occured" + e); 
			    }
			}
		}
	}
	

	
	public int ReturnMaxID() 
	{
		String FILENAME = "orderDetails.txt";
		String line;
		Orders view;
		int i;
		int Maxid = 0;

		ArrayList<Orders> detail = new ArrayList<Orders>();
		try 
		{

			BufferedReader out = new BufferedReader(new FileReader(FILENAME));
			while ((line = out.readLine()) != null) 
			{

				String tmp[] = line.split("/");
				view = new Orders(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]),
						Integer.parseInt(tmp[3]), Double.parseDouble(tmp[4]), tmp[5], tmp[6]);
				detail.add(view);
			}
			out.close();
			if (detail.size() == 0) 
				return 0;
			else 
			{
				for (i = 0; i < detail.size(); i++) 
				{
					if (Maxid < detail.get(i).getOrderId()) 
					{
						Maxid = detail.get(i).getOrderId();
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Maxid;
	}
	
	public void createOrders(String FileName, ArrayList<Orders> str)
	{
		  try 
		    {
		      BufferedWriter out = new BufferedWriter(new FileWriter(FileName,true)); 
		      for(int i=0;i<str.size();i++)
		    	  {
		    	  out.write(str.get(i).getOrderId()+ "/"); 
		    	  out.write(str.get(i).getStaffId()+ "/");
		    	  out.write(str.get(i).getTableId()+ "/");
		    	  out.write(str.get(i).getQuantity()+ "/");
		    	  out.write(str.get(i).getPrice()+ "/");
		    	  out.write(str.get(i).getProdName()+"/");
		    	  out.write(str.get(i).getTimestamp()+"/");
		    	  out.newLine();
		    	  }
		      out.close(); 
		    } 
		    catch (IOException e) 
		    { 
		      System.out.println("exception occoured" + e); 
		    } 
		  System.out.println("Orders Submitted Successfully");
		  System.out.println(" ");
	}

	public int getOrderDetailsID()
	{
		
		Orders read=new Orders();
		int oID = 0;
		ArrayList<Orders> detail=new ArrayList<Orders>();
		System.out.print("Enter Order No. ");
			try
			{
			 oID=sc.nextInt();
			 detail=RetrieveOrder(oID);
				if(detail.size()==0)
				{
					System.out.println("Invalid Order No.!\n");
					System.out.print("Enter Order No. Again: ");
						oID=sc.nextInt();
						 detail=RetrieveOrder(oID);
						if(detail.size()==0)
						{
							System.out.println("\nInvalid Order No.!");
							System.out.println("Returning To Main Page! \n");
							
						}
						else
						{
							readOrder(detail);
							
						}	
				}
				else
				{
					readOrder(detail);
				}
			}catch(InputMismatchException ex)
			{
			System.out.println("Please Enter A Valid Number!\n");
			System.out.print("Enter Order No. Again: ");
			sc.next();
				try {
					oID=sc.nextInt();
					detail=read.RetrieveOrder(oID);
					if(detail.size()==0)
					{
					
						System.out.println("\nInvalid Order No.!");
						System.out.println("Returning To Main Page!\n");
						
					}
					else
					{
						read.readOrder(detail);
					
					}	
				}catch(InputMismatchException e)
				{
					sc.next();
					System.out.println("Please Enter A Valid Number!");
					System.out.println("Returning To Main Page!!!");
					
				}
			}
			return oID;
	}
	public static List read(String fileName) throws IOException 
	{
		ArrayList<String> data = new ArrayList<String>() ;
		String line;
		BufferedReader scan = new BufferedReader(new FileReader(fileName));
	    try 
	    {
	    	while ((line = scan.readLine()) != null)
	      	{
	    		data.add(line);
	      	}
	    }
	    finally
	    {
	    	scan.close();
	    }
	    return data;
	  }

}
