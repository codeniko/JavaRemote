import java.rmi.Remote;
import java.rmi.RemoteException;

import PlaceData.PlaceDataProto.*;

public interface AirportsInterface extends Remote {
	public RemoteResult lookup(double lat, double lon) throws RemoteException;
}
