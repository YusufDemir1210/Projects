// Name: Yusuf Demir
// ID: 2021400033
// Date: 29/03/2023

import java.util.*;
import java.awt.*;
import java.io.*;

public class Yusuf_Demir {
	
	public static Ball[] array = new Ball[3];

	public static void main(String[] args) {
		
	
		array[0] = new Ball(1);
		array[2] = new Ball(2);
		array[1] = new Ball(0);
		
		Ball ball = tryF(2);
		System.out.println(ball);
		
		
	}
	
	public static Ball tryF(int n) {
		
		Ball answer = array[n];
		array[n] = null;
		return answer;
		
	}

}
