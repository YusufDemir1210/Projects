// Name: Yusuf Demir
// ID: 2021400033
// Date: 29/03/2023

import java.util.*;
import java.awt.*;
import java.io.*;

public class Bar {
	
	public final double BAR_HEIGHT = 0.5;
	
	public final double BAR_STARTING_POINT = Environment.scaleX0;
	public final double BAR_VELOCITY =  2 * Environment.scaleX / Environment.TOTAL_GAME_DURATION;
	
	// no-arg constructor
	Bar(){}
	
	public void drawBar(double startTime) { // this method draws the bar
		
		double currentTime = getCurrentTime(startTime); // currentTime for bar's motion

		int barGreenValue = getBarGreenValue(currentTime); // calculating the green value of the bar's color

		StdDraw.setPenColor(255,barGreenValue,0); 

		double barEndingPoint = getBarEndingPoint(currentTime); // calculating the ending point of the bar at the moment

		StdDraw.filledRectangle((barEndingPoint - BAR_STARTING_POINT)/2.0, (Environment.scaleY0 - Environment.scaleY1)/2.0, Environment.scaleX/2.0, BAR_HEIGHT/2.0);

	}
	
	// getters
	public double getCurrentTime(double time) { // this method returns currentTime
		return System.currentTimeMillis() - time;
	}
	
	public double getBarEndingPoint(double time) { // this method returns bar's ending point
		return Environment.scaleX1 - BAR_VELOCITY * time;
	}
	
	public int getBarGreenValue(double time) { // this method returns the green value of the bar's color
		return (int) (225 * (1 - time / Environment.TOTAL_GAME_DURATION));
	}
	
	// toString
	@Override
	public String toString() {
		return "Bar [BAR_HEIGHT=" + BAR_HEIGHT + ", BAR_STARTING_POINT=" + BAR_STARTING_POINT + ", BAR_VELOCITY="
				+ BAR_VELOCITY + "]";
	}
	
	
}
