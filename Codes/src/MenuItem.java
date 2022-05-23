import java.io.*;
import java.util.ArrayList;
//import java.util.Scanner;
public class MenuItem 
{
	private String itemId;
	private String name;
	private String description;
	private double price;
	private String type;
	
	public MenuItem()
	{
		
	}
	
	public MenuItem(String itemId, String name, String description, double price, String type)
	{
		this.itemId = itemId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.type = type;
	}
	
	public MenuItem(String itemId, String name, String description, double price)
	{
		this.itemId = itemId;
		this.name = name;
		this.description = description;
		this.price = price;
	}
	
	public ArrayList<MenuItem> retrieveMenu()
	{
		ArrayList<MenuItem> getM=new ArrayList<MenuItem>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("menuItems.txt"));
			String line=null;
			while((line=br.readLine())!=null)
			{
				String[] item=line.split("/");
				MenuItem v=new MenuItem(item[0], item[1], item[2],
						Double.parseDouble(item[3]), item[4]);
				getM.add(v);
			}
			br.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return getM;
	}
	
	public void print()
	{
		System.out.println("Item ID: " + this.getItemId());
		System.out.println("Name: " + this.getName());
		System.out.println("Description: " + this.getDescription());
		System.out.println("Price: " + String.format("%.2f", this.getPrice()));
		System.out.println("Type: " + this.getType());
		System.out.println("");
	}
	
	public void updateMenuItem(ArrayList<MenuItem> m)
	{
		deleteMenuItem(m);
	}
	
	public void deleteMenuItem(ArrayList<MenuItem> m)
	{
		String FILENAME = "menuItems.txt";
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
		  for(int i=0; i<m.size(); i++)
		  {
			  create.write(m.get(i).getItemId()+ "/"); 
			  create.write(m.get(i).getName()+ "/");
			  create.write(m.get(i).getDescription()+ "/");
			  create.write(m.get(i).getPrice()+ "/");
			  create.write(m.get(i).getType());
			  create.newLine();
		  }
		  create.close(); 
	    } 
	    catch (IOException e) 
	    { 
	      System.out.println("exception occoured" + e); 
	    } 
	}
	
	public void printSortedMenuItem(ArrayList<MenuItem> menu)
	{
		if (menu.size()!=0)
		{
			System.out.println("\t\t\tAla Carte Menu");
			System.out.println("------------------------------------------------------------------------");
			System.out.println("Main Course:");
	        for(int i=0;i<menu.size();i++)
	        {
	        	if(menu.get(i).getType().equals("mainCourse"))
	        		menu.get(i).print();
	        }
	        System.out.println("------------------------------------------------------------------------");
	        System.out.println("Sides:");
	        for(int i=0;i<menu.size();i++)
	        {
	        	if(menu.get(i).getType().equals("sides"))
	        		menu.get(i).print();
	        }
	        System.out.println("------------------------------------------------------------------------");
	        System.out.println("Drinks:");
	        for(int i=0;i<menu.size();i++)
	        {
	        	if(menu.get(i).getType().equals("drinks"))
	        		menu.get(i).print();
	        }
	        System.out.println("------------------------------------------------------------------------");
	        System.out.println("Desserts:");
	        for(int i=0;i<menu.size();i++)
	        {
	        	if(menu.get(i).getType().equals("dessert"))
	        		menu.get(i).print();
	        }
		}
		else
			System.out.println("Ala Carte Menu has no items! \n");
        
	}
	
	//-------------------------------------------------
	public String getItemId() 
	{
		return itemId;
	}
	public void setItemId(String itemId) 
	{
		this.itemId = itemId;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getDescription() 
	{
		return description;
	}
	public void setDescription(String description) 
	{
		this.description = description;
	}
	public double getPrice() 
	{
		return price;
	}
	public void setPrice(double price) 
	{
		this.price = price;
	}
	public String getType() 
	{
		return type;
	}
	public void setType(String type) 
	{
		this.type = type;
	}
	
	
	
}
