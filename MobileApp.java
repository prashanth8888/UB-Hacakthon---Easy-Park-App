
package ubhackathon;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MobileApp {
	//in server
	public Map<String,LotArea> parkingLotsMap ;
	public Map<String,ParkingLot> users;
	public static String selectedLotArea = "";
	public static int selectedParkingLot = 0;
	MobileApp()
	{
		CentralServer.initialize();
	}
	public static void main(String[] args) throws InterruptedException 
	{
		MobileApp ma = new MobileApp();
		String userId="James Anderson";
		ma.getParkingLot();
		ma.updateDataFromServer();
		//System.out.println(selectedLotArea);
		LotArea desiredLotArea = ma.parkingLotsMap.get(selectedLotArea);
		ParkingLot desiredParkingLot = desiredLotArea.getParkingLot(selectedParkingLot);
		if(desiredParkingLot == null)
		{
			// prompt user that the slot is booked already ?????
		}
		if(desiredParkingLot.isBooked()) // shown in RED
		{
			//if(true) ==> tell the user in the GUI that the slot is booked and 
			// prompt user to select another available slot.
		}else
		{
			//proceed with the confirmation page
			try {
				desiredParkingLot.bookSlot(userId);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // this will proceed to confirmation page
			ma.users.put(userId, desiredParkingLot);
			// the desiredParkingLot instance will start the timer eg: 10 minutes after confirmation
		}
		Thread.sleep(10000);
		reachedLot(desiredLotArea,userId,selectedParkingLot,selectedLotArea);
//		System.out.println("The slot is booked by "+desiredParkingLot.userId);
//		System.out.println(CentralServer.users.get(userId).LotAreaName);
//		System.out.println(CentralServer.users.get(userId).isBooked());
//		System.out.println(selectedParkingLot);
	
	}
	
	public static void reachedLot(LotArea desiredLotArea, String userId, int selectedParkingLot, String selectedLotArea)throws InterruptedException 
	{
		if(CentralServer.users.get(userId) != null){
			desiredLotArea.barCodeIn1.readBarCode(userId, selectedParkingLot);
			Thread.sleep(1000);
			desiredLotArea.barCodeIn2.readBarCode(userId, selectedParkingLot);
			//System.out.println();
			System.out.println("--------------------------------------------------------------------------------------------------------");
			System.out.println(userId + " has parked at " + selectedLotArea + " Parking Slot " + selectedParkingLot + " is now occupied");
			Thread.sleep(6000);
			//System.out.println(userId);
			//System.out.println(desiredLotArea.barCodeOut1);
			System.out.println("--------------------------------------------------------------------------------------------------------");
			System.out.println("User " + userId + " wants to check out of the parking lot " + "Do you Authorize? YES/NO");
			Scanner in = new Scanner(System.in);
			String input = in.next();
			desiredLotArea.barCodeOut1.readBarCode(userId, selectedParkingLot);
			System.out.println("---------------------------------------------------------------------------------------------------------");
			System.out.println("User "+ userId + " Checking out of the lot...");
			Thread.sleep(3000);
			String temp_user_id = userId;
			desiredLotArea.barCodeOut2.readBarCode(userId, selectedParkingLot);
			System.out.println("---------------------------------------------------------------------------------------------------------");
			System.out.println("Notification: " + temp_user_id + " has exited the parking lot!" + "Parking Slot " + selectedParkingLot + " at " + selectedLotArea + " is free now!");
			System.out.println("---------------------------------------------------------------------------------------------------------");
			//System.out.println(CentralServer.getUsers().get(userId) == null ?"null":"userid is still available");
			//System.out.println(CentralServer.getUsers().get(userId));
		
		}
		}
	
	//this userId is got from the actual 
//	public static void authenticateArrived(String userId)
//	{
//		ParkingLot parkingLot = users.get(userId);
//		//parkingLot.receiveAuth(userId);
//	}
	
	public static void getParkingLot(){
		Scanner in = new Scanner(System.in);
		String[] parking_lots = {"GovernorsCLot","GovernorsDLot","GovernorsELot","GovernorsFLot"};
		//System.out.print("GovernorsCLot/GovernorsDLot/GovernorsELot/GovernorsFLot");
		System.out.println("-------------------------");
		System.out.println("Welcome to Easy Parking!");
		System.out.println("-------------------------");
		System.out.println("Parking Slots Available");
		System.out.println("------------------------------------------------------");
		for(int i=0; i<4; i++){
			if(i < 4){
				if(i % 2 != 0){
					System.out.print("         " +"|");
					System.out.print("|" + "        ");
					System.out.print(parking_lots[i]);
				}
				else{
					System.out.print("|");
					System.out.print("|");
					System.out.print(parking_lots[i]);
				}
				
			}
			if(i == 1){
				System.out.println(" ");
				System.out.println("------------------------------------------------------");
			}
		}
		System.out.println("");
		System.out.println("------------------------------------------------------");
		System.out.println("Enter the Parking Space to be booked");
		String input = in.next();
		if(input.equalsIgnoreCase("GovernorsCLot") || input.equalsIgnoreCase("GovernorsDLot") || input.equalsIgnoreCase("GovernorsELot") || input.equalsIgnoreCase("GovernorsFLot")){
		selectedLotArea = input;
		LotArea a1 = CentralServer.parkingLotsMap.get(selectedLotArea);
		a1.getParkingLotsList();
		for(int i = 0; i < a1.size;i++)
		{
			System.out.print(i+1 + "\t");
			if((i+1) % a1.col == 0)
			{
				System.out.println();
				System.out.println();
			}
		}
		System.out.println("Enter the Slot to be Booked from the list shown above");
		selectedParkingLot = in.nextInt();
		
		//System.out.println("Selected Lot Area " + selectedLotArea);
		}
		
		else{
			System.out.println("Enter the Correct ParkingLot");
			MobileApp.getParkingLot();
		}
		
	}
	public void updateDataFromServer()
	{
		parkingLotsMap = CentralServer.getParkingLotsMap();
		users = CentralServer.getUsers();
	}

}
