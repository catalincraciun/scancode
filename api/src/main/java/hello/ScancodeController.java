package hello;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScancodeController {

    private static final long upperBound = 8589934591L;
    private StorageGuard storage;
    private static final String apiKey =
        "7D8s2DJK23iD92jdDJksqEQewscxnr24j2Dsncsksddsjejdmnds2";
    private static final String template = "Alright, %s!";
    private final AtomicLong counter = new AtomicLong();

    public ScancodeController() {
        storage = new StorageGuard();
    }

    private long getUniqueCode() {
        long rand = ThreadLocalRandom.current().nextLong(upperBound);
        while (storage.contains(rand)) {
            rand = ThreadLocalRandom.current().nextLong(upperBound);
        }
        return rand;
    }

    @RequestMapping("/generateCode")
    public Greeting generateCode(
        @RequestParam(value="data", defaultValue="null") String data,
        @RequestParam(value="apiKey", defaultValue="null") String clientKey) {
        if (clientKey.equals(apiKey)) {
            // Authorised access
            try {
                long rand = getUniqueCode();
                Image generatedImage = new Image(rand);
                storage.add(rand, data);
                return new Greeting(true, generatedImage.getBase64());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Greeting(false, "null");
        }
        return new Greeting(false, "Unauthorised access");
    }

    @RequestMapping("/scanCode")
    public Greeting scanCode(
        @RequestParam(value="key", defaultValue="-1") String base64,
        @RequestParam(value="apiKey", defaultValue="null") String clientKey) {
        if (clientKey.equals(apiKey)) {
            // Authorised access
          return new Greeting(false, "null");
        }
        return new Greeting(false, "Unauthorised access");
    }

}
