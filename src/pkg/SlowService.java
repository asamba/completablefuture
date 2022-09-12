package pkg;

public class SlowService {
    public String callSlowExternalService(String payload) {
        try {
            System.out.println("STARTING slowservice : payload = " + payload);
            Thread.sleep(3000);
            System.out.println("COMPLETED slowservice : payload = " + payload);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Enhanced " + payload;
    }
}
