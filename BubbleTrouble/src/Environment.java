// Name: Yusuf Demir
// ID: 2021400033
// Date: 29/03/2023

import java.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;


public class Environment {
	
	public static int canvasWidth =  800;
	public static int canvasHeigth = 500;

	
	public static double scaleX0 = 0; // min possible x value
	public static double scaleX1 = 16; // max possible x value
	public static double scaleY0 = -1; // min possible y value
	public static double scaleY1 = 0; // min possible y value above the time bar
	public static double scaleY2 = 9; // max possible y value
	
	public static double scaleX = scaleX1 - scaleX0; 
	public static double scaleY = scaleY2 - scaleY1;
	
	public static final int TOTAL_GAME_DURATION = 40000;
	public static final int PAUSE_DURATION = 15;
	public static final int ARROW_COOLDOWN_DURATION = 160; // a cooldown time for arrow shots to prevent undesired situations
	
	public static final double GAME_SCREEN_X = scaleX / 2.0;
	public static final double GAME_SCREEN_Y = scaleY / 2.18;
	public static final double GAME_SCREEN_WIDTH = scaleX / 3.8;
	public static final double GAME_SCREEN_HEIGTH = scaleY / 4.0;
	
	// no-arg constructor
	Environment(){}
	
	public static void bubbleTrouble() { // this method is basically the game itself
		
	
		Player player = new Player(); // creating a new player object	

		boolean victory = false; // if player wins, this becomes true
		boolean gameOver = true; // if player wins, this becomes false

		Arrow arrow = null; // declaring an arrow object, it is null since there should be no arrow until 'space' is pressed
		Bar bar = new Bar(); // creating a new bar object

		ArrayList<Ball> ballList = new ArrayList<Ball>(); // creating an ArrayList of ball objects which will consist of all the active balls

		ballList.add(new Ball(2)); // creating the original level 2 ball
		ballList.add(new Ball(1)); // creating the original level 1 ball
		ballList.add(new Ball(0)); // creating the original level 0 ball
		
		double startTime = System.currentTimeMillis();
		
		while (System.currentTimeMillis() - startTime <= Environment.TOTAL_GAME_DURATION) { // making sure that the games lasts no longer than TOTAL_GAME_DURATION

			StdDraw.clear(); // clearing the background
			Environment.drawBackground(); // drawing the background

			bar.drawBar(startTime); // drawing the bar
			
			player.motion(); // this method moves the player
			
			if ((StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) && (arrow == null) && (System.currentTimeMillis() - Arrow.hitTime >= ARROW_COOLDOWN_DURATION)) { // if 'space' is pressed checks cooldown time and if there is no active arrow, creates a new arrow
				arrow = new Arrow(player.getX());

			}

			if (arrow != null) { // moving the arrow
				arrow.drawArrow(); // drawing the arrow
				arrow.setY(arrow.getY() + arrow.getVelocity());

				if (arrow.getY() > Environment.scaleY2 + arrow.getVelocity()) { // if arrow reaches max height, it disappears
					arrow = null;
				}

			}
			player.drawPlayer(); // drawing the player
			
			ArrayList<Ball> trashList = new ArrayList<Ball>(); // creating an ArrayList which will contain balls which was hit by the arrow
			
			boolean arrowHit = false; // becomes true if arrow hits at least one ball
			boolean playerHit = false; // becomes true if a ball touches the player

			for (Ball ball : ballList) { // iterating over all the balls
				
				ball.motion(); // moving the ball

				if (ball.isArrowHit(arrow)){ // if this ball is hit, ball is added to a temporary ArrayList, and boolean becomes true
					trashList.add(ball);
					arrowHit = true;
					Arrow.hitTime = System.currentTimeMillis(); // hitTime is saved
				}

				ball.drawBall(); // drawing the ball
				
				if (ball.isPlayerHit(player)) { // if the player is hit by the ball, player loses
					playerHit = true;		
				}

			}
			
			if (playerHit) { // this means the player is hit by a ball
				Environment.endScreen(victory, gameOver, player, arrow, bar, startTime); // this methods makes end screen pop up
				return;	
			}
			
			if (arrowHit) { // if the arrow touches more than one ball at the particular frame, only the ball at the lowest point is counted as hit
				
				double minY = Double.MAX_VALUE;
				Ball trashBall = null;
				
				if (trashList.size() == 1) { // checking for efficiency
					trashBall = trashList.get(0);
				}
				
				else {
					for (Ball trash : trashList) {
						if (trash.getCurrentY() - trash.getRadius() < minY) {
							trashBall = trash;			
						}
					}
				}	
				
				ballList.remove(trashBall);
				if (trashBall.getLevel() > 0) { // if the hit ball is level 1 or level 2, 2 new balls with lower levels are created
					
					Ball newBall0 = new Ball(trashBall.getLevel() -1, 1, trashBall.getCurrentX(), trashBall.getCurrentY());
					Ball newBall1 = new Ball(trashBall.getLevel() -1, -1, trashBall.getCurrentX(), trashBall.getCurrentY());
					
					ballList.add(newBall0);
					ballList.add(newBall1);
					
					newBall0.drawBall(); // drawing the new balls
					newBall1.drawBall();
				}
			
			}			
			if (ballList.size() == 0) { // if there is no ball left in ballList, player wins
				victory = true;
				gameOver = false;
				break;
			}
			
			if (arrowHit) { // this makes arrow passive
				arrow = null;
			}

			StdDraw.show();
			StdDraw.pause(Environment.PAUSE_DURATION);


		}
		
		Environment.endScreen(victory, gameOver, player, arrow, bar, startTime); // this methods makes end screen pop up
	}
	
