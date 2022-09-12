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

        List<String> cities = Arrays.asList("Bangalore", "LA", "Chicago", "London");
        SlowService slowService = new SlowService();
        List<CompletableFuture<String>> completableFutureList = new ArrayList<>();

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("availableProcessors = " + availableProcessors);
        ExecutorService yourOwnExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()-2);

        for(String city : cities){
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> slowService.callSlowExternalService(city));
            completableFutureList.add(completableFuture);
        }

        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .exceptionally(ex -> null)
                .join();

        //Map<Boolean, List<CompletableFuture<String>>> collect = completableFutureList.stream().collect(Collectors.partitioningBy(CompletableFuture::isCompletedExceptionally));
        List<CompletableFuture<String>> collect = completableFutureList.stream().collect(Collectors.toList());
        for (CompletableFuture<String> item : collect) {

            String resultStr = item.get();
            System.out.println("result = " + resultStr);

        }
        System.out.println("Completed all the city processing");

    }

}
