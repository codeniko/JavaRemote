import AirportData.AirportDataProto.*;
import java.io.Serializable;

public class AP implements Serializable {
	public Airport airport;
	public double dist;
	
	public AP(Airport a) {
		airport = a;
	}
}
