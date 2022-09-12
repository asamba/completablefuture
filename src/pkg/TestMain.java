package pkg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TestMain {

    public static void main(String[] args) throws Exception{

        long startTime = System.currentTimeMillis();

        List<String> cities = Arrays.asList("1", "2", "3", "4", "5", "6", "7");
        SlowService slowService = new SlowService();

        System.out.println("================= First Solution -- START ===========================");
        firstSolution(slowService, cities);
        System.out.println("================= First Solution -- END ===========================");

        System.out.println("================= Second Solution -- START ===========================");
        secondSolution(slowService, cities);
        System.out.println("================= Second Solution -- End ===========================");
    }

    public static void firstSolution(SlowService slowService, List<String> cities) throws Exception {

        long startTime = System.currentTimeMillis();

        List<CompletableFuture<String>> completableFutureList = new ArrayList<>();

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("availableProcessors = " + availableProcessors);
        //ExecutorService yourOwnExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ExecutorService yourOwnExecutor = Executors.newFixedThreadPool(1);

        for(String city : cities){
            System.out.println("Calling TestMain - slowService CompletableFuture for city = " + city);
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> slowService.callSlowExternalService(city), yourOwnExecutor);
            completableFutureList.add(completableFuture);
        }

        //wait for all completion
        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .exceptionally(ex -> null)
                .join();

        //Map<Boolean, List<CompletableFuture<String>>> collect = completableFutureList.stream().collect(Collectors.partitioningBy(CompletableFuture::isCompletedExceptionally));
        //print result
        List<CompletableFuture<String>> collect = completableFutureList.stream().collect(Collectors.toList());
        for (CompletableFuture<String> item : collect) {
            String resultStr = item.get();
            System.out.println("result = " + resultStr);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Completed all the city processing: Duration of execution "  + (endTime - startTime)/1000 + " secs");

    }

    public static void secondSolution(SlowService slowService, List<String> cities) throws Exception {

        long startTime = System.currentTimeMillis();

        CompletableFuture[] futures = cities
            .stream()
            .map(slowService::callSlowService)
            .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
        System.out.println(Arrays.stream(futures).map(CompletableFuture::join).toList());

        long endTime = System.currentTimeMillis();

        System.out.println("Completed all the city processing - Duration: " + (endTime - startTime)/1000 + "secs");
}

}
