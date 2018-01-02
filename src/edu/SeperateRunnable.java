package edu;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class SeperateRunnable implements Runnable
{

    private Predicate<Integer> predicate;
    private String printString;
    private AtomicInteger number;
    private CyclicBarrier countingBarrier;


    public SeperateRunnable(Predicate<Integer> predicate, String printString, AtomicInteger number, CyclicBarrier countingBarrier)
    {
        this.predicate = predicate;
        this.printString = printString;
        this.number = number;
        this.countingBarrier = countingBarrier;
    }

    @Override
    public void run()
    {
        while (number.get() < FizzBuzz.maxCountValue)
        {
            testAndApply(this.predicate, this.printString);
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
