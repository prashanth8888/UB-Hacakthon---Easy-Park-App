package ubhackathon;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the central server containing the actual parking lot data, that is the actual HashMap.
 * @author Anandaraju CS
 *
 */
public class CentralServer {
	public static Map<String,LotArea> parkingLotsMap = new HashMap<String,LotArea>();
	public static Map<String,ParkingLot> users = new HashMap<String,ParkingLot>();
	public CentralServer()
	{
		initialize();
	}
	public static void initialize()
	{
		// initialize all the ParkingLot objects, LotArea and BarCodeReader Objects
		
		parkingLotsMap.put("GovernorsCLot", new LotArea("GovernorsCLot",5,10));
		parkingLotsMap.put("GovernorsDLot", new LotArea("GovernorsDLot",3,6));
		parkingLotsMap.put("GovernorsELot", new LotArea("GovernorsELot",5,6));
		parkingLotsMap.put("GovernorsFLot", new LotArea("GovernorsFLot",7,10));
	}
	
	public static Map<String,LotArea> getParkingLotsMap()
	{
		return parkingLotsMap;
	}
	public static Map<String,ParkingLot> getUsers()
	{
		return users;
	}
	
	public static ParkingLot getParkingLot(String userId)
	{
		return users.get(userId);
	}
}
