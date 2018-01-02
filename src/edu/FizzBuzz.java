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
        //prints fizz instead of numbers that are divisible by 3
        Thread fizzThread = new Thread(new MyNestedClass(x -> (x % 3 == 0 && x % 5 != 0), "Fizz"));

        //prints fizz instead of numbers that are divisible by 5
        Thread buzzThread = new Thread(new MyNestedClass(x -> (x % 3 != 0 && x % 5 == 0), "Buzz"));

        //prints fizz instead of numbers that are divisible by 5 and 3
        Thread fizzBuzzThread = new Thread(new MyNestedClass(x -> (x % 3 == 0 && x % 5 == 0), "FizzBuzz"));

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

    private class MyNestedClass implements Runnable
    {
        private Predicate<Integer> predicate;
        private String printString;

        public MyNestedClass(Predicate<Integer> predicate, String printString)
        {
            this.predicate = predicate;
            this.printString = printString;
        }

        @Override
        public void run()
        {
            while (number.get() < FizzBuzz.maxCountValue)
            {
                testAndApply(this.predicate, this.printString);
            }
        }
    }
}
