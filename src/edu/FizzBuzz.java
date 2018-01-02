package edu;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class FizzBuzz
{
    private static int maxCountValue = 20;

    private AtomicInteger number;
    private CyclicBarrier countingBarrier;
    private Object waitObject = new Object();

    public FizzBuzz()
    {
        this.number = new AtomicInteger();
        this.countingBarrier = new CyclicBarrier(4);
    }
    public void applySolution()
    {
        Thread fizzThread = new Thread(new Runnable()//prints fizz instead of numbers that are divisible by 3
        {
            @Override
            public void run()
            {
                while (number.get() < FizzBuzz.maxCountValue)
                {
                    Predicate<Integer> predicate = x -> (x % 3 == 0 && x % 5 != 0);
                    testAndApply(predicate, "Fizz");
                }
            }
        });

        Thread buzzThread = new Thread(new Runnable()//prints fizz instead of numbers that are divisible by 5
        {
            @Override
            public void run()
            {
                while (number.get() < FizzBuzz.maxCountValue)
                {
                    Predicate<Integer> predicate = x -> (x % 3 != 0 && x % 5 == 0);
                    testAndApply(predicate, "Buzz");
                }
            }
        });

        Thread fizzBuzzThread = new Thread(new Runnable()//prints fizz instead of numbers that are divisible by 5 and 3
        {
            @Override
            public void run()
            {
                while (number.get() < FizzBuzz.maxCountValue)
                {
                    Predicate<Integer> predicate = x -> (x % 3 == 0 && x % 5 == 0);
                    testAndApply(predicate, "FizzBuzz");
                }
            }
        });

        fizzThread.start();
        fizzBuzzThread.start();
        buzzThread.start();

        while (this.number.get() < FizzBuzz.maxCountValue)
        {//print regular numbers
            Predicate<Integer> predicate = x -> (x % 3 != 0 && x % 5 != 0);
            testAndApply(predicate, null);
        }

    }

    private void testAndApply(Predicate<Integer> predicate, String printValue)
    {
        try
        {
            if (predicate.test(this.number.get()))
            {
                this.countingBarrier.await(); // wait for all threads check the predicate for the current number
                int current = this.number.getAndIncrement();
                if (printValue != null)
                {
                    System.out.println(printValue);
                } else
                {
                    System.out.println(current);
                }
                this.countingBarrier.await(); // wait for the appropriate thread to print the expected value
            }
            else
            {

                this.countingBarrier.await(); // wait for all threads check the predicate for the current number
                this.countingBarrier.await(); // wait for the appropriate thread to print the expected value
            }
        } catch(InterruptedException e)
        //should spend some time thinking should handing such scenarios,
        // but this is not the focus for me at the moment
        {
            e.printStackTrace();
        } catch(BrokenBarrierException e)
        {
            e.printStackTrace();
        }
    }
}


//    int currentValue = this.number.get();
//            if (currentValue % 3 != 0 && currentValue % 5 != 0)
//                    {//this one is a regular number
//                    System.out.println(currentValue);
//                    this.countingBarrier.wait();
//                    this.number.getAndIncrement();
//                    this.waitObject.notifyAll();
//                    }
//                    else
//                    {
//                    this.countingBarrier.wait();
//                    this.
//                    }
