package com.dangasaur;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Created by Dangasaur on 3/17/2016.
 * Don't steal my code?
 */
public class Main {

    public static void main(String[] args) {
        try {

            PromiseUtils.completeOnTimeoutOrInput("enter A to continue or Q to quit: ", "A", "Q", Duration.of(10, ChronoUnit.SECONDS))
                    .thenAccept( v -> {
                        System.out.println("finally this is working...");
                    })
                    .get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
