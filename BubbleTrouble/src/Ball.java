// Name: Yusuf Demir
// ID: 2021400033
// Date: 29/03/2023

import java.util.*;
import java.awt.*;
import java.io.*;

public class Ball {

	private int level;
	private double x0; // starting x-coordinate
	private double y0; // starting y-coordinate
	private double radius;
	private double heigth; // maximum heigth that ball can reach
	
	private double currentX; // current x-coordinate at the particular frame
	private double currentY; // current y-coordinate at the particular frame

	private double vX; // x component of ball's velocity
	private double vY; // y component of ball's velocity
	private int direction; // direction of the ball, if 1 direction is right, if -1 direction is left
	
	private double t0; // ball's starting time
	private double t1; // ball's current time
	private double collisionXTime; // time at the last horizontal collision, initially 0
	private double collisionYTime; // time at the last vertical collision, initially 0

	public double  minPossibleRadius = Environment.scaleY * 0.02;
	public double minPossibleHeigth = Player.heigth * 1.3;

	public static final double HEIGHT_MULTIPLIER = 1.75;
	public static final double RADIUS_MULTIPLIER  = 2.0;

	public static final double GRAVITY = Environment.scaleY * 0.0000004;
	public static final int PERIOD_OF_BALL = 10000;

	// no-arg constructor
	Ball(){}
	
	/**
	 * Constructor: creates a product with a particular point as the center of the ball
	 * @param level level of the ball
	 * @param direction if the direction equals 1 ball goes right, left for -1
	 * @param x0 starting x-coordinate
	 * @param y0 starting y-coordinate
	 */
	Ball (int level, int direction, double x0, double y0){
		
		setLevel(level);
		setDirection(direction);
		setX0(x0);
		setY0(y0);
		
		setT0(System.currentTimeMillis());;
		setCollisionXTime(0);
		setCollisionYTime(0);

		setRadius(minPossibleRadius * Math.pow(RADIUS_MULTIPLIER, level));;
		setHeigth(minPossibleHeigth * Math.pow(HEIGHT_MULTIPLIER, level));;
		
		setVX(Environment.scaleX / PERIOD_OF_BALL * direction);;
		setVY(Math.pow(2 * GRAVITY * heigth, 0.5));
		
		setCurrentX(getX0());
		setCurrentY(getY0());
	}
	
	/**
	 * Constructor: creates a product with default points as the center of the ball, depending on the level
	 * @param level level of the ball
	 */
	Ball(int level){
		
		setLevel(level);
		
		setT0(System.currentTimeMillis());;
		setCollisionXTime(0);
		setCollisionYTime(0);
		
		setRadius(minPossibleRadius * Math.pow(RADIUS_MULTIPLIER, level));;
		setHeigth(minPossibleHeigth * Math.pow(HEIGHT_MULTIPLIER, level));;
		

		if ((level == 0) || (level == 2)) {
			setX0(Environment.scaleX/4.0);;
			setY0(2);
			
			setDirection(1);
		}

		else if (level == 1) {
			setX0(Environment.scaleX/3.0);;
			setY0(2);
			
			setDirection(-1);
		}
		
		setVX(Environment.scaleX / PERIOD_OF_BALL * direction);;
		setVY(Math.pow(2 * GRAVITY * heigth, 0.5));
		
		setCurrentX(getX0());
		setCurrentY(getY0());
	}
	
	public void motion() { // this method makes a ball move
		setT1(System.currentTimeMillis() - getT0()); // current time for this particular ball is calculated

		setCurrentX(getX0() + getVX() * (getT1() - getCollisionXTime())); // current x coordinate for this particular ball is calculated
		setCurrentY(getY0() + getVY() * (getT1() - getCollisionYTime()) - GRAVITY * Math.pow((getT1() - getCollisionYTime()), 2.0) * 0.5); // current y coordinate for this particular ball is calculated
		
		if (collisionLeftX(getCurrentX())) { // if the ball collides with the left end :
			setCollisionXTime(System.currentTimeMillis() - getT0()); // new collisionXTime is calculated
			setX0(Environment.scaleX0 + getRadius()); // x0 changes to exact collision location + radius, thus a new origin point for the horizontal component of the ballistic motion is determined
			setVX(-1* getVX()); // horizontal velocity changes direction

			setCurrentX(getX0()); // current x becomes equal to new x0
		}

		else if (collisionRightX(getCurrentX())) { // if the ball collides with the right end :
			setCollisionXTime(System.currentTimeMillis() - getT0()); // new collisionXTime is calculated
			setX0(Environment.scaleX1 - getRadius()); // x0 changes to exact collision location - radius, thus a new origin point for the horizontal component of the ballistic motion is determined
			setVX(-1* getVX()); // horizontal velocity changes direction

			setCurrentX(getX0()); // current x becomes equal to new x0
		}


		if (collisionDownY(getCurrentY())) { // if the ball collides with the bottom :
			setCollisionYTime(System.currentTimeMillis() - getT0()); // new collisionYTime is calculated
			setY0(Environment.scaleY1 + getRadius()); // y0 changes to exact collision location + radius, thus a new origin point for the vertical component of the ballistic motion is determined
			setVY(-1* getVY()); // vertical velocity changes direction

			setCurrentY(getY0()); // current y becomes equal to new y0
		}
	}
	
