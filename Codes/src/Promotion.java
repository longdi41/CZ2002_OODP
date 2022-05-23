import java.io.*;
import java.util.ArrayList;

public class Promotion extends MenuItem
{
	private double discountPrice; 
	
	public Promotion()
	{
		
	}
	
	public Promotion(String itemId, String name, String description, double price, double discountPrice)
	{
		super(itemId,name,description,price);
		this.discountPrice = discountPrice;
	}
	
	public ArrayList<Promotion> GetMenu()
	{
		ArrayList<Promotion> getP=new ArrayList<Promotion>();
		try
		{
		   BufferedReader br = new BufferedReader(new FileReader("promotionSet.txt"));
		   String pdata=null;
		   while((pdata=br.readLine())!=null)
		   {
				String[] pItem=pdata.split("/");
				Promotion v=new Promotion(pItem[0],pItem[1],pItem[2],Double.parseDouble(pItem[3]),Double.parseDouble(pItem[4]));
				getP.add(v);
		   }
		   br.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return getP;
	}
	
	public void print()
	{
		System.out.println("Item ID: " + this.getItemId());
		System.out.println("Name: " + this.getName());
		System.out.println("Description: " + this.getDescription());
		System.out.println("Original Price: " + String.format("%.2f", this.getPrice()));
		System.out.println("Discounted Price: " + this.getDiscountPrice());
		System.out.println("");
	}
	
	public void updatePromotion(ArrayList<Promotion> p)
	{
		deletePromotion(p);
	}
	
	public void deletePromotion(ArrayList<Promotion> p)
	{
		String FILENAME = "promotionSet.txt";
		//remove everyting
		PrintWriter writer = null;
		try 
		{
			writer = new PrintWriter(FILENAME);
		} 
		catch (FileNotFoundException e1)
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
		  for(int i=0; i<p.size(); i++)
		  {
			  create.write(p.get(i).getItemId()+ "/"); 
			  create.write(p.get(i).getName()+ "/");
			  create.write(p.get(i).getDescription()+ "/");
			  create.write(p.get(i).getPrice()+ "/");
			  create.write(p.get(i).getDiscountPrice() + "/");
			  create.newLine();
		  }
		  create.close(); 
	    } 
	    catch (IOException e) 
	    { 
	    	System.out.println("exception occoured" + e); 
	    } 
		
	}
	
	//----------------------------------------------------
	public double getDiscountPrice() 
	{
		return discountPrice;
	}
	public void setDiscountPrice(double discountPrice) 
	{
		this.discountPrice = discountPrice;
	}
}
