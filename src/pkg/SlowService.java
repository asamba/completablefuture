package pkg;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SlowService {
    public String callSlowExternalService(String payload) {
        try {
            System.out.println("STARTING slowservice : payload = " + payload);
            Thread.sleep(1500);
            System.out.println("COMPLETED slowservice : payload = " + payload);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Enhanced " + payload;
    }


    public CompletableFuture<String> callSlowService(String payload) {

        ExecutorService yourOwnExecutor = Executors.newFixedThreadPool(2);

        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("in slowservice : payload = " + payload);
                Thread.sleep(1500);
                System.out.println("Completed slowservice : payload = " + payload);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Result of processing " + payload;
        }, yourOwnExecutor);
    }


}
