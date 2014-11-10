import java.rmi.Remote;
import java.rmi.RemoteException;

import PlaceData.PlaceDataProto.*;

public interface PlacesInterface extends Remote {
	public RemoteResult lookup(String city, String state) throws RemoteException;
}
