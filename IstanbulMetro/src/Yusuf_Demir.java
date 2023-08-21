// Name: Yusuf Demir
// ID: 2021400033
// Date: 14/03/2023

import java.util.*;
import java.awt.*;
import java.io.*;

public class Yusuf_Demir {



	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		File file = new File("input.txt");
		Scanner inputFile = new Scanner(file);

		// Creating arrays to sort the data taken from the coordinates.txt
		String[] metroLineNames = new String[10];
		String[] metroLineColors = new String[10];

		String[][] stationNames = new String[10][];
		String[][][] stationCoordinates = new String[10][][];

		String[] breakpointNames = new String[7];
		String[][] breakpointConnections = new String[7][];

		for (int i = 0; i < 10; i++) { // for the first 20 lines, first of every two is about the metro line, the other is about stations
			String[] metroLineInfo = inputFile.nextLine().split(" ");  // splitting and sorting the data of the metro line line

			metroLineNames[i] = metroLineInfo[0];
			metroLineColors[i] = metroLineInfo[1];

			String[] stationInfo = inputFile.nextLine().split(" "); // every element with even index represents a station name, odd ones represent station coordinates

			String[] tempNames = new String[stationInfo.length/2]; // even indexed names goes into this array
			String[][] tempCoordinates = new String[stationInfo.length/2][2]; // odd indexed names goes into this array

			int index = 0;
			for (int j = 0; j < tempNames.length; j++) {
				tempNames[j] = stationInfo[index];
				tempCoordinates[j] = stationInfo[index+1].split(",");
				index +=2;
			}

			stationNames[i] = tempNames;
			stationCoordinates[i] = tempCoordinates;
		}

		for (int i = 0; i < 7; i++) { // remaining 7 lines are about breakpoints
			String[] breakpointInfo = inputFile.nextLine().split(" ");

			breakpointNames[i] = breakpointInfo[0]; // first element of a line gives us the name of breakpoint station

			String[] tempList = new String[breakpointInfo.length -1]; // remaning elements are the connected lines at that breakpoint and they go into this array

			for (int j = 0; j < breakpointInfo.length-1; j++) {
				tempList[j] = breakpointInfo[j+1];
			}

			breakpointConnections[i] = tempList;

		}

		inputFile.close();

		// Taking input from the user
		Scanner input = new Scanner(System.in);
		String inputStation0 = input.next();
		String inputStation1 = input.next();
		String inputMetroLine0 = "";
		String inputMetroLine1 = "";

		// Checking (with for-loops) if the given inputs are actual station names 
		boolean nonValidStationName0 = true;
		boolean nonValidStationName1 = true;

		for (int i = 0; i < 10; i++) {
			for (String station : stationNames[i]) {

				if ((station.equals(inputStation0)) || (station.equals("*" + inputStation0))) {
					inputMetroLine0 = metroLineNames[i];
					nonValidStationName0 = false;
				}

				if ((station.equals(inputStation1)) || (station.equals("*" + inputStation1))) {
					inputMetroLine1 = metroLineNames[i];
					nonValidStationName1 = false;
				}
			}
		}

		if ((nonValidStationName0) || (nonValidStationName1)) { // if at least one input is invalid, print error message
			System.out.println("No such station names in this map");
		}

