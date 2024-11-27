package osu.cse2123;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 *   A simulator program for shortest path simulation based on the list of destinations
 *   
 *   @author Connor Kovacs
 *   @version 12012022
 */

public class ShortestPath {

	
	public static void main(String[] args) throws FileNotFoundException{
		// ask user for filename
		Scanner input = new Scanner(System.in);
		System.out.println("Enter a filename with paths: ");
		String fname = input.nextLine();
		System.out.println("Start City     Paths");
		System.out.println("-------------- ------------------------------");
		// read paths from the file
		Map<String, List<Path>> paths = readPaths(fname);

		// display list
		displayAdjacencyList(paths);

		// ask user for start city
		System.out.println("\nEnter a start city (empty line to quit): ");

		String start = input.nextLine();

		while(!start.isEmpty()) {
			// find the shortest paths from the start city

			Map<String, Double> shortest = findDistances(start, paths);

			// display the shortest paths from the start city
			displayShortest(start, shortest);

			// ask user for a start city 
			System.out.println("\nEnter a start city (empty line to quit): ");
			start = input.nextLine();
		}
		//bye
		System.out.println("Goodbye!");
		input.close();
	}




	public static Map<String, Double> findDistances(String start, Map<String, List<Path>> adj_list){
		// create a map to store the distance from the start node to each other node
		Map<String, Double> distances = new HashMap<>();

		// initialize the distances map with the start node set to 0.0
		distances.put(start, 0.0);

		// create a queue to store the nodes if not empty
		Queue<String> queue = new LinkedList<>();
		//add start
		queue.add(start);

		// while there are still nodes we will eval them and then exe
		while (!queue.isEmpty()) {
			String current = queue.remove();
			double currentDistance = distances.get(current);

			// for each next we will just update the distance if necessary
			for (Path path : adj_list.get(current)) {
				String destination = path.getEndpoint();
				double distance = currentDistance + path.getCost();
				if (!distances.containsKey(destination) || distance < distances.get(destination)) {
					distances.put(destination, distance);
					queue.add(destination);
				}
			}
		}

		// return our map with the distances
		return distances;
	}

	public static void displayShortest(String start, Map<String, Double> shortest) {
		// prints out the distances from start to each city in the Map
		System.out.println();
		System.out.println("Distances from " + start + " to each city:");

		System.out.println("Dest. City     Distance");
		System.out.println("-------------- --------");
		for(Map.Entry<String, Double> entry : shortest.entrySet()){
			// for each entry print out the name of the city and the distance from start to the city with format.
			System.out.printf("%-15s %7.2f\n",entry.getKey(), entry.getValue());
		}
	}

	// Displays the entire adjacency list stored in the multimap that is passed in as a parameter.  
	public static void displayAdjacencyList(Map<String, List<Path>> paths) {
		// sort the paths in the map in ascending order by key
		Map<String, List<Path>> sortedPaths = new TreeMap<String, List<Path>>(paths);
		// iterate through the sorted paths
		for(Map.Entry<String, List<Path>> entry : sortedPaths.entrySet()) {
			// print the key
			System.out.printf("%-14s ", entry.getKey());
			// get the list of paths
			List<Path> distances = entry.getValue();
			// iterate through the list of paths and print each path
			for (int i = 0; i < distances.size(); i++) {
				if (i == distances.size() - 1) {
					System.out.printf("%s", distances.get(i));
				} else {
					System.out.printf("%s, ", distances.get(i));
				}
			}
			System.out.println();
		}
	}
	//Takes a filename for a file containing paths list as an argument.  
	//Creates an adjacency list by reading paths from the input file and organizing them in the multimap 
	//Returns a map of paths
	public static Map< String, List<Path> > readPaths(String fname){
		Map<String, List<Path>> paths = new HashMap<String, List<Path>>();
		//*bad practice to use exceptions in control flow*
		try {
			//create a  reader and file reader
			BufferedReader reader = new BufferedReader(new FileReader(fname));

			//read the file line by line
			String line = reader.readLine();
			while(line != null) {
				//split by comma
				String[] parts = line.split(",");
				//get the nodes distance from the line
				String node1 = parts[0];
				String node2 = parts[1];
				double distance = Double.parseDouble(parts[2]);
				//order alphabetically
				String first = node1;
				String second = node2;
				if (node2.compareTo(node1) < 0) {
					first = node2;
					second = node1;
				}
				//create a new SimplePath object with the node and distance
				SimplePath path1 = new SimplePath(second, distance);
				SimplePath path2 = new SimplePath(first, distance);

				//we check if the first node is already in the map
				if(paths.containsKey(first)) {
					//If it hits then we add the path to the list of paths for that node
					paths.get(first).add(path1);
				} else {
					//else create a new list with the path and add it to the map
					List<Path> list = new ArrayList<Path>();
					list.add(path1);
					paths.put(first, list);
				}

				//we check if the second node is already in the map
				if(paths.containsKey(second)) {
					//If it hits then we add the path to the list of	paths for that node
					paths.get(second).add(path2);
				} else {
					//else create a new list with the path and add it to the map
					List<Path> list = new ArrayList<Path>();
					list.add(path2);
					paths.put(second, list);
				}

				//read the next line
				line = reader.readLine();
			}

			reader.close();//close our reader

		} 
		catch (IOException e) {//catch
			e.printStackTrace();
		}
		return paths;//return paths
	}
}
