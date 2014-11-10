import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Comparator;

import PlaceData.PlaceDataProto.*;
import AirportData.AirportDataProto.*;

// this is the class with remote methods

public class Airports
	extends UnicastRemoteObject
	implements AirportsInterface {

	public Airports() throws RemoteException {
		System.out.println("New instance of Airports Service Object created");
	}

	private double calcDistance(double lat1, double lon1, double lat2, double lon2) {
		lat1 = Math.toRadians(lat1);
		lon1= Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);
		double res = Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1));
		res = 60*Math.toDegrees(res)*1.1507794;
		return res;
	}

	public RemoteResult lookup(double lat, double lon) throws RemoteException {
		AirportList airportlist = null;


		// Read the existing Places list
		try {
			airportlist = AirportList.parseFrom(new FileInputStream("airports-proto.bin"));
		} catch (IOException e) {
			System.err.println("Error: Places binary file not found.");
			return new RemoteResult(RemoteErrorCode.FILENOTFOUND);
		}

		PriorityQueue<AP> minHeap = new PriorityQueue<AP>(1100, new MyComparator());

		for (Airport airport : airportlist.getAirportList()) {
			AP ap = new AP(airport);
			ap.dist = calcDistance(lat, lon, airport.getLat(), airport.getLon());
			minHeap.add(ap);
		}

		AP[] response = new AP[5];
		int i = 0;
		for (i = 0; i < 5; i++) {
			response[i] = minHeap.poll();
			if (response[i] == null)
				break;
		}
		if (i == 0)
			return new RemoteResult(RemoteErrorCode.VALUENOTFOUND);

		return new RemoteResult(response);
	}
}


class MyComparator implements Comparator<AP>
{
	public int compare( AP x, AP y )
	{
		double res = x.dist - y.dist;
		if (res < 0)
			return -1;
		else if (res > 0)
			return 1;
		else 
			return 0;
	}
}
