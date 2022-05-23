import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;

import java.util.*;
import java.time.*;
import java.text.*;
import java.io.*;

public class Restaurant_Application 
{
	static HashMap<Integer, ReservationDetail > reservations = new HashMap<Integer, ReservationDetail>();
	static HashMap<String, HashMap<Integer, ArrayList<Integer>>> dateWiseTables = new HashMap<String, HashMap<Integer, ArrayList<Integer>>>();
	static Scanner in = new Scanner(System.in);	
	
	public static void main(String[] args) throws IOException, ParseException
	{
		Scanner sc=new Scanner(System.in);
		int fail = 0;
				
		File [] fileArray = new File [3];
		fileArray[0] = new File("menuItems.txt");
		fileArray[1] = new File("promotionSet.txt");
		fileArray[2] = new File("tables.txt");
		File tempFile = new File("temp.txt");
		
		
		try 	// Check if files exist, if not create new files
		{
			for (int i=0; i<fileArray.length; i++)
			{
				if (fileArray[i].createNewFile())
					System.out.println(fileArray[i].getName() + " is created!");
				else
					removeEmptyLines(fileArray[i], tempFile);
			}
		}catch (IOException e) 
		{
		   e.printStackTrace();
		}
		
		MenuItem m1 = new MenuItem();
		ArrayList<MenuItem> alaCarte = m1.retrieveMenu();
		
		Promotion p1 = new Promotion();
		ArrayList<Promotion> promotion = p1.GetMenu();
		String [] menuTypes = {"mainCourse", "sides", "drinks", "dessert"};
		ArrayList<Orders> getOrder=new ArrayList<Orders>();
		
//		Get data for files -J

		BufferedReader tReader = new BufferedReader(new FileReader("tables.txt"));
		BufferedReader rReader = new BufferedReader(new FileReader("res.txt"));

		String strCurrentLine;
		while ((strCurrentLine = tReader.readLine()) != null) 
		{
			String [] currTable = strCurrentLine.split("/");
			
			String date = currTable[0];
			int cap = Integer.parseInt(currTable[2]);
			int tID = Integer.parseInt(currTable[1]);

			if(dateWiseTables.get(date) == null) {	
				dateWiseTables.put(date, new HashMap<Integer, ArrayList<Integer>>());
			}
			
			ArrayList<Integer> tempp = new ArrayList<Integer>();
			tempp.add(tID);

			if(dateWiseTables.get(date).get(cap) != null) dateWiseTables.get(date).get(cap).add(tID);
			
			else dateWiseTables.get(date).put(cap, tempp);

	    }
		strCurrentLine = "";
		
		while ((strCurrentLine = rReader.readLine()) != null) 
		{
			String [] currRes = strCurrentLine.split("/");	
			reservations.put(Integer.parseInt(currRes[0]), new ReservationDetail(currRes[1], currRes[2], currRes[3], Integer.parseInt(currRes[4]), Integer.parseInt(currRes[5])));
	    }

//		Finish getting data from files -J
		
		int option=0;
		Staff viewStaff=new Staff();
		int staffId=0;
		String staffName=null;
		do 
		{
			fail = 0;
			option=-1;
			boolean ok = false;
			while (!ok)
			{
			    try 
			    {
			    	System.out.print("Enter Your Staff Id: ");
			    	staffId=sc.nextInt();
			        ok = true;
			    }
			    catch (InputMismatchException e) 
			    {
			    	sc.next(); // clears the buffer
			    	fail++;
			    	System.out.println("Invalid input!\n");
			    }
			}
			//get staff name
			staffName=viewStaff.checkStaffName(staffId);
			while(staffName==null)
			{
				System.out.println("Please return a valid staff id!!\n");
				System.out.println("Enter Your Staff Id Again: ");
				staffId=sc.nextInt();
				staffName=viewStaff.checkStaffName(staffId);
				if (staffName != null)
					break;
			}
				do 
				{
					System.out.println("Restaurant Reservation and Point of Sale System (RRPSS)");
					System.out.println("1. Create/Update/Remove menu item");
					System.out.println("2. Create/Update/Remove promotion");
					System.out.println("3. Create order");
					System.out.println("4. View order");
					System.out.println("5. Add/Remove order item/s to/from order");
					System.out.println("6. Create reservation booking");
					System.out.println("7. Check/Remove reservation booking");	
					System.out.println("8. Check table availability ");
					System.out.println("9. Print bill invoice ");
					System.out.println("10. Print sale revenue report by period (eg day or month)");
					System.out.println("11. Exit\n");
					
					System.out.print("Enter option: ");
					option = sc.nextInt();
//					boolean ok = false;
//					while (!ok && fail<2)
//					{
//					    try 
//					    {
//					        option = sc.nextInt();
//					        ok = true;
//					    }
//					    catch (InputMismatchException e) 
//					    {
//					    	sc.next(); // clears the buffer
//					    	fail++;
//					    	System.out.println("Invalid option!\n");
//					    	if (fail>=2)
//					    		ok = true;
//					    	System.out.print("Enter option: ");
//					    }
//					}
					
					System.out.println("");
					
					
//					Delete reservations before Date.now()
					ArrayList<Integer> toDelete = new ArrayList<Integer>();
					ReservationDetail res;
					DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
					for (int i : reservations.keySet()) 
					{
						res = reservations.get(i);
						
						String dateOfRes = res.getDate().substring(0,res.getDate().length()-2) + " " + add30Mins(res.getArrivalTime());
//						System.out.println(dateOfRes);
						if(dateTimeFormat.parse(dateTimeFormat.format(new Date())).compareTo(dateTimeFormat.parse(dateOfRes)) > 0) {
							toDelete.add(i);
						}
					}
					
					for(int i=0;i<toDelete.size();i++) 
					{
						removeReservation(toDelete.get(i));
					}
					//end deleting old reservations
					
					switch (option)
					{
					//orders
						case 1:
							System.out.println("1. Create menu item");
							System.out.println("2. Update menu item");
							System.out.println("3. Remove menu item");
							System.out.println("4. Exit");
							int choice = checkInvalidInt("Enter option: ");
							System.out.println("");
							if (choice == 1)	//create	
							{
								int itemId = 1;
								if (alaCarte.size() > 0)
									itemId = Integer.parseInt(alaCarte.get(alaCarte.size()-1).getItemId())+1;
								
								sc.nextLine();
								System.out.print("Enter name: ");
								String name = sc.nextLine();
								System.out.print("Enter description: ");
								String desc = sc.nextLine();
								double price = checkInvalidDouble("Enter price: ");
								if (price==-1)
									break;
								else
								{
									price = checkNegativeDouble(price);
									if (price==-1)
										break;
								}
								
								System.out.print("Enter type (mainCourse/sides/drinks/dessert): ");
								String type = sc.next();
								
								fail = 0;
								boolean wrong = true;						
								while (wrong && fail<2)
								{
									for (int i=0; i<4; i++)
									{
										if (type.equals(menuTypes[i]))
										{
											wrong = false;
											break;
										}	
									}
									fail++;
									if (wrong == false || fail==2)
										break;
									
									System.out.println("Invalid input!\n");
									System.out.print("Enter type (mainCourse/sides/drinks/dessert): ");
									type = sc.next();
								}
								if (!wrong)
								{
									MenuItem m = new MenuItem(Integer.toString(itemId), name, desc, price, type);
									if (alaCarte.add(m))
									{
										System.out.println("Menu Item created.\n");
										String s = itemId + "/" + name + "/" + desc + "/" + String.format("%.2f", price) + "/" + type + "\n";
										appendStrToFile(fileArray[0], s);
									}	
									else
										System.out.println("Error occured while creating Menu Item!\n");
								}
							}
							else if (choice == 2)		//update menu item
							{
								m1.printSortedMenuItem(alaCarte);
								if (alaCarte.size()==0)
									break;
								else
								{
									System.out.print("Enter the ItemID of the item you want to update: ");
									String id = sc.next();
									MenuItem m = retrieveMenuItem(id, alaCarte);
									if (m==null)
									{
										System.out.println("Item " + id + " could not be found.\n");
										break;
									}
									else
										m.print();
									
									int updateChoice = 0;
									do
									{
										System.out.println("1. Update Name");
										System.out.println("2. Update Description");
										System.out.println("3. Update Price");
										System.out.println("4. Update Type");
										System.out.println("5. Exit");
										updateChoice = checkInvalidInt("Enter option: ");
										System.out.println("");
										if (updateChoice == 1) //update name
										{
											sc.nextLine();
											System.out.print("Enter new name: ");
											String newName = sc.nextLine();
											m.setName(newName);
											m.updateMenuItem(alaCarte);
											System.out.println("Menu Item successfully updated.\n");
										}
										else if (updateChoice == 2)
										{
											sc.nextLine();
											System.out.print("Enter new description: ");
											String newDesc = sc.nextLine();
											m.setDescription(newDesc);
											m.updateMenuItem(alaCarte);
											System.out.println("Menu Item successfully updated.\n");
										}
										else if (updateChoice == 3)
										{
											double newPrice = checkInvalidDouble("Enter new price: ");
											if (newPrice==-1)
												break;
											else
											{
												newPrice = checkNegativeDouble(newPrice);
												if (newPrice==-1)
													break;
											}
											m.setPrice(newPrice);
											m.updateMenuItem(alaCarte);
											System.out.println("Menu Item successfully updated.\n");
										}
										else if (updateChoice == 4)
										{
											System.out.print("Enter new type (mainCourse, sides, drinks, dessert): ");
											//validate
											String newType = sc.next();
											
											fail = 0;
											boolean wrong = true;						
											while (wrong && fail<2)
											{
												for (int i=0; i<4; i++)
												{
													if (newType.equals(menuTypes[i]))
													{
														wrong = false;
														break;
													}	
												}
												fail++;
												if (wrong == false || fail==2)
													break;
												
												System.out.println("Invalid input!\n");
												System.out.print("Enter type (mainCourse/sides/drinks/dessert): ");
												newType = sc.next();
											}
											if (!wrong)
											{
												m.setType(newType);
												m.updateMenuItem(alaCarte);
												System.out.println("Menu Item successfully updated.\n");
											}
										}
										else if (updateChoice == 5)
											break;
										else if (updateChoice == -1)
											break;
										else
											System.out.println("Invalid option!\n");
									}while (updateChoice>0 && updateChoice<6);
								}
							}
							else if (choice == 3)		//delete
							{
								m1.printSortedMenuItem(alaCarte);
								if (alaCarte.size()==0)
									break;
								else
								{
									System.out.print("Enter the Item ID of the item you want to delete: ");
									String id = sc.next();
									int index =0;
									
									MenuItem m = retrieveMenuItem(id, alaCarte);
									if (m != null)
									{
										for (int i=0; i<alaCarte.size(); i++)
										{
											if (alaCarte.get(i).getItemId().equals(id))
												index = i;
										}
										System.out.println("");
										m.print();
										System.out.println("Confirm (Y/N): ");
										String confirm = sc.next();
										if (confirm.equalsIgnoreCase("y"))
										{
											alaCarte.remove(index);
											m.deleteMenuItem(alaCarte);
											System.out.println("Menu Item has been deleted.\n");
										}
										else if (confirm.equalsIgnoreCase("n"))
											break;
										else
										{
											//validation
										}
									}
									else
										System.out.println("Item " + id + " could not be found.\n");
								}
							}
							else if (choice == 4)
								break;
							else if (choice == -1)
								break;
							else
								System.out.println("Invalid option!\n");
							break;
							
							// crud promo
						case 2:				//promotion
							System.out.println("1. Create Promotion");
							System.out.println("2. Update Promotion");
							System.out.println("3. Remove Promotion");
							System.out.println("4. Exit");
							choice = checkInvalidInt("Enter option: ");
							System.out.println("");
							
							if (choice == 1)
							{
								String desc = "Promotion set consists of ";
								String confirm;
								double price = 0.0;
								
								int id = 0;
								if (alaCarte.size()==0)
								{
									m1.printSortedMenuItem(alaCarte);
									break;
								}
								while (id != -1)
								{
									m1.printSortedMenuItem(alaCarte);
									if (alaCarte.size()==0)
										break;
									else
									{
										System.out.println("Enter Item ID of the item you would like to add to Promotion: ");
										System.out.println("Enter -1 when finished adding\n");
										
										id = sc.nextInt();
										if (id == -1)
											break;
										MenuItem m = retrieveMenuItem(Integer.toString(id), alaCarte);
										if (m != null)
										{
											m.print();
											System.out.print("Confirm (Y/N): ");
											confirm = sc.next();
											if (confirm.equalsIgnoreCase("y"))
											{	
												int qty = checkInvalidInt("Enter quantity: ");
												if (qty>0)
												{
													desc = desc + qty + " " + m.getName() + ", ";
													price += qty*m.getPrice();
												}
												else
													break;
											}
											else if (confirm.equalsIgnoreCase("n"))
												break;
											else
											{
												//validate
											}
										}
										else
											System.out.println("Item could not be found.\n");
									}
								}
								
								String temp = desc.substring(0, desc.length()-2);
								desc = temp + ".";
								sc.nextLine();
								
								if (price >0)
								{
									System.out.print("Enter name of Promotion Set: ");
									String name = sc.nextLine();
									System.out.println("The total price of the set is: " + String.format("%.2f", price));
									double discPrice = checkInvalidDouble("Enter price of Promotion Set: ");
									if (discPrice==-1)
										break;
									else
									{
										discPrice = checkNegativeDouble(discPrice);
										if (price==-1)
											break;
									}
									String promotionId = "P1";
									if (promotion.size() > 0)
									{
										int num = Character.getNumericValue(promotion.get(promotion.size()-1).getItemId().charAt(1))+1;
										promotionId = "P" + num;
									}
									
									Promotion p = new Promotion(promotionId, name, desc, price, discPrice);
									if (promotion.add(p))
									{
										System.out.println("Promotion created! \n");
										String s = promotionId + "/" + name + "/" + desc + "/" + String.format("%.2f", price) + "/" + String.format("%.2f", discPrice) + "\n";
										appendStrToFile(fileArray[1], s);
									}
								}
								else
									System.out.println("No item in promotion, failed to create promotion!\n");
								
							}
							else if (choice == 2)
							{
								if (promotion.size()==0)
								{
									System.out.println("There are no promotions!\n");
									break;
								}
								else
								{
									for (int i=0; i<promotion.size(); i++)
										promotion.get(i).print();
										
									System.out.print("Enter the ItemID of the item you want to update: ");
									String id = sc.next();
									Promotion p = retrievePromotion(id, promotion);
									if (p == null)
									{
										System.out.println("Promotion " + id + "could not be found.\n");
										break;
									}
									else
									{
										p.print();
										
										int updateChoice = 0;
										do
										{
											System.out.println("1. Update Name");
											System.out.println("2. Update Description");
											System.out.println("3. Update Discounted Price");
											System.out.println("4. Exit");
											updateChoice = checkInvalidInt("Enter option: ");
											System.out.println("");
											if (updateChoice == 1) //update name
											{
												sc.nextLine();
												System.out.print("Enter new name: ");
												String newName = sc.nextLine();
												p.setName(newName);
												p.updatePromotion(promotion);
												System.out.println("Promotion successfully updated.\n");
											}
											else if (updateChoice == 2)
											{
												sc.nextLine();
												System.out.print("Enter new description: ");
												String newDesc = sc.nextLine();
												p.setDescription(newDesc);
												p.updatePromotion(promotion);
												System.out.println("Promotion successfully updated.\n");
											}
											else if (updateChoice == 3)
											{
												double newDiscPrice = checkInvalidDouble("Enter new discounted price: ");
												if (newDiscPrice==-1)
													break;
												else
												{
													newDiscPrice = checkNegativeDouble(newDiscPrice);
													if (newDiscPrice==-1)
														break;
												}
												p.setDiscountPrice(newDiscPrice);
												p.updatePromotion(promotion);
												System.out.println("Promotion successfully updated.\n");
											}
											else if (updateChoice == 4)
												break;
											else if (updateChoice == -1)
												break;
											else
												System.out.println("Invalid option!\n");
											
										}while (updateChoice>0 && updateChoice<5);
									}
								}
							}
							else if (choice == 3) //delete
							{
								if (promotion.size() == 0)
								{
									System.out.println("There are no promotions!\n");
									break;
								}
								else
								{
									for (int i =0; i<promotion.size(); i++)
										promotion.get(i).print();
									
									System.out.print("Enter the Item ID of the item you want to delete: ");
									String id = sc.next();
									int index = 0;
									
									Promotion p = retrievePromotion(id, promotion);
									if (p != null)
									{
										for (int i=0; i<promotion.size(); i++)
										{
											if (promotion.get(i).getItemId().equals(id))
												index = i;
										}
										p.print();
										System.out.print("Confirm (Y/N): ");
										String confirm = sc.next();
										if (confirm.equalsIgnoreCase("y"))
										{
											promotion.remove(index);
											p.deletePromotion(promotion);
											System.out.println("Promotion has been deleted.\n");
										}
									}
									else
										System.out.println("Promotion " + id + "could not be found.\n");
								}
							}
							else if (choice == 4)
								break;
							else if (choice == -1)
								break;
							else	
								System.out.println("Invalid option!\n");
							
							break;
							
					//create Order
						case 3:
						{
						ArrayList<Staff> sDetail = new ArrayList<Staff>();
						ArrayList<Orders> orderArray = new ArrayList<Orders>();
						int pax = 0;
						int tableId=0, orderopt, orderId, quantity;
						String prodId = null;
						int continueOpt = 0;
						int counter = 0;
						double price = 0;
						String prodName = null;
						char checkout;
						String FILENAME = "orderDetails.txt";

						System.out.println("Do You Have Any Reservation? Y/N");
						char reserveOpt = sc.next().charAt(0);
						reserveOpt = Character.toLowerCase(reserveOpt);
						switch (reserveOpt) 
						{
							case 'y': 
							{
								System.out.println("Enter Contact No. ");
								int contact = sc.nextInt();
								
								ReservationDetail result = removeRes(contact);
								if(result != null) System.out.println("Proceed To Table :" + result.getTableId());
								
								
//								ReservationDetail j = new ReservationDetail();
//								ArrayList<ReservationDetail> checkR = j.getReservation(contact);
//								DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yy");
//								LocalDateTime now = LocalDateTime.now();
//								String currentDate = null;
//								if (checkR.size() != 0) 
//								{
//									for (int i = 0; i < checkR.size(); i++) 
//									{
//										String data = checkR.get(i).getDate();
//										data = data.substring(0, data.length() - 2);
//										currentDate = dtf.format(now);
//										if (currentDate.equals(data)) 
//										{
//											tableId = checkR.get(i).getTableId();
//											System.out.println("Proceed To Table :" + tableId);
//											removeRes(contact);
//											break;
//										
//										}
//									}
//								}
								if (result == null)
									System.out.println("Do Not Have Any Reservation!\n");
								else 
									break;
							}
						    case 'n':
						    	  pax=checkInvalidInt("Enter No. Of Pax: ");
						    	  if (pax == -1)
						    		  break;
						    	  else if (pax <=0)
						    	  {
						    		  System.out.println("Please enter a positive value!\n");
						    		  System.out.print("Enter No. Of Pax: ");
						    		  pax = sc.nextInt();
						    		  if (pax<=0)
						    		  {
						    			  System.out.println("Invalid input!\n");
						    			  break;
						    		  }
						    	  }
									
						    	  //change Table status and become occupied
						    	  tableId = createTable(roundUp(pax), getCurrentDateTime());
						    	  System.out.println("Proceed To Table: " +tableId);
						    	  break;
							}
							
							
							MenuItem m=new MenuItem();
							Promotion p=new Promotion();
							MenuItem m2=new MenuItem();
							Promotion check=new Promotion();
							/*choose ala Carte or Set Meal*/
							DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							LocalDateTime now = LocalDateTime.now();
							 System.out.println();
							Orders getmaxId=new Orders();
							orderId=getmaxId.ReturnMaxID();
						
							orderId++;
							
							do 
							{
								System.out.println("\n--------Choose option--------");
								System.out.println("1: Ala Carte ");
								System.out.println("2: Promotion Set Package");
								System.out.println("3: Submit Orders");
								System.out.print("Enter option: ");
								orderopt=sc.nextInt();
								
								switch(orderopt)
								{
									case 1: /*ala carte*/
									{
										m.printSortedMenuItem(alaCarte);
										counter=0;
										//Place Orders
										System.out.println("\n---------Place Orders---------");
										do 
										{
											System.out.print("Enter Item ID: ");
											prodId=sc.next();
											 m2= retrieveMenuItem(prodId,alaCarte);

											if(m2==null)
											{
												System.out.println("Invalid Item ID !! Please Enter Again!");
												System.out.print("Enter Item ID: ");
												prodId=sc.next();
												counter+=1;
												m2= retrieveMenuItem(prodId,alaCarte);
												if(m2==null)
												{
													System.out.println("Invalid Item ID!! Returning To Main Menu!!");
													break;
												}
												else
												{
													quantity=checkInvalidInt("Enter Quantity: ");
													if (quantity<=0)
														break;
													else
													{
														Orders order=new Orders(orderId, staffId, tableId, quantity, m2.getPrice(), m2.getName(),dtf.format(now));
														orderArray.add(order);
														counter+=1;
													}
													break;
												}
											}
											else
											{
												quantity=checkInvalidInt("Enter Quantity: ");
												if (quantity<=0)
													break;
												else
												{
													Orders order=new Orders(orderId, staffId, tableId, quantity, m2.getPrice(), m2.getName(),dtf.format(now));
													orderArray.add(order);
													counter+=1;
												}
												break;
											}
											
										}while(m2!=null |counter>2);

										break;
										
									}
									case 2: /* promotion*/
									{
										counter=0;
										if (promotion.size()==0)
										{
											System.out.println("There are no promotions!\n");
											break;
										}
										else
										{
											System.out.println("--------Promotion Set Menu--------");
											
											for(int i=0;i<promotion.size();i++)
											{
												
													System.out.println("Item Id: "+promotion.get(i).getItemId());
													System.out.println("Name:       "+promotion.get(i).getName());
													System.out.println("Original Price:  "+"$"+promotion.get(i).getPrice());
													System.out.println("Promo Price:     "+"$"+promotion.get(i).getDiscountPrice());		
													System.out.println("");
											}
											//Place Orders
											System.out.println("---------Place Orders---------");
											do 
											{
												System.out.print("Enter Item ID: ");
												prodId=sc.next();
												check=retrievePromotion(prodId,promotion);
												if(check==null)
												{
													System.out.println("Invalid Item ID !! Please Enter Again!");
													System.out.println("Enter Item ID: ");
													prodId=sc.next();
													counter+=1;
													check=retrievePromotion(prodId,promotion);
													if(check==null)
													{
														System.out.println("Invalid Item ID!! Returning To Main Menu!!");
														break;
													}
													else
													{
														quantity=checkInvalidInt("Enter Quantity: ");
														if (quantity<=0)
															break;
														else
														{
															Orders order=new Orders(orderId, staffId, tableId, quantity,check.getDiscountPrice(), check.getName(),dtf.format(now));
															orderArray.add(order);
															counter+=1;
														}
														break;
													}
												}
												else 
												{
													quantity=checkInvalidInt("Enter Quantity: ");
													if (quantity<=0)
														break;
													else
													{
														Orders order=new Orders(orderId, staffId, tableId, quantity, check.getDiscountPrice(), check.getName(),dtf.format(now));
														orderArray.add(order);
													}
													break;
												}
											}while(check!=null|counter>=2);
										}
										break;
									}
								}
							}while(orderopt<=2);
							//get current order details
							if (orderopt!= 3)
								break;
							if(orderArray.size() == 0)
							{
								System.out.println("No items in order!\n");
								break;
							}
							System.out.println("\nTable No: "+tableId);
							System.out.println("Order No: "+orderId);
							System.out.println("------------Order Details-------------");
							
							for(int k=0;k<orderArray.size();k++)
							{
								System.out.printf("%-15s %15s", orderArray.get(k).getProdName()+":", orderArray.get(k).getQuantity());
								System.out.println("");
							
							}
							System.out.println("--------------------------------------");
							System.out.println("      Press u to update orders      ");
							System.out.println("        Press c to check out        \n");
							checkout=sc.next().charAt(0);
							if(checkout=='u'||checkout=='U')
							{
								char upOpt = 0;
								System.out.println("------Change quantity: Press 'q'------");
								System.out.println("-------Add new item: Press 'a'--------");
								System.out.println("-------Delete Order: Press 'd'--------");
								upOpt=sc.next().charAt(0);
								switch(upOpt)
								{
									case 'q':
									{
										int num=0;
										char o;
										for(int i=0;i<orderArray.size();i++)
										{
											System.out.println(orderArray.get(i).getProdName()+ " Enter quantity: ");
											num=sc.nextInt();
											orderArray.get(i).setQuantity(num);
										}
										System.out.println("Orders Updated Successfully\n");

										System.out.println("Table No. "+orderArray.get(0).getTableId());
										System.out.println("Orders No: "+orderArray.get(0).getOrderId());
										System.out.println("------------Order Details-------------");
										for(int i=0;i<orderArray.size();i++)
										{
											System.out.println(orderArray.get(i).getProdName() + " quantity: "+orderArray.get(i).getQuantity());
										}
										System.out.println("Submit Orders? Y/N");
										
										o=sc.next().charAt(0);
										switch(o)
										{
											case 'y'|'Y':
											{
												Orders create=new Orders();
												for(int i=0;i<orderArray.size();i++)
												{
													orderArray.get(i).setTimestamp(dtf.format(now));
												}
												create.createOrders(FILENAME,orderArray);
												//write to table txt file to make the table unavilable - persistent data
												BufferedWriter tFinalWriter = new BufferedWriter(new FileWriter("tables.txt"));
											
												HashMap<Integer, ArrayList<Integer>> a;
												for (String j : dateWiseTables.keySet()) 
												{
													a = dateWiseTables.get(j);
													
													for (int k : a.keySet()) 
													{
														for(int l=0;l<a.get(k).size();l++) 
														{
															tFinalWriter.write(j + "/" + a.get(k).get(l) + "/" + k + "/true");		
															tFinalWriter.newLine();
														}
													}			
												}
												
												tFinalWriter.close();
												tReader.close();
												break;
											}
											case'N'|'n':
											{
												System.out.println("Cancelling Order......\n");
												break;
											}
										}
										break;
									}
									case 'a':
									{
										int addOpt=0;
										int chooseOpt=0;
										char o;
										do 
										{
											System.out.println("--------Choose option--------");
											System.out.println("1: Ala Carte ");
											System.out.println("2: Promotion Set Package");
											System.out.println("3: Exit");
											chooseOpt=sc.nextInt();
											//ala Carte
											if(chooseOpt==1)
											{
												counter=0;
												m.printSortedMenuItem(alaCarte);
												System.out.println("---------Place Order---------");
												do
												{
													System.out.print("Enter Item ID: ");
													prodId=sc.next();
													m2= retrieveMenuItem(prodId,alaCarte);
													if(m2==null)
													{
														System.out.println("Invalid Item ID !! Please Enter Again!");
														System.out.println("Enter Item ID: ");
														prodId=sc.next();
														counter+=1;
														m2= retrieveMenuItem(prodId,alaCarte);
														if(m2==null)
														{
															System.out.println("Invalid Item ID!! Returning To Main Menu!!");
															break;
														}
														else
														{
															quantity=checkInvalidInt("Enter Quantity: ");
															if (quantity<=0)
																break;
															else
															{
																Orders order=new Orders(orderId, staffId, tableId, quantity, m2.getPrice(), m2.getName(),dtf.format(now));
																orderArray.add(order);
																counter+=1;
															}
															break;
														}
													}
													else
													{
														quantity=checkInvalidInt("Enter Quantity: ");
														if (quantity<=0)
															break;
														else
														{
															Orders order=new Orders(orderId, staffId, tableId, quantity, m2.getPrice(), m2.getName(),dtf.format(now));
															orderArray.add(order);
															counter+=1;
														}
														break;
													}
													
												}while(m2!=null |counter>=2);
												
											}
											else if(chooseOpt==2)
											{
												counter=0;
												if (promotion.size()==0)
												{
													System.out.println("There are no promotions!\n");
													break;
												}
												else
												{
													System.out.println("--------Promotion Set Menu--------");
													for(int i=0;i<promotion.size();i++)
													{
														
															System.out.println("Item Id: "+promotion.get(i).getItemId());
															System.out.println("Name: "+promotion.get(i).getName());
															System.out.println("Original Price: "+"$"+promotion.get(i).getPrice());
															System.out.println("Promo Price: "+"$"+promotion.get(i).getDiscountPrice());		
															System.out.println("");
													}
												
													//Place Orders
													System.out.println("---------Place Orders---------");
													do 
													{
														System.out.print("Enter Item ID: ");
														prodId=sc.next();
														check=retrievePromotion(prodId,promotion);
														if(check==null)
														{
															System.out.println("Invalid Item ID !! Please Enter Again!");
															System.out.println("Enter Item ID: ");
															prodId=sc.next();
															counter+=1;
															check=retrievePromotion(prodId,promotion);
															if(check==null)
															{
																System.out.println("Invalid Item ID!! Returning To Main Menu!!");
																break;
															}
															else
															{
																quantity=checkInvalidInt("Enter Quantity: ");
																if (quantity<=0)
																	break;
																else
																{
																	Orders order=new Orders(orderId, staffId, tableId, quantity,check.getDiscountPrice(), check.getName(),dtf.format(now));
																	orderArray.add(order);
																	counter+=1;
																}
																break;
															}
														}
														else 
														{
															quantity=checkInvalidInt("Enter Quantity: ");
															if (quantity<=0)
																break;
															else
															{
																Orders order=new Orders(orderId, staffId, tableId, quantity, check.getDiscountPrice(), check.getName(),dtf.format(now));
																orderArray.add(order);
															}
															break;
														}
													}while(check!=null|counter>=2);
												}
												
											}
											else if(chooseOpt==3)
											{
												System.out.println("Returing Back To Main Menu!");
												break;
											}
											
										}while(addOpt !=2);
										
	
										System.out.println("Order Updated Successfully!");

										System.out.println("Table No. "+orderArray.get(0).getTableId());
										System.out.println("Orders No: "+orderArray.get(0).getOrderId());
										System.out.println("------------Order Details-------------");
										for(int i=0;i<orderArray.size();i++)
										{
											System.out.println(orderArray.get(i).getProdName()+ " Quantity: "+ orderArray.get(i).getQuantity());
										}
										System.out.println("Submit Orders? Y/N");
										o=sc.next().charAt(0);
										switch(o)
										{
											case 'y'|'Y':
											{
												
												Orders create=new Orders();
												for(int i=0;i<orderArray.size();i++)
												{
													orderArray.get(i).setTimestamp(dtf.format(now));
												}
												create.createOrders(FILENAME,orderArray);
												
												//write to table txt file to make the table unavilable - persistent data
												BufferedWriter tFinalWriter = new BufferedWriter(new FileWriter("tables.txt"));
											
												HashMap<Integer, ArrayList<Integer>> a;
												for (String j : dateWiseTables.keySet()) {
													a = dateWiseTables.get(j);
													
													for (int k : a.keySet()) {
														for(int l=0;l<a.get(k).size();l++) {
															tFinalWriter.newLine();
															tFinalWriter.write(j + "/" + a.get(k).get(l) + "/" + k + "/true");					
														}
													}			
												}
												
												tFinalWriter.close();
												tReader.close();
												break;
											}
											case 'n'|'N':
											{
												System.out.println("Cancelling Order......\n");
												break;
											}
										}
										break;
									}
									case 'd':
									{
										char dOpt;
										for(int i=0;i<orderArray.size();i++)
										{
											System.out.println(orderArray.get(i).getProdName()+ " quantity: "+ orderArray.get(i).getQuantity());
											System.out.println("Delete this item? y/n");
											dOpt=sc.next().charAt(0);
											if(dOpt=='y')
											{
												if(orderArray.size()==1)
												{
													orderArray.clear();
												}
												else
												{
													orderArray.remove(i);
													System.out.println("Item deleted.\n");
												}
											}
										}
										System.out.println("Table No. "+orderArray.get(0).getTableId());
										System.out.println("Orders No: "+orderArray.get(0).getOrderId());
										System.out.println("------------Order Details-------------");
				
										for(int i=0;i<orderArray.size();i++)
										{
											System.out.println(orderArray.get(i).getProdName()+ " Quantity: "+ orderArray.get(i).getQuantity());
										}
										System.out.println("Submit Orders? Y/N");
										char o=sc.next().charAt(0);
										switch(o)
										{
											case 'y'|'Y':
											{
												Orders create=new Orders();
												for(int i=0;i<orderArray.size();i++)
												{
													orderArray.get(i).setTimestamp(dtf.format(now));
												}
												create.createOrders(FILENAME,orderArray);
												
												//write to table txt file to make the table unavilable - persistent data
												BufferedWriter tFinalWriter = new BufferedWriter(new FileWriter("tables.txt"));
											
												HashMap<Integer, ArrayList<Integer>> a;
												for (String j : dateWiseTables.keySet()) {
													a = dateWiseTables.get(j);
													
													for (int k : a.keySet()) {
														for(int l=0;l<a.get(k).size();l++) {
															tFinalWriter.newLine();
															tFinalWriter.write(j + "/" + a.get(k).get(l) + "/" + k + "/true");					
														}
													}			
												}
												
												tFinalWriter.close();
												tReader.close();
											}
											case 'n'|'N':
											{
												System.out.println("Cancelling Order......\n");
												break;
											}
										}
										break;
									}
								}
							
							}
							else if(checkout=='c'||checkout=='C')
							{
								//create orders
								
								Orders create=new Orders();
								for(int i=0;i<orderArray.size();i++)
								{
									orderArray.get(i).setTimestamp(dtf.format(now));
								}
								create.createOrders(FILENAME,orderArray);
								
								//write to table txt file to make the table unavilable - persistent data
								BufferedWriter tFinalWriter = new BufferedWriter(new FileWriter("tables.txt"));
							
								HashMap<Integer, ArrayList<Integer>> a;
								for (String j : dateWiseTables.keySet()) 
								{
									a = dateWiseTables.get(j);
									
									for (int k : a.keySet()) 
									{
										for(int l=0;l<a.get(k).size(); l++) 
										{
											tFinalWriter.write(j + "/" + a.get(k).get(l) + "/" + k + "/true");
											tFinalWriter.newLine();
										}
									}			
								}
								
								tFinalWriter.close();
								tReader.close();
								break;
							
							}
							else
							{
								System.out.println("Clearing shopping cart.\n");
								break;
							}
							break;
						}
						case 4: //view order
						{
							Orders read=new Orders();
							read.getOrderDetailsID();
							break;
						}
							
						case 5: // Add/remove item
						{
							DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							LocalDateTime now = LocalDateTime.now();
							ArrayList<Orders> updateOrder=new ArrayList<Orders>();
							int uID;
							MenuItem reM=new MenuItem();
							Promotion p=new Promotion();
							
							Orders read=new Orders();
							char aUpdate;
							uID=read.getOrderDetailsID();
							int counter=0;
							int i=0;
							//get the order details
							getOrder=read.RetrieveOrder(uID);
							updateOrder=read.retrieveOtherDetails(uID);
							
							System.out.println("Press 'd' to delete item ");
							System.out.println("Press 'a' to add item ");
							System.out.println("Press 'u' to update order quantity");
							aUpdate=sc.next().charAt(0);
							if(aUpdate=='d')
							{
								for(i=0;i<getOrder.size();i++)
								{
									System.out.println("Delete "+getOrder.get(i).getProdName()+"? y/n");
									char dOpt = sc.next().charAt(0);
									if(dOpt=='y')
									{
										if(getOrder.size()==1)
										{
											getOrder.clear();
											break;
										}
										else
										{
											getOrder.remove(i);
											i--;
											System.out.println("Item deleted\n");
										}
									}
								}
								for(i=0;i<getOrder.size();i++)
								{
									getOrder.get(i).setTimestamp(dtf.format(now));
								}
								
								updateOrder.addAll(getOrder);
								read.deleteOrders(updateOrder);
								System.out.println("Order Updated Successfully! ");
								System.out.println();
								read.readOrder(getOrder);
								System.out.println("Returing Back To Main Menu!");
								break;

							}//end of aUpdate ifloop
							
							else if(aUpdate=='a')
							{
								int addOpt=0;
								String prodId;
								int chooseOpt=0;
								int quantity=0;
								do
								{
									System.out.println("--------Choose option--------");
									System.out.println("1: Ala Carte ");
									System.out.println("2: Promotion Set Package");
									System.out.println("3: Exit");
									chooseOpt=sc.nextInt();
									//ala carte
									if(chooseOpt==1)
									{
										counter=0;
										reM.printSortedMenuItem(alaCarte);
										if (alaCarte.size()==0)
											break;
										else
										{
											System.out.println("---------Place Order---------");
											do
											{
												System.out.print("Enter Item ID: ");
												prodId=sc.next();
												reM= retrieveMenuItem(prodId,alaCarte);
												if(reM==null)
												{
													System.out.println("Invalid Item ID !! Please Enter Again!");
													System.out.println("Enter Item ID: ");
													prodId=sc.next();
													counter+=1;
													reM= retrieveMenuItem(prodId,alaCarte);
													if(reM==null)
													{
														System.out.println("Invalid Item ID!! Returning To Main Menu!!");
														break;
													}
													else
													{
														quantity=checkInvalidInt("Enter Quantity: ");
														if (quantity<=0)
															break;
														else
														{
															Orders order=new Orders(uID, getOrder.get(0).getStaffId(), getOrder.get(0).getTableId(), quantity, reM.getPrice(), reM.getName(),
																	dtf.format(now));
															getOrder.add(order);
															counter+=1;
														}
														break;
													}
												}
												else
												{
													quantity=checkInvalidInt("Enter Quantity: ");
													if (quantity<=0)
														break;
													else
													{
														Orders order=new Orders(uID, getOrder.get(0).getStaffId(), getOrder.get(0).getTableId(), quantity, reM.getPrice(), reM.getName(),
																dtf.format(now));
														getOrder.add(order);
														counter+=1;
													}
													break;
												}
											}while(reM!=null|counter>=2);
										}
									}//end of choice 1
									else if(chooseOpt==2)
									{
										counter=0;
										if (promotion.size() ==0)
										{
											System.out.println("There are no promotions!\n");
											break;
										}
										else
										{
											System.out.println("--------Promotion Set Menu--------");
											for(i=0;i<promotion.size();i++)
											{
												
													System.out.println("Item Id: "+promotion.get(i).getItemId());
													System.out.println("Name: "+promotion.get(i).getName());
													System.out.println("Original Price: "+"$"+promotion.get(i).getPrice());
													System.out.println("Promo Price: "+"$"+promotion.get(i).getDiscountPrice());		
													System.out.println("");
											}
											System.out.println("---------Place Order---------");
											do 
											{
												System.out.print("Enter Item ID: ");
												prodId=sc.next();
												p=retrievePromotion(prodId,promotion);
												if(p==null)
												{
													System.out.println("Invalid Item ID !! Please Enter Again!");
													System.out.println("Enter Item ID: ");
													prodId=sc.next();
													counter+=1;
													p=retrievePromotion(prodId,promotion);
													if(p==null)
													{
														System.out.println("Invalid Item ID!! Returning To Main Menu!!");
														break;
													}
													else
													{
														System.out.println("Enter Quantity:   ");
														quantity=sc.nextInt();
														Orders order=new Orders(uID, getOrder.get(0).getStaffId(), getOrder.get(0).getTableId(), quantity, p.getDiscountPrice(), p.getName(),
																dtf.format(now));
														getOrder.add(order);
														counter+=1;
														break;
													}
												}
												else 
												{
													System.out.println("Enter Quantity:   ");
													quantity=sc.nextInt();
													Orders order=new Orders(uID, getOrder.get(0).getStaffId(), getOrder.get(0).getTableId(), quantity, p.getDiscountPrice(), p.getName(),
															dtf.format(now));
													getOrder.add(order);
													break;
												}
											}while(p!=null|counter>=2);
										}
									}
									else if(chooseOpt==3)
									{
									
										for(i=0;i<getOrder.size();i++)
										{

											getOrder.get(i).setTimestamp(dtf.format(now));
										
										}
										updateOrder.addAll(getOrder);
										read.deleteOrders(updateOrder);
										System.out.println("Order Updated successfully!");
										System.out.println();
										read.readOrder(getOrder);
										System.out.println("Returing Back To Main Menu!");
										break;
									}
									 addOpt=checkInvalidInt("Press 1 to add more, press 2 to submit Order");
								}while(addOpt!=2);

								for(i=0;i<getOrder.size();i++)
								{
									getOrder.get(i).setTimestamp(dtf.format(now));
								}

								updateOrder.addAll(getOrder);
								read.deleteOrders(updateOrder);
								System.out.println("Order Updated successfully!");
								read.readOrder(getOrder);
								System.out.println("Returing Back To Main Menu!");
								break;
							}//end of aUpdate else if loop for adding item
							
							else if(aUpdate=='u')
							{
								int num;
								for(i=0;i<getOrder.size();i++)
								{
									System.out.println(getOrder.get(i).getProdName());
									System.out.println("Current Quantity:"+getOrder.get(i).getQuantity());
									num=checkInvalidInt("Enter Quantity:");
									if (num<=0)
										break;
									else
										getOrder.get(i).setQuantity(num);
								}
								System.out.println("Orders Updated Successfully");
								for(i=0;i<getOrder.size();i++)
								{
									System.out.println(getOrder.get(i).getProdName() + " Quantity: "+getOrder.get(i).getQuantity());
								}
								for(i=0;i<getOrder.size();i++)
								{
									getOrder.get(i).setTimestamp(dtf.format(now));
								}
								updateOrder.addAll(getOrder);
								read.deleteOrders(updateOrder);
								System.out.println("Order Updated successfully!");
								System.out.println();
								read.readOrder(getOrder);
								System.out.println("Returing Back To Main Menu!");
								break;
							}//end of Update else if loop
							else //enter wrong stuff
							{
								System.out.println("Wrong Input!! Returing Back To Main!!");
							}
							break;
						}//end of case 5
						case 6: 
							createRes();
							BufferedWriter rFinalWriter = new BufferedWriter(new FileWriter("res.txt"));
							
							ReservationDetail r;
							for (int i : reservations.keySet()) {
								r = reservations.get(i);
								rFinalWriter.write(i + "/" + r.getName() + "/" + r.getDate() + "/" + r.getArrivalTime() + "/" + r.getPax()  + "/" + r.getTableId());	
								rFinalWriter.newLine();
							}	
							rFinalWriter.close();
							rReader.close();
							//write to files - persistent data
							BufferedWriter tFinalWriter = new BufferedWriter(new FileWriter("tables.txt"));

							HashMap<Integer, ArrayList<Integer>> a;
							for (String j : dateWiseTables.keySet()) 
							{
								a = dateWiseTables.get(j);
								
								for (int k : a.keySet()) 
								{
									for(int l=0;l<a.get(k).size();l++) 
									{
										tFinalWriter.write(j + "/" + a.get(k).get(l) + "/" + k + "/true");		
										tFinalWriter.newLine();
									}
								}			
							}
							
							
							tFinalWriter.close();
							
							tReader.close();
							break;
						case 7: 
							checkRemoveRes();
							break;
						case 8: 
							checkTableAvailability();
							break;
							//invoice
						case 9:
							Invoice i = new Invoice();
							Orders o = new Orders();
							int tableID = checkInvalidInt("Enter Table ID: ");
							ArrayList<Orders> orderArr = o.RetrieveOrderbyTable(tableID);
							if (orderArr.size() == 0)
							{
								System.out.println("Invalid Table ID! \n");
								break;
							}
							else
							{
								
								i.printBill(orderArr);
								freeTable(tableID);
							}
							break;
							
						case 10:
							System.out.println("Print Sale Revenue Report");
							Staff s = new Staff();
							try 
							{
								s.validate(staffId);
							} catch (IOException e) 
							{
								System.out.println("File not found\n");
							}
							break;
						default:
							break;
							
					}// end of switch case loop
			
				}while(option>0 && option<=10 && option!=11);


				
				System.out.println("System logging out......");
				System.out.println("--------------------------------------------------------\n");
			
		}while(staffName!=null);
	}
	public static String add30Mins(String time) throws ParseException
	{
		final DateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(time));
		cal.add(Calendar.MINUTE, 30);
		return sdf.format(cal.getTime());
	}
	
	public static void freeTable(int tableId) 
	{
		HashMap<Integer, ArrayList<Integer>> a = dateWiseTables.get(getCurrentDateTime());
		for (int i : a.keySet()) {
			ArrayList<Integer> temp;
			temp = a.get(i);
			
			for(int j=0; j<temp.size(); j++) 
			{
				if(temp.get(j) == tableId) 
				{
					temp.remove(j);
					break;
				}
			}				
		}
	}
	
	public static void checkTableAvailability() 
	{
		ArrayList<Integer> nums = new ArrayList<Integer>();
		for(int i=0; i<=30; i++) nums.add(i+1);
		HashMap<Integer, ArrayList<Integer>> a;
		a = dateWiseTables.get(getCurrentDateTime());
		if(a != null) {
			for (int k : a.keySet()) {
				for(int l=0;l<a.get(k).size();l++) {
					nums.remove(a.get(k).get(l));
				}
			}			
		}
	
		System.out.println("");
		System.out.println("Tables available:");
		int [] options = {2, 4, 8, 10};
		boolean flag = true;
		int optionNo=getTableRange(nums.get(0));
		
		for(int k=0; k<nums.size()-1;k++) {
			if(flag) {
				System.out.println("");
				System.out.print(options[optionNo] + " seaters: ");
				flag = false;
			}
			if(getTableRange(nums.get(k+1)) != optionNo) {
				flag = true;
				optionNo++;
			}
			
			System.out.print(nums.get(k) + ", ");
		}
		System.out.println("");
		System.out.println("");
	}
	
	public static int getTableRange(int tID) 
	{
		if(tID>0 && tID<=10) return 0;
		if(tID>10 && tID<=20) return 1;
		if(tID>20 && tID<=25) return 2;
		if(tID>25 && tID<=30) return 3;
		return 0;
	}
	
	public static int createTable(int capacity, String key) {
		int tableID =genNextID(capacity, key);		
		return tableID;
	}
	
	public static boolean contains(String key, int cap, int num) {
		ArrayList<Integer> temp = dateWiseTables.get(key).get(cap);
		if(temp != null) {
			for(int i=0;i<temp.size(); i++) {
				if(temp.get(i) == num) return true;
			}	
		}
		
		return false;
	}
	
	public static int roundUp(int num) {
		if(num<=2) return 2;
		if(num<=4) return 4;
		if(num>=4 &&num<=8) return 8;
		if(num>=8 && num<=10) return 10;
		
		return 0;
	}
	
	public static int genNextID(int cap, String key) {
		
		if (dateWiseTables.get(key) == null) 
		{
			dateWiseTables.put(key, new HashMap<Integer, ArrayList<Integer>>());
		}
		ArrayList<Integer> temp1 = dateWiseTables.get(key).get(cap);
		int l = 0;
		int u = 0;
		if (cap == 2) 
		{
			l = 1;
			u = 10;
		} else if (cap == 4) 
		{
			l = 11;
			u = 20;
		} else if (cap == 8) 
		{
			l = 21;
			u = 25;
		} else if (cap == 10) 
		{
			l = 26;
			u = 30;
		}

		for (int i = l; i <= u; i++) 
		{
			if (!contains(key, cap, i)) 
			{
				if (temp1 == null) 
				{
					dateWiseTables.get(key).put(cap, new ArrayList<Integer>());
				}
				dateWiseTables.get(key).get(cap).add(i);
				return i;
			}
		}
		return -1;
	}
	
	public static ReservationDetail removeRes(int contact) 
	{
		ReservationDetail ret = reservations.remove(contact);
		return ret;	
	}

	public static void createRes() throws ParseException, IOException 
	{
		final DateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, 1);
		Date nextMonth = cal.getTime();
		
		System.out.print("Enter name: ");
		String n = in.next();
		if (n.length() < 1) {
			System.out.println("");
			System.out.print("Invalid entry. Try again:");
			n = in.next();
		}
		System.out.println("");
		System.out.print("Enter contact number: ");
		int c = in.nextInt();

		if (Integer.toString(c).length() != 8) {
			System.out.println("");
			System.out.print("Invalid entry. Try again:");
			c = in.nextInt();
		}

		if (Integer.toString(c).length() == 8) {
			System.out.println("");
			System.out.print("Enter date: (dd-mm-yy) ");
			String d = in.next();
			
			if (!((sdf.parse(sdf.format(nextMonth)).compareTo(sdf.parse(d)) > 0) && (sdf.parse(d).compareTo(sdf.parse(sdf.format(new Date()))) > 0))) {
				System.out.println("");
				System.out.print("Invalid date, enter date within 1 month from today: (dd-mm-yy) ");
				d = in.next();	
			}
			
			if ((sdf.parse(sdf.format(nextMonth)).compareTo(sdf.parse(d)) > 0) && (sdf.parse(d).compareTo(sdf.parse(sdf.format(new Date()))) > 0)) {
				
				System.out.println("");
				System.out.print("Enter AM/PM: ");
				String slot = in.next().toUpperCase();

				System.out.println(
						"Enter arrival time:" + ((slot.equalsIgnoreCase("AM")) ? "11:00-14:00" : "18:00-21:00"));
				String a = in.next();
				int time = Integer.parseInt(a.substring(0, 2) + a.substring(3));

				int startTime = slot.equalsIgnoreCase("AM") ? 1100 : 1800;
				int endTime = slot.equalsIgnoreCase("AM") ? 1400 : 2100;

				if (!(time >= startTime && time <= endTime)) {
					System.out.println("");
					System.out.print("Invalid entry. Try again:");
					a = in.next();
					time = Integer.parseInt(a.substring(0, 2) + a.substring(3));

					startTime = slot.equalsIgnoreCase("AM") ? 1100 : 1800;
					endTime = slot.equalsIgnoreCase("AM") ? 1400 : 2100;

				}

				if (time >= startTime && time <= endTime) {
					System.out.println("");
					System.out.print("Enter number of persons: ");
					int p = in.nextInt();
					System.out.println("");

					if (p > 0 && p <= 10) {
						boolean keepTrying = true;
						int pax = p;
						while (keepTrying) {
							int tableID = createTable(roundUp(p), d + slot);
							if (tableID != -1) {
								reservations.put(c, new ReservationDetail(n, d + slot, a, pax, tableID));
								keepTrying = false;
								System.out.println("");
								System.out.println("Your reservation has been placed! Table number: " + tableID);
							} else {
								if (roundUp(p) == 10) {
									keepTrying = false;
									System.out.println(
											"Sorry no tables available on the day you selected. Please try another time!");
								}
								p = roundUp(p) + 1;
							}
						}
					}

					else
						System.out.println("Please enter a valid number between 1-10");
				} else
					System.out.println("Invalid time!");
			} 
			else 
				System.out.println("Date is beyond 1 month. Can only reserve for a table within one month.");
		}
		else
			System.out.println("Please enter a valid contact number!");
	}
	
	public static void checkRemoveRes() {
		System.out.print("Enter contact number: ");
		int c = in.nextInt();
		ReservationDetail r = reservations.get(c);
		if(r != null) {
			System.out.println("Your reservation:");
			System.out.println("Name: "+ r.getName() + ", Date: " + r.getDate() + ", Arrival Time: " + r.getArrivalTime() + ", #persons: " + r.getPax() + ", tableID: " + r.getTableId());
			System.out.println("");
			System.out.println("Would you like to delete your reservation? Y/N");
			String shouldRemove = in.next().toUpperCase();
			
			if(shouldRemove.charAt(0) == 'Y') 
			{
				removeReservation(c);
			}
		}
		else {
			System.out.println("You do not have a reservation");
			System.out.println("");
		}
	}
	public static void removeReservation(int contact) throws ConcurrentModificationException {
		ReservationDetail ret = reservations.remove(contact);
//		System.out.println(contact);
//		System.out.println(dateWiseTables);
		
		if(dateWiseTables.get(ret.getDate()) !=null) {
			ArrayList<Integer> a = dateWiseTables.get(ret.getDate()).get(roundUp(ret.getPax())); 
			for(int i=0; i< a.size(); i++) {
				if(a.get(i) == ret.getTableId()) {
					dateWiseTables.get(ret.getDate()).get(roundUp(ret.getPax())).remove(i);
				}
			}	
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
	
	public static void removeEmptyLines(File fileName1, File fileName2)
	{
		Scanner file;
        PrintWriter writer;
        try {

            file = new Scanner(fileName1);
            writer = new PrintWriter(fileName2);

            while (file.hasNext()) 
            {
                String line = file.nextLine();
                if (!line.isEmpty()) 
                {
                    writer.write(line);
                    writer.write("\n");
                }
            }

            file.close();
            writer.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        fileName1.delete();
        fileName2.renameTo(fileName1);
	}
	public static void appendStrToFile(File fileName, String str) 
	{ 
		try 
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true)); 
			out.write(str); 
			out.close(); 
		} 
		catch (IOException e) 
		{ 
			System.out.println("exception occoured" + e); 
		} 
	}
	public static MenuItem retrieveMenuItem(String id, ArrayList<MenuItem> m)
	{
		for (int i=0; i<m.size(); i++)
		{
			if (id.equals(m.get(i).getItemId()))
				return m.get(i);
		}
		return null;
	}
	
	public static Promotion retrievePromotion(String id, ArrayList<Promotion> p)
	{
		for (int i=0; i<p.size(); i++)
		{
			if (id.equalsIgnoreCase(p.get(i).getItemId()))
				return p.get(i);
		}
		return null;
	}
	
	
	public static int checkInvalidInt(String s)
	{
		Scanner sc = new Scanner(System.in);
		int fail = 0;
		int option=-1;
		boolean ok = false;
		while (!ok && fail<2)
		{
		    try 
		    {
		    	System.out.print(s);
		    	option = sc.nextInt();
		        ok = true;
		        if (option<=0)
		        {
		        	System.out.println("Invalid input!\n");
		        	fail++;
		        	break;
		        }
		    }
		    catch (InputMismatchException e) 
		    {
		    	sc.next(); // clears the buffer
		    	fail++;
		    	System.out.println("Invalid input!\n");
		    	if (fail>=2)
		    		return -1;
		    }
		}
		return option;
	}
	
	public static double checkInvalidDouble(String s)
	{
		Scanner sc = new Scanner(System.in);
		int fail = 0;
		double option=-1;
		boolean ok = false;
		while (!ok && fail<2)
		{
		    try 
		    {
		    	System.out.print(s);
		    	option = sc.nextDouble();
		        ok = true;
		    }
		    catch (InputMismatchException e) 
		    {
		    	sc.next(); // clears the buffer
		    	fail++;
		    	System.out.println("Invalid input!\n");
		    	if (fail>=2)
		    		return -1;
		    }
		}
		return option;
	}
	
	public static double checkNegativeDouble(double value)
	{
		Scanner sc = new Scanner(System.in);
		double option=0;
		if (value<=0)
		{
			System.out.println("Invalid input! Please enter a positive value.\n");
			System.out.print("Enter price: ");
			option = sc.nextDouble();
			if (option<0)
			{
				System.out.println("Invalid input!\n");
				return -1;
			}
			else
				return option;
		}
		else
			return value;
	}
}