		else { // if everything is correct, start the algorithm
			
			boolean onTheSameLine = false;

			if (inputMetroLine0.equals(inputMetroLine1)) { // check whether 2 given stations are on the same line or not
				onTheSameLine = true;
			}
			
			ArrayList<String> triedLines = new ArrayList<String>(); // creating an ArrayList, which contains all tried lines in the particular recursive loop
			triedLines.add(inputMetroLine0);

			ArrayList<String> temp = new ArrayList<String>(); // creating an empty ArrayList, which will returned by the recursivePathFinder method

			ArrayList<String> pathLines = recursivePathFinder(inputMetroLine0, inputMetroLine1, triedLines, temp, breakpointConnections); // creating an ArrayList which contains the metro lines used to reach the target destination
			
			if ((pathLines.size() == 0) && (!onTheSameLine)) { // if there is no connection, pathLines is an empty ArrayList
				System.out.println("These two stations are not connected");
			}
			
			else {
				ArrayList<String> pathConnections = new ArrayList<String>(); // creating an ArrayList to add used breakpoints to reach the target destination

				

				if (onTheSameLine) { // adding given 2 stations to pathConnections
					pathConnections.add(inputStation0);
					pathConnections.add(inputStation1);
				}

				else { // this line makes sure that 2 given stations are not on the same metro line, otherwise pathConnections.size() would be equal to 1
					int index = 0;
					for (int i = 0; i < pathLines.size()-1; i++) {

						String line0 = pathLines.get(index);
						String line1 = pathLines.get(index+1);
						index += 1;

						for (int j = 0; j < breakpointConnections.length; j++) { // finding the element of breakpointConnections Array which contains 2 successive lines of pathLines
							if ((in(line0, breakpointConnections[j])) && (in(line1, breakpointConnections[j]))) {
								if (!pathConnections.contains(breakpointNames[j])) {
									pathConnections.add(breakpointNames[j]); // the breakpointNames element with the same index is the necessary breakpoint station 
									break;
								}

							}
						}
					}
					// adding 2 given stations to pathConnections if they are not already present in pathConnections
					if (!inputStation0.equals(pathConnections.get(0))) {
						pathConnections.add(0, inputStation0);
					}

					if (!inputStation1.equals(pathConnections.get(pathConnections.size()-1))) {
						pathConnections.add(inputStation1);
					}
				}


				// Creating a final ArrayList which will contain every station on our destination
				ArrayList<String> resultantPath = new ArrayList<String>();

				for (int i = 0; i < pathConnections.size()-1; i++) {
					String station0 = pathConnections.get(i);
					String station1 = pathConnections.get(i+1);


					int index0 = -1;
					int index1 = -1;
					int increment = 0;

					resultantPath.add(station0);

					for (String[] stationArray : stationNames) { // finding the indexes of 2 successive stations of pathConnections in the stationNames Array and adding every station in between to resultantPath
						if (((in(station0,stationArray)) || (in("*" + station0,stationArray))) && ((in(station1,stationArray)) || (in("*" + station1,stationArray)))) {

							if (in(station0,stationArray)){
								for (int j = 0; j < stationArray.length; j++) {
									if (stationArray[j].equals(station0)) {
										index0 = j;
										break;
									}
								}
							}
							else if (in("*" + station0,stationArray)){
								for (int j = 0; j < stationArray.length; j++) {
									if (stationArray[j].equals("*" + station0)) {
										index0 = j;
										break;
									}
								}
							}

							if (in(station1,stationArray)){
								for (int j = 0; j < stationArray.length; j++) {
									if (stationArray[j].equals(station1)) {
										index1 = j;
										break;
									}
								}
							}
							else if (in("*" + station1,stationArray)){
								for (int j = 0; j < stationArray.length; j++) {
									if (stationArray[j].equals("*" + station1)) {
										index1 = j;
										break;
									}
								}
							}

							if (index0 < index1) { // if this line is true, we should increase our index to start from index0 to reach index1
								increment = 1;
							}
							else if (index0 > index1) { // if this line is true, we should decrease our index to start from index0 to reach index1
								increment = -1;
							}

							for (int j = index0 + increment; j != index1; j += increment) {
								String nextStation = stationArray[j];	


								if (nextStation.charAt(0) == '*') { // if a station name starts with '*' we should delete that character
									nextStation = nextStation.substring(1);
								}
								if (!resultantPath.contains(nextStation)) {
									resultantPath.add(nextStation);
								}
							}
						}
					}
				}
				if (!resultantPath.contains(inputStation1)) { // adding the target destination to resultantPath
					resultantPath.add(inputStation1);	
				}

				for (String station : resultantPath) { // printing every element of resultantPath
					System.out.println(station);
				}

				// Drawing the map

				StdDraw.setCanvasSize(1024, 482);
				StdDraw.setXscale(0,1024);
				StdDraw.setYscale(0,482);
				StdDraw.setFont( new Font("Helvetica", Font.BOLD, 8) );
				StdDraw.enableDoubleBuffering();

				defaultMapDrawer(metroLineColors, stationNames, stationCoordinates); // this method is explained below

				// Animation

				for (int i = 0; i < resultantPath.size(); i++) {

					String station0 = resultantPath.get(i);

					double[] coordinates0 = findCoordiantes(station0, stationNames, stationCoordinates); // this method is explained below

					double x0 = coordinates0[0];
					double y0 = coordinates0[1];

					defaultMapDrawer(metroLineColors, stationNames, stationCoordinates); // redrawing the original map
					StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);

					for (int j = 0; j < i; j++) { // recoloring all the already passed stations to make the circle smaller which was once bigger
						String station1 = resultantPath.get(j);

						double[] coordinates1 = findCoordiantes(station1, stationNames, stationCoordinates);

						double x1 = coordinates1[0];
						double y1 = coordinates1[1];

						StdDraw.filledCircle(x1, y1, 2.75);
					}

					StdDraw.filledCircle(x0, y0, 5); // coloring the current station
					StdDraw.show(); // showing the current status of the map
					StdDraw.pause(320);

				}
			}

		}

	}

	private static void defaultMapDrawer(String[] metroLineColors, String[][] stationNames, String[][][] stationCoordinates) { // resets the map to its original state
		StdDraw.clear();
		StdDraw.picture(512, 241, "background.jpg");

		for (int i = 0; i < 10; i++ ) { // getting RGB values from the metroLineColors Array
			String[] colors = metroLineColors[i].split(",");

			int red = Integer.parseInt(colors[0]);
			int green = Integer.parseInt(colors[1]);
			int blue = Integer.parseInt(colors[2]);

			for (int j = 0; j < stationCoordinates[i].length-1; j++) { // getting the coordinates of successive stations

				String stationName0 = stationNames[i][j];
				String stationName1 = stationNames[i][j+1];

				double x0 = Double.parseDouble(stationCoordinates[i][j][0]);
				double y0 = Double.parseDouble(stationCoordinates[i][j][1]);

				double x1 = Double.parseDouble(stationCoordinates[i][j+1][0]);
				double y1 = Double.parseDouble(stationCoordinates[i][j+1][1]);


				StdDraw.setPenColor(red, green, blue); // Drawing the part of the metro line which connects successive stations
				StdDraw.setPenRadius(0.012);
				StdDraw.line(x0, y0, x1, y1);

				StdDraw.setPenColor(StdDraw.WHITE); // Drawing the station circles
				StdDraw.filledCircle(x0, y0, 2.75);
				StdDraw.filledCircle(x1, y1, 2.75);

				if ('*' == stationName0.charAt(0)) { // Writing station names if they start with '*'
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.text(x0, y0 +5, stationName0.substring(1));
				}
				if ('*' == stationName1.charAt(0)) {
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.text(x1, y1 +5, stationName1.substring(1));
				}			
			}	
		}
		StdDraw.show();


	}

	private static ArrayList<String> recursivePathFinder(String currentLine, String targetLine, ArrayList<String> triedLines, ArrayList<String> path, String[][] breakpointConnections){ // this methods recursively finds the lines which should be used to reach the target destination

		String[] neighbourLines = neighbourFinder(currentLine, breakpointConnections); // this method is explained below
		path.add(currentLine); // adding current line to the path ArrayList


		ArrayList<String> endRecursion = new ArrayList<String>(); // creating an undesired empty ArrayList to return if recursion failed

		if (targetLine.equals(currentLine)) { // if they are on the same line, no need for recursion
			return path;
		}
		
		if (in(targetLine,neighbourLines)) { // desired condition to end recursion
			path.add(targetLine);
			return path; // returns path ArrayList which contains all the necessary lines
		}

		else { // if triedLines contains all neighbourLine, this means this recursion failed, thus finish the loop
			boolean end = true;
			for (String neighbourLine : neighbourLines) {
				if (!triedLines.contains(neighbourLine)) {
					end = false;
					break;
				}
			}

			if (end) {
				return endRecursion;
			}
		}

		for (String neighbourLine : neighbourLines) { // starting a new loop for every neighboring line
			if (!triedLines.contains(neighbourLine)) { // skip the neighbour line if it is already tried
				triedLines.add(neighbourLine); // add the untried neighbour line to triedLines to prevent infinite loops
				ArrayList<String> temp = new ArrayList<>(path); // creating a copy of path
				ArrayList<String> result = recursivePathFinder(neighbourLine, targetLine, triedLines, temp, breakpointConnections);

				if (result.size() > 1) { // checking whether result is the desired ArrayList or not
					return result;
				}

			}
		}
		return endRecursion;

	}

	private static String[] neighbourFinder(String lineName, String[][] breakpointConnections) { // finds all the neighbour metro lines of a given metro line

		int neighbourCount = 0; // first counting the number of neighbours

		for (String[] connections : breakpointConnections) {
			if (in(lineName, connections)) {
				neighbourCount += connections.length -1;
			}
		}

		String[] result = new String[neighbourCount]; // creating an array which will contain all the neighbours and be returned
		int index = 0;

		for (String[] connections : breakpointConnections) { // if a breakpoint connects given metro line with other, add all the others to the result Array
			if (in(lineName, connections)) {
				for (String line : connections) {
					if (!line.equals(lineName)) {
						result[index] = line;
						index += 1;
					}
				}
			}
		}
		return result;

	}

	private static double[] findCoordiantes(String station, String[][] names, String[][][] coordinates){ // this method finds the coordinates of a given station, utilizing already created Arrays
		double[] result = {-1, -1};

		for (int i = 0; i < 10; i++) {

			for (int j = 0; j < names[i].length; j++) { // find the index of station in stationNames, then get the coordinates with same index from stationCoordinates
				String tempStation = names[i][j];
				if ((tempStation.equals(station)) || (tempStation.equals("*" + station))) { 

					result[0] = Double.parseDouble(coordinates[i][j][0]);
					result[1] = Double.parseDouble(coordinates[i][j][1]);

					return result;
				}
			}
		}
		return result;
	}

	private static boolean in(String s, String[] list) { // this method checks if a given Array contains a given String
		boolean logic = false; // logic is initially false

		for (String temp : list) {
			if (s.equals(temp)) { // if one element is equal to given String, logic becomes true
				logic = true;
				break;
			}
		}
		return logic;
	}

}