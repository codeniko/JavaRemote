import java.rmi.Naming;
import java.rmi.RemoteException;

import PlaceData.PlaceDataProto.*;
import AirportData.AirportDataProto.*;

public class Client {
	public static void main(String args[]) {
		try {
			CLArgs clargs = new CLArgs(new Option("h", false), new Option("p", false));
			CLResult clres;
			if ((clres = clargs.parse(args)) != CLResult.SUCCESS || clargs.argc() != 2) {
				if (clres == CLResult.INVALIDARG || clargs.argc() != 2)
					System.err.println("Error: Program invoked with an invalid argument.");
				System.err.println("Usage: java Client [-h <rmiregistryserver>] [-p <port>] <city> <state>");
				System.exit(1);
			}


			String portStr;
			int port;
			if ((portStr = clargs.getOpt("p")) == null)
				port = 1099;
			else 
				port = Integer.parseInt(portStr);
			String rmiserver = clargs.getOpt("h");
			if (rmiserver == null)
				rmiserver = "localhost";

			String url = "//" + rmiserver + ":" + port + "/Places";
			System.out.println("looking up " + url);

			System.out.println(clargs.getArg(1));
			System.out.println(clargs.getArg(2));

			PlacesInterface places = (PlacesInterface)Naming.lookup(url);

			RemoteResult res = places.lookup(clargs.getArg(1), clargs.getArg(2));
			if (res.getPlace() == null) {
				System.err.println("Error: "+ res.getError());
				System.exit(1);
			}

			System.out.println("State="+res.getPlace().getState() + " name="+ res.getPlace().getName()+" lat="+res.getPlace().getLat()+ " lon="+res.getPlace().getLon());

			url = "//" + rmiserver + ":" + port + "/Airports";
			System.out.println("looking up " + url);
			AirportsInterface airports = (AirportsInterface)Naming.lookup(url);
			RemoteResult res2 = airports.lookup(res.getPlace().getLat(), res.getPlace().getLon());
			if (res2.getAirports() == null) {
				System.err.println("Error: "+ res2.getError());
				System.exit(1);
			}
			for (int i = 0; i < res2.getAirports().length; i++) {
				AP ap = (res2.getAirports())[i];
				if (ap == null)
					break;
				System.out.println("code="+ap.airport.getCode()+" name="+ap.airport.getName()+", state="+ap.airport.getState()+ " distance: "+Math.round(ap.dist)+" miles");
			}

			

		} catch(Exception e) {
			System.out.println("Client exception: " + e);
			e.printStackTrace();
		}
	}
}
