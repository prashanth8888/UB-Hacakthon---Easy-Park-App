package ubhackathon;

public class BarCodeReader {
	enum Version{
		IN1, IN2, OUT1, OUT2
	}
	public Version version = null;
	public LotArea lotArea = null;
	BarCodeReader(Version v, LotArea la)
	{
		this.lotArea = la;
		this.version = v;
	}
	/*
	 * reading the barcode from the mobile app and decode the userId and parkingLotNumber
	 */
	public void readBarCode(String userId, int parkingLotNumber)
	{
		// get the inputs from the mobile app for the user while reaching the correct lot area.
		ParkingLot pl = CentralServer.getParkingLot(userId);
		//get the data from the server for the list of parking lots
		boolean status = pl.setBarCodeStatus(userId, version);
		if(status == true)
		{
			//call open gate method
		}
		
//		if(version == Version.IN1)
//		{
//			//get the data from the server for the list of parking lots
//			pl.setBarCodeStatus(userId, version);
//		}else if(version == Version.IN2)
//		{
//			pl.setBarCodeStatus(userId);
//		}else if(version == Version.OUT1)
//		{
//			pl.setBarCodeStatus(userId);
//		}else if(version == Version.OUT2)
//		{
//			pl.setBarCodeStatus(userId);
//		}
	}
}
