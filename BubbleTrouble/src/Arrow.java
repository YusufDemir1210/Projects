// Name: Yusuf Demir
// ID: 2021400033
// Date: 29/03/2023

import java.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class Arrow {

	public static final int PERIOD_OF_ARROW = 1500;
	public static final double ARROW_WIDTH = 0.25;
	public static double hitTime = -1; // a static variable which is used to calculate how much time passed since the last ball hit
	
	private double x; // x-coordinate of the center point
	private double y; // y-coordinate of the center point
	private double velocity; // velocity of the arrow
	
	// no-arg constructor
	Arrow(){}
	
	Arrow(double x){
		setX(x);
		setY(Player.heigth);
		
		setVelocity(50 * (Environment.scaleY2 - y) / (double)PERIOD_OF_ARROW);;
		
	}
	
	public void drawArrow() { // this methods draws the arrow
		StdDraw.picture(getX(), (getY() - Environment.scaleY1)/2.0, "arrow.png", Arrow.ARROW_WIDTH, getY() - Environment.scaleY1);
	}
	
	// getters and setters
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	// toString
	@Override
	public String toString() {
		return "Arrow [x=" + x + ", y=" + y + ", velocity=" + velocity + "]";
	}
	
	
}
