import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Table {
	private String timeStamp;
	private int tableID;
	private int pax;
	private boolean status;
	
	public Table() 
	{
		
	}
	
	public Table(String timeStamp, int tableID, int pax, boolean status) {
		this.timeStamp = timeStamp;
		this.tableID = tableID;
		this.pax = pax;
		this.status = status;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public int getTableID() {
		return tableID;
	}

	public void setTableID(int tableID) {
		this.tableID = tableID;
	}

	public int getPax() {
		return pax;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	// Delete Table
	public void deleteTable(int tableID) throws IOException {
		// Find the position
		String Sep = "/";
		String filename = "tables.txt";
		
		ArrayList tableStrArr = (ArrayList)read(filename);
		ArrayList<Table> tableArrList = new ArrayList<Table>();
		
		for (int i = 0; i < tableStrArr.size(); i++) {
			String st = (String)tableStrArr.get(i);
			StringTokenizer star = new StringTokenizer(st , Sep);
			
			String timetxt = star.nextToken().trim();
			int tableIDtxt = Integer.parseInt(star.nextToken().trim());
			int paxtxt = Integer.parseInt(star.nextToken().trim());
			boolean status = Boolean.parseBoolean(star.nextToken().trim());
			
			Table t = new Table(timetxt, tableIDtxt, paxtxt, status);
			tableArrList.add(t);
		}
		
		// Clear All
		FileWriter tabletxt = new FileWriter("tables.txt"); 
		PrintWriter clear = new PrintWriter(tabletxt);
		
		clear.flush();
		clear.close();
		
		for (int j = 0; j < tableArrList.size(); j++) {
		
			if (tableID != tableArrList.get(j).getTableID()) {
				String FileName = "tables.txt";
				try 
			    {
					BufferedWriter out = new BufferedWriter(new FileWriter(FileName,true)); 
					out.write(tableArrList.get(j).getTimeStamp() + "/");
					out.write(tableArrList.get(j).getTableID() + "/");
					out.write(tableArrList.get(j).getPax() + "/");
					out.write(tableArrList.get(j).isStatus() + "/");
					out.newLine();
					out.close(); 
			    } 
			    catch (IOException e) 
			    {
			      System.out.println("exception occoured" + e); 
			    }
			}
		} 
		
	}
	
	public static List read(String fileName) throws IOException 
	{
		ArrayList<String> data = new ArrayList<String>();
		String line;
		BufferedReader scan = new BufferedReader(new FileReader(fileName));
		try 
		{
			while ((line = scan.readLine()) != null) 
			{
				data.add(line);
			}
		} finally 
		{
			scan.close();
		}
		return data;
	}
	
	
	
}