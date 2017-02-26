/*Author: Prashanth Seralathan	
 *Place : Buffalo, NY
*/
package ubhackathon;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLot {
	//Input is obtained from the user.
	/* 
	 * This class basically is used for booking the slot for a particular user
	 * It maintains multiple variables and it's status
	 * The user needs to notify after booking the slot within the specified time.
	 * The user needs to notify the user within specified time
	 * Else the status is unlocked and some other user gets to book it again.
	 */
	ParkingLot Slot_ID;
	public String LotAreaName = "";
	public boolean booked; 
	public boolean arrived;
	public boolean occupied;
	public int start_time = 0;
	public static int time_out_paramater = 45;
	public String userId = "";
	private Scanner in;
	UserArrivalThread user_arrival;
	public static Map<String,Boolean> occupied_notify = new ConcurrentHashMap<String,Boolean>();

	public ParkingLot(String LotAreaName){
		this.LotAreaName = LotAreaName;
	}

	public void bookSlot(String user_id) throws InterruptedException{
		this.userId = user_id;
		user_arrival = new UserArrivalThread();
		//Now let's get into the core of booking the slot
		outer:
			if(booked == false){
				System.out.print("If the slot is booked/ You have a 15 minutes window time");
				System.out.println(" to get to the parking slot and Authenticate with the barcode");
				System.out.print("Are you sure you want to Book this slot? YES/NO");
				in = new Scanner(System.in);
				String user_choice = in.next();


				if(user_choice.equalsIgnoreCase("NO")){
					System.out.println("You have opted out of the booking");
					break outer;
				}
				else if(user_choice.equalsIgnoreCase("YES")){
					/*Sets the Booked status to True after getting the input from user
					 * Hence we need to periodically check if we get an Authentication from the user
					 */
					booked = true;
					occupied_notify.put(userId, false);
					user_arrival.setUserId(userId);
					user_arrival.start();
				}
				else{
					System.out.println("Sorry the Input is Invalid, Enter Either Yes or No");
				}
			}
	}


	public void timeout_booking(){
		this.reset();
		System.out.println("You failed to reach the destination within 15 minutes, Your slot has been revoked,Sorry!");
	}

	public void reset(){
		this.booked = false;
		this.occupied = false;
		this.arrived = false;
		//this.userId = "";
		this.start_time = 0;
	}

	/*
	 * The Set Bar Code status involves 4-point authentication for letting the Vehicle in and out.
	 * The flow between the states can be tracked as follows,
	 * Arrived -> Occupied (The User gets into the parking slot and parks the Vehicle)
	 * Occupied - Set it to false when the user passes outgate1
	 * Arrived, Booked and all other states of belonging to the user needs to be initialized since the user has gone vacated the parking slot
	 */


	public boolean setBarCodeStatus(String user_id,BarCodeReader.Version ob){
		boolean status = false;
		if(user_id == this.userId && ob == BarCodeReader.Version.IN1){
			this.arrived = true;
			status = true;
		} 
		else if(user_id == this.userId && ob == BarCodeReader.Version.IN2 && this.arrived == true){
			occupied_notify.replace(userId, true);
			user_arrival.setUserId(userId);
			this.occupied = true;
			status = true;
		}
		else if(user_id == this.userId && ob == BarCodeReader.Version.OUT1 && this.arrived == true && this.occupied == true){
			this.occupied = false;
			status = true;
		}
		else if(user_id == this.userId && ob == BarCodeReader.Version.OUT2 && this.arrived == true && this.occupied == false){
			this.arrived = false;
			CentralServer.getParkingLot(userId).reset();
			CentralServer.getUsers().remove(userId);
			status = true;
		}
		else{
			System.out.println("The Authentication Failed! Contact Admin - 716-545-4416" );
			status = false;
		}

		return status;
	}

	public void setBarCodeIn2Status(Object ob){
		/*if(ob.user_id == this.user_id && this.arrived == true){
		 * this.occupied = true;
		 * }
		 * else{
		 * System.out.println("The Authentication Failed at Incoming Bar code server 2");
		 * */
	}
	public void setBarCodeOut1Status(Object ob){
		/*if(ob.user_id == this.user_id && arrived == true && occupied == true){
		 * this.occupied = false;
		 * }
		 * else{
		 * System.out.println("The Authentication Failed at Incoming Bar code Server 3");
		 * }
		 */
	}
	public void setBarCodeOut2Status(Object ob){
		/*if(ob.user_id == this.user_id && arrived == true && occupied == false){
		 * arrived = false;
		 * Reset the Entire Variables when the user moves out of the parking slot
		 * System.out.println("See you again, If you like our System, Thumps up!");
		 * this.reset();
		 * }
		 * else{
		 * System.out.println("The Authentication Failed at Incoming Bar code Server 4");
		 * }
		 */
	}

	public void recievedAuth(Object ob){
		/*if(ob.user_id == this.user_id && booked == false){
		 * 
         this.booked = true; 
    }
    else{
        System.out.println("Authentication Failure");
        this.booked = false;
    }*/

	}

	public boolean isBooked(){
		if(booked == true) return true;
		else return false;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

class UserArrivalThread extends Thread
{
	int startTime = 0;
	int endTime = 0;
	String userId = " ";
	boolean timeout = true;
	int time=0;
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public boolean getTime(){
		time = LocalDateTime.now().getSecond()-startTime;//< 0 ? 60-time:time;
		if(time < 0){
			time = 60-time;
		}
		//System.out.println("Time difference " + time);
		if(time < ParkingLot.time_out_paramater) 
		{
			return true;
		}
		else 
		{
			timeout = false;
			return false;
		}
	}
	@Override
	public void run(){
		startTime = LocalDateTime.now().getSecond();
		//time = (LocalDateTime.now().getSecond()-startTime) < 0 ? 60-time:time;
		//timeout=((time=LocalDateTime.now().getSecond()-startTime < 0 ? 60-time:time) > 2);
		//System.out.println(timeout);
		while(!ParkingLot.occupied_notify.get(userId) && this.getTime())
		{
			//			System.out.println("NOTIFY : "+!ParkingLot.occupied_notify.get(userId));
			//System.out.println("TIMEOUT : "+timeout);
		}
		if(!timeout)
		{
			CentralServer.getParkingLot(userId).reset();
			CentralServer.getUsers().remove(userId);
			System.out.println("You failed to reach the destination within 15 minutes - Your booking has been revoked based on time constraints");
			System.out.println("Sorry for the Inconvenience caused! Make Another Booking before checking in");
			System.out.println("Exiting Application");
			System.out.println("-------------------");
		}
	}
}