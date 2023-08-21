// Name: Yusuf Demir
// ID: 2021400033
// Date: 29/03/2023

import java.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class Player {

	public static final int PERIOD_OF_PLAYER = 6000;

	public static final double PLAYER_HEIGHT_WIDTH_RATE = 37.0 / 27.0;
	public static final double PLAYER_HEIGHT_SCALEY_RATE  = 1.0 / 8.0;

	public static double heigth = PLAYER_HEIGHT_SCALEY_RATE * Environment.scaleY; // heigth of the player
	public static double width = heigth / PLAYER_HEIGHT_WIDTH_RATE; // width of the player

	private double x; // x-coordinate of the center point
	private double y; // y-coordinate of the center point
	
	// no-arg constructor
	Player(){
		setX((Environment.scaleX0 + Environment.scaleX1) / 2.0);
		setY(Environment.scaleY1 + width / 2.0);
	}
	
	public void motion() { // this method makes player move
		
		if ((StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) && (getX() - Player.width /2.0 >= Environment.scaleX0)) { // if 'left key' is pressed, x coordinate of player decreases by a little
			setX(getX() - 0.12);	
		}

		if ((StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) && (getX() + Player.width /2.0 <= Environment.scaleX1)) { // if 'left key' is pressed, x coordinate of player increases by a little
			setX(getX() + 0.12);			
		}

	}
	
	public void drawPlayer() { // this method draws player
		StdDraw.picture(getX(), getY() + 0.15, "player_back.png", width, heigth); // adding +0.15 to place the player to the correct y-location
	}
	
	
	// getters and Setters
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
	
	// toString
	@Override
	public String toString() {
		return "Player [x=" + x + ", y=" + y + "]";
	}
	
	

	
}