	public void drawBall() { // this methods draws the ball
		
		StdDraw.picture(getCurrentX(), getCurrentY(), "ball.png", getRadius()*2, getRadius()*2);
	}
	
	public boolean isPlayerHit(Player player) { // this methods checks whether the ball hits the player or not
		
		if ((getCurrentY() - getRadius() + 0.05 <= player.getY() + Player.heigth / 2.0)) { // currentY - radius should be lower player's height 
			
			if ((getCurrentX() + getRadius() - 0.2 > player.getX() - Player.width/2.0) && (getCurrentX() + getRadius()  < player.getX() + Player.width/2.0)) { // if ball touches from the left side of the player, currentX + radius should be in between player's min and max x-coordiantes
				return true;
			}
			
			if ((getCurrentX() - getRadius() > player.getX() - Player.width/2.0) && (getCurrentX() - getRadius() + 0.2 < player.getX() + Player.width/2.0)) { // if ball touches from the right side of the player, currentX - radius should be in between player's min and max x-coordiantes
				return true;
			}
			
			if ((getCurrentX() > player.getX() - Player.width/2.0) && (getCurrentX() < player.getX() + Player.width/2.0)) { // if radius is too big, previous checking might be misleading, thus checking one more time with the x-coordinate of the ball's center
				return true;
			}
		}
		return false;
	}

	public boolean isArrowHit(Arrow arrow) { // this methods checks whether the arrow hits the ball or not (works similar to the 'isPlayerHit' method)
		
		if (arrow == null) { // if there is no active error, returns false
			return false;
		}
		
		else {
			double arrowHeadX = arrow.getX(); // x-coordinate of the arrow's head
			double arrowHeadY = arrow.getY() - Environment.scaleY1; // y-coordinate of the arrow's head
			
			if ((arrow.getX() <= currentX + radius) && (arrow.getX() >= currentX - radius)  && (arrowHeadY >= currentY - radius)) { // checks whether the ball touched arrow's sides 
				return true;
			}
			
			else if (Math.pow(arrowHeadX - currentX, 2) + Math.pow(arrowHeadY - currentY, 2.0) <= Math.pow(radius, 2.0)) { // checks whether the arrow head touched the ball
				return true;
			}
		}
		return false;
	}

	
	public boolean collisionLeftX(double x) { // checks whether the ball collides with the left end

		if ((x - getRadius()) < Environment.scaleX0)  {
			return true;
		}
		return false;
	}
	
	public boolean collisionRightX(double x) { // checks whether the ball collides with the right end

		if ((x + getRadius()) > Environment.scaleX1)  {
			return true;
		}
		return false;
	}

	public boolean collisionDownY(double y) { // checks whether the ball collides with the bottom

		if ((y - getRadius()) < Environment.scaleY1) {
			return true;
		}
		return false;
	}
	
	// getters and setters
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getX0() {
		return x0;
	}

	public void setX0(double x0) {
		this.x0 = x0;
	}

	public double getY0() {
		return y0;
	}

	public void setY0(double y0) {
		this.y0 = y0;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getHeigth() {
		return heigth;
	}

	public void setHeigth(double heigth) {
		this.heigth = heigth;
	}

	public double getCurrentX() {
		return currentX;
	}

	public void setCurrentX(double currentX) {
		this.currentX = currentX;
	}

	public double getCurrentY() {
		return currentY;
	}

	public void setCurrentY(double currentY) {
		this.currentY = currentY;
	}

	public double getVX() {
		return vX;
	}

	public void setVX(double vX) {
		this.vX = vX;
	}

	public double getVY() {
		return vY;
	}

	public void setVY(double vY) {
		this.vY = vY;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public double getT0() {
		return t0;
	}

	public void setT0(double t0) {
		this.t0 = t0;
	}

	public double getT1() {
		return t1;
	}

	public void setT1(double t1) {
		this.t1 = t1;
	}

	public double getCollisionXTime() {
		return collisionXTime;
	}

	public void setCollisionXTime(double collisionXTime) {
		this.collisionXTime = collisionXTime;
	}

	public double getCollisionYTime() {
		return collisionYTime;
	}

	public void setCollisionYTime(double collisionYTime) {
		this.collisionYTime = collisionYTime;
	}
	
	// toString
	@Override
	public String toString() {
		return "Ball [level=" + level + ", x0=" + x0 + ", y0=" + y0 + ", radius=" + radius + ", heigth=" + heigth
				+ ", currentX=" + currentX + ", currentY=" + currentY + ", vX=" + vX + ", vY=" + vY + ", direction="
				+ direction + ", t0=" + t0 + ", t1=" + t1 + ", collisionXTime=" + collisionXTime + ", collisionYTime="
				+ collisionYTime + ", minPossibleRadius=" + minPossibleRadius + ", minPossibleHeigth="
				+ minPossibleHeigth + "]";
	}
	


}
