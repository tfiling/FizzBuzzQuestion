package edu.eldar.sol;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class FizzBuzz implements Runnable{

	AtomicBoolean divBy3=new AtomicBoolean(false);
	AtomicBoolean divBy5=new AtomicBoolean(false);
	AtomicBoolean divBy3And5=new AtomicBoolean(false);
	AtomicBoolean isMax=new AtomicBoolean(false);
	int max;

	public FizzBuzz(int n){

		max=n;
	}

	public void run() {

		String threadJob=Thread.currentThread().getName();

		switch (threadJob){

			case "divBy3":
				fizz();
				break;
			case "divBy5":
				buzz();
				break;

			default:
				for (int i=1 ; i<=max ; i++){

					if (i%3==0 && !(i%5==0)){
						divBy3.set(true);
						this.notifyAll();
						try {
							this.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					else if (!(i%3==0 ) && i%5==0){
						divBy5.set(true);
						this.notifyAll();
						try {
							this.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						System.out.println(i);
					}

				}

				isMax.set(true);
				this.notifyAll();
		}
		System.out.println("Thread "+Thread.currentThread().getName() +" exit");
	}

	private void buzz()
	{
		while (!isMax.get()){
			if (!divBy5.get()){
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Buzz");
			divBy5.set(false);
			this.notifyAll();
		}
	}

	private void fizz()
	{
		while (!isMax.get()){
			if (!divBy3.get()){
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Fizz");
			divBy3.set(false);
			this.notifyAll();
		}
	}


}
