package edu;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class FizzBuzz
{
    public static int maxCountValue = 20;

    private static AtomicInteger number;
    private CyclicBarrier countingBarrier;

    public FizzBuzz()
    {
        this.number = new AtomicInteger();
        this.countingBarrier = new CyclicBarrier(4);
    }
    public void applySolution()
    {
        //prints fizz instead of numbers that are divisible by 3
        Thread fizzThread = new Thread(new SeperateRunnable(x -> (x % 3 == 0 && x % 5 != 0),"Fizz",
                this.number, this.countingBarrier));

        //prints fizz instead of numbers that are divisible by 5
        Thread buzzThread = new Thread(new SeperateRunnable(x -> (x % 3 != 0 && x % 5 == 0), "Buzz",
                this.number, this.countingBarrier));

        //prints fizz instead of numbers that are divisible by 5 and 3
        Thread fizzBuzzThread = new Thread(new SeperateRunnable(x -> (x % 3 == 0 && x % 5 == 0), "FizzBuzz",
                this.number, this.countingBarrier));

        Thread regularNumbersThread = new Thread(new SeperateRunnable(x -> (x % 3 != 0 && x % 5 != 0), null,
                this.number, this.countingBarrier));

        fizzThread.start();
        fizzBuzzThread.start();
        buzzThread.start();
        regularNumbersThread.start();
    }

}
