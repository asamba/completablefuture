package pkg;

public class SlowService {
    public String callSlowExternalService(String payload) {
        try {
            System.out.println("in slowservice : payload = " + payload);
            Thread.sleep(1500);
            System.out.println("Completed slowservice : payload = " + payload);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Enhanced " + payload;
    }
}
