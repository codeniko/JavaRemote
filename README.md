README - Rutgers CS417 - Distributed Systems
==========

This is assignment 5 to learn how to use Google Protocol Buffers and Java RMI.

INSTRUCTIONS FOR COMPILING
--------------------------
Make sure you're in the assignment5 directory and run the following command:
javac -cp ".:./protobuf.jar" *.java AirportData/AirportDataProto.java PlaceData/PlaceDataProto.java

INSTRUCTIONS FOR RUNNING
-------------------------
First, run the rmiregistry and listen on a port of your choosing. An example with port 1099 is as follow:
rmiregistry 1099

Make sure you're in the assignment5 directory and run the following commands. For running the servers, you can append an optional argument containing the port for the rmiserver. By default, it will use 1099.
java -cp ".:./protobuf.jar" -Djava.security.maner -D"java.security.policy=policy" PlaceServer
java -cp ".:./protobuf.jar" -Djava.security.maner -D"java.security.policy=policy" AirportServer

For the Client, it accepts an optional -p argument containing port and an optional -h argument containing rmi_registryserver. The default for port is 1099 and for rmi_registryserver is localhost. While those are optional, the client does require two arguments though containing a city and a state for the search. An example with all the optional arguments is as follow:
java -cp ".:./protobuf.jar" Client -p 1099 -h localhost Princeton NJ

javac *.java -cp "/home/niko/JavaRemote:/home/niko/JavaRemote/protobuf.jar"


HOW DATA IS STORED
------------------
When each of the servers are run, they create an instance of its corresponding service class, Airport or Place. When the instance is created, the google proto binary file that contains all the place/airport information is read inside the constructor and stored in memory. If the file location is incorrect, the variable is null and returns a RemoteResult object containing an enum value of RemoteErrorCode.FILENOTFOUND, which is handled gracefully by the client.

HOW DATA IS SEARCHED FOR
------------------------
When the client does the naming lookup to retrieve either the Airport or Plane service stub object, this object has a stub method to perform its own "lookup". You call place.lookup(city, state) or airport.lookup(lat, lon) to perform a lookup. Prior to this call, the object should already have loaded all of the places/airports from when it was first initialized in the constructor, NOT during the lookup call. Therefore, the lookup simply traverses through the list of airports/places and finds matches. 

More specifically, to find a place, it matches city to what the user provided using startsWith and the state match entirely, case insensitive. To find the 5 nearest airports, latitude and longitude coordinates are passed into the lookup function. The service object creates a MinHeap based on distance and goes through each of the airports in its list. For each airport, it calculates the distance between the given coords and the airports coords, and put the object along with its distance into the MinHeap. Once all the airports have been processed, the top 5 airport objects and their distances in the minheap are polled and placed into an array that is returned.

