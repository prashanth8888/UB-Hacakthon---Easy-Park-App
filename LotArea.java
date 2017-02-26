package ubhackathon;

import java.util.ArrayList;
import java.util.List;

public class LotArea {
	public String name = "";
	public int size = 0;
	public int row = 0;
	public int col = 0;
	public int selectedParkingLot = 0;
	public List<ParkingLot> parkingLots= new ArrayList<ParkingLot>();
	public BarCodeReader barCodeIn1 = null;
	public BarCodeReader barCodeIn2 = null;
	public BarCodeReader barCodeOut1 = null;
	public BarCodeReader barCodeOut2 = null;
	public LotArea(String name, int row, int col)
	{
		this.row=row;
		this.col=col;
		this.size=row*col;
		this.name=name;
		for(int i=0;i<size;i++)
		{
			parkingLots.add(new ParkingLot(name));
		}
		barCodeIn1= new BarCodeReader(BarCodeReader.Version.IN1,this);
		barCodeIn2= new BarCodeReader(BarCodeReader.Version.IN2,this);
		barCodeOut1= new BarCodeReader(BarCodeReader.Version.OUT1,this);
		barCodeOut2= new BarCodeReader(BarCodeReader.Version.OUT2,this);
	}
	
	public ParkingLot getParkingLot(int parkingLotNumber)
	{
		if(parkingLotNumber > size )
		{
			return null; // wrongSlotSelected
		}
		return parkingLots.get(parkingLotNumber-1);
	}
	
	public List<ParkingLot> getParkingLotsList()
	{
		return parkingLots;
	}

}