	public static void endScreen(boolean victory, boolean gameOver, Player player, Arrow arrow, Bar bar, double t0) {
		
		if (victory) {	
			Environment.victory(player, arrow, bar, t0); // writes victory text

		}

		else if (gameOver){
			Environment.gameOver(); // writes victory text
		}

		while (true) { // taking the next input
			
			if (StdDraw.isKeyPressed(KeyEvent.VK_N)) { // if 'N' is pressed, code terminates itself
				System.exit(0);
				
			}
			
			if (StdDraw.isKeyPressed(KeyEvent.VK_Y)) { // if 'Y' is pressed, a new game starts with utilization of recursion
				Environment.bubbleTrouble();
				return;
			}

		}
	}

	
	public static void set() { // this methods creates the canvas
		StdDraw.setCanvasSize(canvasWidth,canvasHeigth);
		StdDraw.setXscale(scaleX0, scaleX1);
		StdDraw.setYscale(scaleY0, scaleY2);

		StdDraw.enableDoubleBuffering();		
	}
	
	public static void drawBackground() { // this methods draws the background
		StdDraw.picture((scaleX1 - scaleX0)/2.0, (scaleY2 - scaleY1)/2.0, "background.png", scaleX, scaleY);
		StdDraw.picture((scaleX1 - scaleX0)/2.0, (scaleY0 - scaleY1)/2.0, "bar.png", scaleX, (scaleY1 - scaleY0));

	}
	
	public static void gameScreen() { // this methods makes end screen pop up without victory or loss text
	
		StdDraw.picture(Environment.GAME_SCREEN_X, Environment.GAME_SCREEN_Y, "game_screen.png", Environment.GAME_SCREEN_WIDTH, Environment.GAME_SCREEN_HEIGTH);
	
		StdDraw.setFont(new Font("Helvetica", Font.ITALIC, 15));
		StdDraw.setPenColor(StdDraw.BLACK);
		
		StdDraw.text(Environment.scaleX/2.0, Environment.scaleY/2.3, "To Replay Click 'Y'");
		StdDraw.text(Environment.scaleX/2.0, Environment.scaleY/2.6, "To Quit Click 'N'");
		
		StdDraw.show();
	}
	
	public static void victory(Player player, Arrow arrow, Bar bar, double t0) { // this methods writes 'You Won!'
		
		// drawing the final frame without ball which is hit
		StdDraw.clear();
		Environment.drawBackground();
		bar.drawBar(t0);
		arrow.drawArrow();
		player.drawPlayer();
		
		Environment.gameScreen(); // this methods makes end screen pop up without victory or loss text
		
		StdDraw.setPenColor(StdDraw.BLACK);
		
		StdDraw.setFont(new Font("Helvetica", Font.BOLD, 30));
		StdDraw.text(Environment.scaleX/2.0, Environment.scaleY/2.0, "You Won!");
		
		StdDraw.show();
	}
	
	public static void gameOver() { // this methods writes 'Game Over!'
		Environment.gameScreen(); // this methods makes end screen pop up without victory or loss text
		
		StdDraw.setPenColor(StdDraw.BLACK);
		
		StdDraw.setFont(new Font("Helvetica", Font.BOLD, 30));
		StdDraw.text(Environment.scaleX/2.0, Environment.scaleY/2.0, "Game Over!");
		
		StdDraw.show();
	}
	
	
	
}
