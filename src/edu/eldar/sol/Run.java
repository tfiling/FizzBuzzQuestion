package edu.eldar.sol;

import java.util.concurrent.locks.Lock;

public class Run {
	
	
	
	public static void main(String[] args) {
		
		FizzBuzz obj=new FizzBuzz(15);
		Thread t1=new Thread(obj,"divBy3");
		Thread t2=new Thread(obj,"divBy5");
		Thread t3=new Thread(obj);
		
		t1.start();
		t2.start();
		t3.start();
	}

}
