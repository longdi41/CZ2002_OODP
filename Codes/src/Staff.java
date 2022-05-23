import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Staff {

	private int staffId;
	private String staffName;
	private char gender;
	private String position;
	private int number;
	
	// getter and setter
	public int getStaffId() {
		return staffId;
	}
	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	//constructor
	public Staff()
	{
		
	}
	
	public Staff(int staffId, String staffName, char gender, String position, int number) {
		super();
		this.staffId = staffId;
		this.staffName = staffName;
		this.gender = gender;
		this.position = position;
		this.number = number;
	}
	
	public String checkStaffName(int id) throws NumberFormatException, IOException
	{
		BufferedReader brStream = new BufferedReader(new FileReader("StaffDetails.txt"));
		String detailLine=null,staffName = null;
		Staff details;
		ArrayList<Staff> staffs=new ArrayList<Staff>();
		String[] staffvalues = null;
		int i=0;
		
		
		while ((detailLine = brStream.readLine()) != null) 
		{
			staffvalues = detailLine.split("/");
			details = new Staff(Integer.parseInt(staffvalues[0]), staffvalues[1], staffvalues[2].charAt(0),
					staffvalues[3], Integer.parseInt(staffvalues[4]));
			staffs.add(details);
		
		}
		brStream.close();

		// get staff Name
		for (i = 0; i < staffs.size(); i++)
		{
			if (staffs.get(i).getStaffId() == id)
			{
				staffName = staffs.get(i).getStaffName();
				break;
			}
		}
		if(staffName==null)
		{
			return null;
		}
		return staffName;
	}
	
	public void validate(int validateStaffID) throws IOException 
	{
		String sep = "/";
		
		// Loop and Validate
		String filename = "staffDetails.txt";
		
		// Read String from text file
		ArrayList stringArray = (ArrayList)read(filename);
		
		// Store data
		ArrayList<Staff> staffArray = new ArrayList<Staff>();
		
		for (int i = 0 ; i < stringArray.size() ; i++) {
			String st = (String)stringArray.get(i);
			StringTokenizer star = new StringTokenizer(st , sep);

			int staffId = Integer.parseInt(star.nextToken().trim());
			String name = star.nextToken().trim();
			String gender = star.nextToken().trim();
			String role = star.nextToken().trim();
			String contact = star.nextToken().trim();
			
			if (validateStaffID == staffId) {
				// Add in roles using "||"
				if (role.equals("Manager")) {
					SalesReport s = new SalesReport();
					s.printReport();
				}
				else {
					System.out.println("Invalid Access.\n");
					return;
				}
			}
		}
	}

	  /** Read the contents of the given file. */
	  public static List read(String fileName) throws IOException {
		ArrayList<String> data = new ArrayList() ;
		String line;
		BufferedReader scanner = new BufferedReader(new FileReader(fileName));
	    try {
	      while ((line = scanner.readLine()) != null){
	        data.add(line);
	      }
	    }
	    finally{
	      scanner.close();
	    }
	    return data;
	  }
}
