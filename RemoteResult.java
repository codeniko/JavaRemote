import PlaceData.PlaceDataProto.*;
import AirportData.AirportDataProto.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.io.Serializable;

public class RemoteResult 
	implements Serializable
	{
	private boolean placeFlag;
	private Place place;
	private AP[] airports;
	private RemoteErrorCode errno;

	public RemoteResult(Place p) throws RemoteException {
		this.place = p;
		this.errno = RemoteErrorCode.SUCCESS;
		this.placeFlag = true;
	}

	public RemoteResult(AP[] airports) throws RemoteException {
		this.airports = airports;
		this.errno = RemoteErrorCode.SUCCESS;
		this.placeFlag = false;
	}

	public RemoteResult(RemoteErrorCode errno) throws RemoteException {
		this.place = null;
		this.errno = errno;
	}

	public Place getPlace() throws RemoteException {
		return place;
	}

	public AP[] getAirports() throws RemoteException {
		return airports;
	}

	public boolean isPlace() throws RemoteException {
		return placeFlag;
	}

	public String getError() throws RemoteException {
		switch(errno) {
			case SUCCESS: return "SUCCESS";
			case FILENOTFOUND: return "Proto binary file not found.";
			case VALUENOTFOUND: return "Lookup didn't find a match.";
		}

		return "";
	}
}

enum RemoteErrorCode {
	SUCCESS,
	FILENOTFOUND,
	VALUENOTFOUND
}
