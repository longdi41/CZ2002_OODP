import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReservationDetail {
	private String name;

	private String date;
	private String arrivalTime;
	private int pax;
	private int tableId;
	private int contactNo;
		public ReservationDetail(String n,String  d,String  a,int p,int t)
		{
			this.name = n;
			this.date= d;
			this.arrivalTime=a;
			this.pax=p;
			this.tableId = t;
		}
		public ReservationDetail()
		{
			
		}
		
		public ReservationDetail(int contactNo, String name,String date,String arrivalTime,int pax,int tableId)
		{
			this.contactNo=contactNo;
			this.name=name;
			this.date=date;
			this.arrivalTime=arrivalTime;
			this.pax=pax;
			this.tableId=tableId;
		}
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getArrivalTime() {
			return arrivalTime;
		}

		public void setArrivalTime(String arrivalTime) {
			this.arrivalTime = arrivalTime;
		}

		public int getPax() {
			return pax;
		}

		public void setPax(int pax) {
			this.pax = pax;
		}

		public int getTableId() {
			return tableId;
		}

		public void setTableId(int tableId) {
			this.tableId = tableId;
		}

		public int getContactNo() {
			return contactNo;
		}

		public void setContactNo(int contactNo) {
			this.contactNo = contactNo;
		}
		public ArrayList<ReservationDetail> getReservation(int c)
		{
			ArrayList<ReservationDetail> update=new ArrayList<ReservationDetail>();
			ArrayList<ReservationDetail> temp=new ArrayList<ReservationDetail>();
			
			ReservationDetail view= new ReservationDetail();
			String FILENAME = "res.txt";
			BufferedReader read =null;
			try 
			{
				read = new BufferedReader(new FileReader(FILENAME));
				String line=null;
				while((line=read.readLine())!=null)
				{
					String tmp[]=line.split("/");
					view = new ReservationDetail(Integer.parseInt(tmp[0]),tmp[1],tmp[2],tmp[3],Integer.parseInt(tmp[4]),Integer.parseInt(tmp[5]));
					temp.add(view);
				
				}
				for(int i=0; i<temp.size();i++)
				{
					if(temp.get(i).getContactNo() ==c)
					{
						update.add(temp.get(i));
						return update;
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
}
