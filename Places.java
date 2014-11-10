import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import PlaceData.PlaceDataProto.*;

// this is the class with remote methods

public class Places 
	extends UnicastRemoteObject
	implements PlacesInterface {

	public Places() throws RemoteException {
		System.out.println("New instance of Places Service Object created");
	}

	public RemoteResult lookup(String city, String state) throws RemoteException {
		PlaceList placelist = null;

		// Read the existing Places list
		try {
			placelist = PlaceList.parseFrom(new FileInputStream("places-proto.bin"));
		} catch (IOException e) {
			System.err.println("Error: Places binary file not found.");
			return new RemoteResult(RemoteErrorCode.FILENOTFOUND);
		}

		city = city.toLowerCase();
		for (Place place : placelist.getPlaceList()) {
			if (place.getState().equalsIgnoreCase(state) && place.getName().toLowerCase().startsWith(city))
				return new RemoteResult(place);
		}

		return new RemoteResult(RemoteErrorCode.VALUENOTFOUND);
	}
}
