package com.dangasaur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.*;

/**
 * Created by Dangasaur on 3/17/2016.
 * Don't steal my code?
 */
public class PromiseUtils {

    public static CompletableFuture<Void> completeOnInput(String message, String completeInput, String rejectInput) {
        CompletableFuture<Void> inputPromise = new CompletableFuture<>();
        daemonThreadFactory.newThread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input;
            while (!inputPromise.isDone()) {
                System.out.print(message);
                try {
                    input = reader.readLine();
                    if (input.equalsIgnoreCase(completeInput)) {
                        inputPromise.complete(null);
                    } else if (input.equalsIgnoreCase(rejectInput)) {
                        inputPromise.completeExceptionally(new UnsupportedOperationException("input matched rejected input"));
                    }
                } catch (IOException e) {
                    inputPromise.completeExceptionally(e);
                }
            }
        }).start();
        return inputPromise;
    }

    public static CompletableFuture<Void> completeOnTimeout(Duration duration) {
        CompletableFuture<Void> timeoutPromise = new CompletableFuture<>();
        scheduler.schedule(() -> timeoutPromise.complete(null), duration.toMillis(), TimeUnit.MILLISECONDS);
        return timeoutPromise;
    }

    public static CompletableFuture<Void> completeOnTimeoutOrInput(String message, String resolveInput, String rejectInput, Duration duration) {
        CompletableFuture<Void> inputPromise = completeOnInput(message, resolveInput, rejectInput);
        CompletableFuture<Void> timeoutPromise = completeOnTimeout(duration);
        return timeoutPromise.acceptEither(inputPromise, v -> {});
    }

    private static final ThreadFactory daemonThreadFactory = r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    };

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, daemonThreadFactory);
}
